package com.myproject.radiojourney.domain.iRepository

import com.myproject.radiojourney.model.local.CountryLocal
import com.myproject.radiojourney.model.presentation.RadioStationPresentation
import com.myproject.radiojourney.model.remote.RadioStationRemote
import kotlinx.coroutines.flow.Flow

/**
 * Repository. Domain layer.
 */
interface IContentRepository {
    suspend fun getCountryListAndSaveToRoom()
    fun subscribeOnCountryList(): Flow<List<CountryLocal>>
//    fun getRadioStationList(): List<RadioStationRemote>
}