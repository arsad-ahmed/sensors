package com.example.sensors.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensors.database.LocationRepository
import com.example.sensors.model.LocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val locationRepository:LocationRepository):ViewModel()
{
    fun saveLocation(locationModel:LocationModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.saveLocation(locationModel)
        }
    }
}