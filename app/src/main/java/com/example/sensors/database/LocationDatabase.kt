package com.example.sensors.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sensors.model.LocationModel

@Database(entities = [LocationModel::class], version = 1)
abstract class LocationDatabase:RoomDatabase()
{
    abstract fun getDao():LocationDao
}