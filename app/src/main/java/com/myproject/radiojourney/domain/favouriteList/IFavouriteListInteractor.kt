package com.myproject.radiojourney.domain.favouriteList

import com.myproject.radiojourney.model.presentation.RadioStationFavouritePresentation

interface IFavouriteListInteractor {
    suspend fun getToken(): Int?
    suspend fun getRadioStationFavouriteList(userCreatorIdInt: Int): List<RadioStationFavouritePresentation>
}