package com.myproject.radiojourney.presentation.content.homeRadio

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.radiojourney.domain.homeRadio.IHomeRadioInteractor
import com.myproject.radiojourney.domain.logOut.ILogOutInteractor
import com.myproject.radiojourney.utils.extension.call
import com.myproject.radiojourney.model.presentation.RadioStationPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * ViewModel. Здесь осуществляется подписка, запрос через корутины. Работает с Interactor
 */
@HiltViewModel
class HomeRadioViewModel @Inject constructor(
    private val logOutInteractor: ILogOutInteractor,
    private val homeRadioInteractor: IHomeRadioInteractor
) : ViewModel() {
    companion object {
        private const val TAG = "HomeRadioViewModel"
    }

    private var mediaPlayer: MediaPlayer? = null
    private var audioUrl: String = ""

    val isRadioPlayingLiveData = MutableLiveData<Boolean>()
    val isRadioStoppedLiveData = MutableLiveData<Boolean>()
    val isUrlEmptyLiveData = MutableLiveData<Boolean>()
    val isStationSelectedLiveData = MutableLiveData<Boolean>()

    // Подписка на локальную БД
    val countryListFlow = homeRadioInteractor.subscribeOnCountryList()

    val radioStationSavedLiveData = MutableLiveData<RadioStationPresentation>()

    // LiveData, которые будут отвечать за отображение прогресса (кружок)
    val showProgressLiveData = MutableLiveData<Boolean>()
    val hideProgressLiveData = MutableLiveData<Boolean>()

    // LiveData для открытия диалогового окна
    val dialogInternetTroubleLiveData = MutableLiveData<Boolean>()

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            showProgressLiveData.call()
            logOutInteractor.onLogout()
            hideProgressLiveData.call()
        }
    }

//    fun getCountryListAndSaveToRoom() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                homeRadioInteractor.getCountryListAndSaveToRoom()
//            } catch (e: IOException) {
//                Log.d(TAG, "Exception: ${e.message}")
//                dialogInternetTroubleLiveData.call()
//                hideProgressLiveData.call()
//            }
//        }
//    }

    // Подгрузить радиостанцию из Shared Preference, если она там сохранена. Если нет - текст "выберите радиостанцию"
    fun getStoredRadioStation() {
        viewModelScope.launch(Dispatchers.IO) {
            // Узнаём, была ли ранее сохнанена радиостанция
            val isRadioStationStored = homeRadioInteractor.isRadioStationStored()
            Log.d(
                TAG,
                "Узнали, была ли сохранена радиостанция: $isRadioStationStored"
            )
            // Если да - выводим её на экран
            if (isRadioStationStored) {
                // Узнаём URL
                val radioStationUrl: String =
                    homeRadioInteractor.getRadioStationUrl().toString()
                // Находим в Room
                val radioStationSaved: RadioStationPresentation? =
                    homeRadioInteractor.getRadioStationSaved(radioStationUrl)
                // Если всё ок - подгружаем
                radioStationSaved?.let { nonNullRadioStation ->
                    Log.d(
                        TAG,
                        "Передаются значения в LiveData: nonNullRadioStation = $nonNullRadioStation"
                    )
                    radioStationSavedLiveData.postValue(nonNullRadioStation)
                    audioUrl = nonNullRadioStation.url
                    isStationSelectedLiveData.call()
                }
            }
        }
    }

    // Получаем радиостанцию из списка на предыдущей странице, если перешли сюда из списка радиостанций
    fun saveRadioStationAndShow(radioStation: RadioStationPresentation) {
        viewModelScope.launch(Dispatchers.IO) {
            // Поменять в Shared Preference setIsRadioStationStored на true
            // Сохранить в Shared Preference (url) и Room (станцию)
            homeRadioInteractor.saveRadioStationUrl(true, radioStation)
            // Отобразить
            radioStationSavedLiveData.postValue(radioStation)
            audioUrl = radioStation.url
            isStationSelectedLiveData.call()
        }
    }

    // PLAY URL -> 4. MediaPlayer play url
    fun playAudio() {
        viewModelScope.launch(Dispatchers.IO) {

            if (audioUrl.isNotBlank()) {
                mediaPlayer = null // Не всегда сбрасывается

                // initializing media player
                mediaPlayer = MediaPlayer()

                // below line is use to set the audio stream type for our media player.

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mediaPlayer!!.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                } else {
                    mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                }

                // below line is use to set our url to our media player.
                try {
                    try {
                        mediaPlayer!!.setDataSource(audioUrl)
                    } catch (e: IOException) {
                        isUrlEmptyLiveData.call()
                        e.printStackTrace()
                    }
                    // below line is use to prepare and start our media player.
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                // below line is use to display a toast message.
                isRadioPlayingLiveData.call()
            } else {
                isUrlEmptyLiveData.call()
            }

        }
    }

    // checking the media player if the audio is playing or not.
    fun stopAudio() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                mediaPlayer?.let { nonNullMediaPlayer ->
                    if (nonNullMediaPlayer.isPlaying) {
                        // pausing the media player
                        // if media player is playing we are calling below line to stop our media player.
                        nonNullMediaPlayer.stop()
//                        nonNullMediaPlayer.reset() -> Вместо этого - mediaPlayer = null. This is only for those who get error when your activity is in stop or in resume state.
                        nonNullMediaPlayer.release()
                        mediaPlayer = null

                        // below line is to display a message when media player is paused.
                        isRadioStoppedLiveData.call()
                    }
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace();
            }

        }
    }

}