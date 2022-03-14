package com.myproject.radiojourney.model.presentation

import android.os.Parcelable
import com.myproject.radiojourney.model.local.RadioStationLocal
import kotlinx.parcelize.Parcelize

@Parcelize
data class RadioStationPresentation (val stationName : String, val url : String, val clickCount : Int, val countryCode : String) :
    Parcelable {

    companion object {
        fun fromLocalToPresentation(radioStationLocal: RadioStationLocal): RadioStationPresentation = RadioStationPresentation(
            stationName = radioStationLocal.stationName,
            url = radioStationLocal.url,
            clickCount = radioStationLocal.clickCount,
            countryCode = radioStationLocal.countryCode
        )
    }

}