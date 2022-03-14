package com.myproject.radiojourney.utils.service

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.maps.model.LatLng
import com.myproject.radiojourney.R
import com.myproject.radiojourney.data.dataSource.local.radio.ILocalRadioDataSource
import com.myproject.radiojourney.data.dataSource.network.INetworkRadioDataSource
import com.myproject.radiojourney.data.repository.ContentRepository
import com.myproject.radiojourney.domain.homeRadio.IHomeRadioInteractor
import com.myproject.radiojourney.model.local.CountryLocal
import com.myproject.radiojourney.presentation.content.homeRadio.HomeRadioFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Создадим Foreground Service
 * FOREGROUND_SERVICE -> 1. Для начала, задекларируем Foreground Service в Manifest. А так же добавим туда разрешение для Foreground Service
 */
@AndroidEntryPoint
class ProgressForegroundService @Inject constructor() : Service() {
    companion object {
        private const val TAG = "ProgressForeground"
        private const val CHANNEL_CASHING_ID = "CHANNEL_CASHING_ID"
    }

    @Inject
    lateinit var networkRadioDataSource: INetworkRadioDataSource

    @Inject
    lateinit var localRadioDataSource: ILocalRadioDataSource

    private var notificationBuilder: NotificationCompat.Builder? = null
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private lateinit var dialogInternetTrouble: Dialog

    override fun onCreate() {
        super.onCreate()

        // Настройки диалогового окна
        dialogInternetTrouble = Dialog(this)
        // Передайте ссылку на разметку
        dialogInternetTrouble.setContentView(R.layout.layout_internet_trouble_dialog)

        // FOREGROUND_SERVICE -> 3. Вызываем метод для создания Channel
        // Добавляем проверку, т.к. создавать NotificationChannel можно только начиная с API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelCashingCounties()
        }

        // FOREGROUND_SERVICE -> 4. Channel создан, теперь можно приступить непосредственно к созданию уведомления
        // 4.1. Создаём notification c помощью билдера, который первым будет показываться нашему пользователю
        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_CASHING_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.foregroundNotification_name))
            .setContentText(getString(R.string.foregroundNotification_name))
            // Для отображения прогресса добавляем .setProgress()
            .setProgress(100, 0, false)
            // Так же добавим приоритет
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        // 4.2. Для того, чтобы Service из обычного перешел в Foreground, нам нужно вызвать метод startForeground() внутри этого сервиса
        startForeground(5, notificationBuilder?.build())

        // 4.3. И далее вызываем метод, который будет обновлять наш notification
        try {
            updateProgress(this)
        } catch (e: IOException) {
            Log.d(TAG, "Exception: ${e.message}")
            dialogInternetTrouble.show()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        serviceJob.cancel()
    }

    // FOREGROUND_SERVICE -> 2. Создадим Channel CashingCounties
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelCashingCounties() {
        // 2.4. Находим NotificationManager
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 2.1. Находим наши строки
        val channelNameCashingCounties = getString(R.string.foregroundNotification_name)
        val channelDescriptionCashingCounties =
            getString(R.string.foregroundNotification_description)
        // 2.2. Настраиваем приоритет
        val importanceCashingCounties = NotificationManager.IMPORTANCE_DEFAULT

        // 2.3. Создаём Channel и регистрируем его
        val channelCashingCounties = NotificationChannel(
            CHANNEL_CASHING_ID,
            channelNameCashingCounties,
            importanceCashingCounties
        ).apply {
            description = channelDescriptionCashingCounties
        }

        // 2.5. И вызываем у него (NotificationManager) метод createNotificationChannel(), куда передаём наш channel
        notificationManager.createNotificationChannel(channelCashingCounties)
    }

    // FOREGROUND_SERVICE -> 5. Обновляем наш notification
    private fun updateProgress(context: Context) {
        // 5.1. Находим NotificationManager
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 5.2 Описываем обновление нашего notification
        serviceScope.launch(Dispatchers.IO) {
            // Получаем список кодов стран из networkRadioDataSource
            val countryCodeRemoteList = networkRadioDataSource.getCountryCodeList()

            val listSize = countryCodeRemoteList.size

            // Преобразуем коды (remote) в читабельные страны (local) с локацией
            val countryLocalList = mutableListOf<CountryLocal>()

            val geocoder = Geocoder(context)
            var addresses = mutableListOf<Address>()

            var countryCodeRemoteCount = 0
            countryCodeRemoteList.forEach { countryCodeRemote ->
                countryCodeRemoteCount += 1

                // Узнаем название страны
                val loc: Locale = Locale("", countryCodeRemote.name)
                val countryName = loc.displayName
                Log.d(TAG, "результат countryName: $countryName")

                // Узнаем местоположение
                // В этом месте вылетает, если проблема с интернетом
                addresses = geocoder.getFromLocationName(countryName, 1)
                var latitude: Double = 0.0
                var longitude: Double = 0.0
                if (addresses.size > 0) {
                    latitude = addresses[0].latitude;
                    longitude = addresses[0].longitude;
                }
                Log.d(TAG, "результат addresses: $latitude, $longitude")

                // remote -> local
                val countryLocal = CountryLocal.fromRemoteToLocal(
                    countryCodeRemote,
                    countryName = countryName,
                    countryLocation = LatLng(latitude, longitude)
                )
                countryLocalList.add(countryLocal)

                // Если notificationBuilder != null
                notificationBuilder?.let { builder ->
                    builder
                        .setContentText("Progress: $countryCodeRemoteCount files")
                        .setProgress(listSize, countryCodeRemoteCount, false)
                        .setSound(null)
                    // 5.3. Передаём notificationManager наш билдер notification
                    notificationManager.notify(5, builder.build())
                }
            }

            countryLocalList.forEach { countryLocal ->
                Log.d(
                    TAG,
                    "результат преобразования названия страны: ${countryLocal.countryName}"
                )
            }

            // Теперь сохраним наши страны в Room
            localRadioDataSource.saveCountryList(countryLocalList)


            // 5.4. Когда прогресс заканчивается, закрываем Foreground, удаляем уведомления, stop service
            stopForeground(true)
            notificationManager.cancelAll()
//            stopSelf() -> Не убиваю сервис, чтобы не запускался запрос при каждом переходе на главную страницу.

        }
    }

    // FOREGROUND_SERVICE -> 6. Запустим наш Foreground Service из NotificationFragment
}