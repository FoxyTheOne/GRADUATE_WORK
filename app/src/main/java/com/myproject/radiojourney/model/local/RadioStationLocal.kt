package com.myproject.radiojourney.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myproject.radiojourney.model.presentation.RadioStationPresentation
import com.myproject.radiojourney.model.remote.RadioStationRemote

@Entity
data class RadioStationLocal(
    @ColumnInfo(name = "stationName") val stationName: String,
    @PrimaryKey
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "clickCount") val clickCount: Int,
    @ColumnInfo(name = "countrycode") val countryCode : String
) {

    companion object {
        fun fromRemoteToLocal(remote: RadioStationRemote): RadioStationLocal = RadioStationLocal(
            stationName = remote.name,
            url = remote.url,
            clickCount = remote.clickcount,
            countryCode = remote.countrycode
        )

        fun fromPresentationToLocal(presentation: RadioStationPresentation): RadioStationLocal = RadioStationLocal(
            stationName = presentation.stationName,
            url = presentation.url,
            clickCount = presentation.clickCount,
            countryCode = presentation.countryCode
        )
    }

}