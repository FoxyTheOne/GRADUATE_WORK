package com.myproject.radiojourney.utils.service

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
import com.myproject.radiojourney.model.local.CountryLocal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.myproject.radiojourney.MainActivity




/**
 * Создадим Foreground Service
 * FOREGROUND_SERVICE -> 1. Для начала, задекларируем Foreground Service в Manifest. А так же добавим туда разрешение для Foreground Service
 */
@AndroidEntryPoint
class ProgressForegroundService @Inject constructor() : Service() {
    companion object {
        private const val TAG = "ProgressForeground"
        private const val CHANNEL_CASHING_ID = "CHANNEL_CASHING_ID" // 5
        private const val CHANNEL_CASHING_FAILED_ID = "CHANNEL_CASHING_FAILED_ID" // 6
    }

    @Inject
    lateinit var networkRadioDataSource: INetworkRadioDataSource

    @Inject
    lateinit var localRadioDataSource: ILocalRadioDataSource

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationBuilder2: NotificationCompat.Builder? = null
    private lateinit var alertDialog: AlertDialog
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onCreate() {
        super.onCreate()

        // FOREGROUND_SERVICE -> 3. Вызываем метод для создания Channel
        // Добавляем проверку, т.к. создавать NotificationChannel можно только начиная с API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelCashingCountries()
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
            .setOnlyAlertOnce(true) // Без этого при обновлении уведомления каждый раз будет издаваться звук

        // 4.2. Для того, чтобы Service из обычного перешел в Foreground, нам нужно вызвать метод startForeground() внутри этого сервиса
        startForeground(5, notificationBuilder?.build())

        // 4.3. И далее вызываем метод, который будет обновлять наш notification
        updateProgress(this)


//        // NOTIFICATION -> 1. Notification for minimum target API level is 4+
//        notificationBuilder2 = NotificationCompat.Builder(this, CHANNEL_CASHING_FAILED_ID)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle(getString(R.string.foregroundNotification_failedName))
//            .setContentText(getString(R.string.foregroundNotification_name))
//
//        val targetIntent = Intent(this, HomeRadioFragment::class.java)
//        val contentIntent =
//            PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//        notificationBuilder2?.setContentIntent(contentIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        serviceJob.cancel()
    }

    // FOREGROUND_SERVICE -> 2. Создадим Channel CashingCountries
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelCashingCountries() {
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
            try {
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



//                    try {
//                        val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
//                        val addresses: List<*> = geocoder.getFromLocation(latitude, longitude, 1)
//                        val addresslist = addresses
//                        val address: String = addresslist[0].getAddressLine(0)
//                        Log.d("add", address)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }




                    // Узнаем местоположение
                    // В этом месте часто исключение, как будто проблема с интернетом
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

                    Log.d(
                        TAG,
                        "Успешное преобразование стран. Получен результат [0]: ${countryLocalList[0]}"
                    )

                // Теперь сохраним наши страны в Room
                localRadioDataSource.saveCountryList(countryLocalList)


                // 5.4. Когда прогресс заканчивается, закрываем Foreground, удаляем уведомления, stop service
                stopForeground(true)
                notificationManager.cancelAll()
//            stopSelf() -> Не убиваю сервис, чтобы не запускался запрос при каждом переходе на главную страницу.
            } catch (e: IOException) {
                Log.d(
                    TAG,
                    "Exception: ${e.message}. Please, try turn on and then turn off airplane mode (on the emulator). And then restart the program, if needed."
                )
//                // NOTIFICATION -> 2. Notification for minimum target API level is 4+
//                val nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//                nManager.notify(6, notificationBuilder2?.build())
                // TODO сброс и перезапуск кеширования
            }
        }
    }

    // FOREGROUND_SERVICE -> 6. Запустим наш Foreground Service из NotificationFragment
}