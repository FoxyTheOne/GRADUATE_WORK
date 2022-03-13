package com.myproject.radiojourney.domain.radioList

import android.util.Log
import com.myproject.radiojourney.data.repository.ContentRepository
import com.myproject.radiojourney.domain.iRepository.IContentRepository
import com.myproject.radiojourney.model.local.RadioStationLocal
import com.myproject.radiojourney.model.presentation.RadioStationPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Interactor. Domain layer. Работает только с Repository.
 * Interactor ответственен за обеспечение данными отдельные экраны (для каждого экрана - отдельный Interactor)
 * При работе с model, здесь происходит преобразование local -> presentation (опционально)
 */
class RadioListInteractor @Inject constructor(
    private val contentRepository: IContentRepository
) : IRadioListInteractor {
    override suspend fun getRadioStationList(countryCode: String): List<RadioStationPresentation> {
        val radioStationLocalList = contentRepository.getRadioStationList(countryCode)

        // Преобразуем модельки local -> presentation
        val radioStationPresentationList = mutableListOf<RadioStationPresentation>()

        radioStationLocalList.forEach { radioStationLocal ->
            val radioStationPresentation = RadioStationPresentation.fromLocalToPresentation(radioStationLocal)
            radioStationPresentationList.add(radioStationPresentation)
        }

        return radioStationPresentationList.toList()
    }
}