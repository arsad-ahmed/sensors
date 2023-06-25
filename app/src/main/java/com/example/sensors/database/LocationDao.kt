package com.example.sensors.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.sensors.model.LocationModel


@Dao
interface LocationDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(locationModel:LocationModel)
}