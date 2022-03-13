package com.myproject.radiojourney.model.presentation

import com.myproject.radiojourney.model.local.RadioStationLocal

data class RadioStationPresentation (val stationName : String, val url : String, val clickCount : Int, val countryCode : String) {

    companion object {
        fun fromLocalToPresentation(radioStationLocal: RadioStationLocal): RadioStationPresentation = RadioStationPresentation(
            stationName = radioStationLocal.stationName,
            url = radioStationLocal.url,
            clickCount = radioStationLocal.clickCount,
            countryCode = radioStationLocal.countryCode
        )
    }

}