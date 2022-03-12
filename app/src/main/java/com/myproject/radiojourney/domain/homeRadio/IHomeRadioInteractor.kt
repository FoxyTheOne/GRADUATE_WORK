package com.myproject.radiojourney.domain.homeRadio

import com.myproject.radiojourney.model.presentation.CountryPresentation
import com.myproject.radiojourney.model.presentation.RadioStationPresentation
import kotlinx.coroutines.flow.Flow

interface IHomeRadioInteractor {
    suspend fun getCountryListAndSaveToRoom()
    fun subscribeOnCountryList(): Flow<List<CountryPresentation>>
//    fun getRadioStationList(): List<RadioStationPresentation>
}