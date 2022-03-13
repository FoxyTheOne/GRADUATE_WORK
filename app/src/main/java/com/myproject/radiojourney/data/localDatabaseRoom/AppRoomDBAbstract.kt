package com.myproject.radiojourney.data.localDatabaseRoom

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myproject.radiojourney.model.local.CountryLocal
import com.myproject.radiojourney.model.local.RadioStationLocal
import com.myproject.radiojourney.model.local.UserEntity

@Database(
    entities = [UserEntity::class, CountryLocal::class, RadioStationLocal::class],
    version = 3,
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration(from = 2, to = 3)
//    ]
)
@TypeConverters(LatLngConverter::class)
abstract class AppRoomDBAbstract : RoomDatabase() {
    abstract fun getUserDAO(): IUserDAO
    abstract fun getCountryDAO(): ICountryDAO
    abstract fun getRadioStationDAO(): IRadioStationDAO
}