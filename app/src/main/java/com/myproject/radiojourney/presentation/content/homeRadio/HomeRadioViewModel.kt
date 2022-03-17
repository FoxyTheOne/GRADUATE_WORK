package com.myproject.radiojourney.presentation.content.homeRadio

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
        }
    }

}