package com.example.sensors.database

import com.example.sensors.model.LocationModel
import javax.inject.Inject

class LocationRepository @Inject constructor(private val locationDao:LocationDao)
{
    suspend fun saveLocation(locationModel:LocationModel)
    {
        locationDao.saveLocation(locationModel)
    }
}