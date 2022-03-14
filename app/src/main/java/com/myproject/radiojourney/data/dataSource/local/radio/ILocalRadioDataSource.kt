package com.myproject.radiojourney.data.dataSource.local.radio

import com.myproject.radiojourney.model.local.CountryLocal
import com.myproject.radiojourney.model.local.RadioStationLocal
import com.myproject.radiojourney.model.presentation.RadioStationPresentation
import kotlinx.coroutines.flow.Flow

interface ILocalRadioDataSource {
    suspend fun saveCountryList(countryLocalList: MutableList<CountryLocal>)
    fun subscribeOnCountryList(): Flow<List<CountryLocal>>

    suspend fun isRadioStationStored(): Boolean
    suspend fun getRadioStationUrl(): String?
    suspend fun getRadioStationSaved(radioStationUrl: String): RadioStationLocal?

    suspend fun saveRadioStationUrl(isStored: Boolean, radioStation: RadioStationLocal)
}