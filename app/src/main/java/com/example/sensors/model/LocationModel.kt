package com.example.sensors.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationModel(
    @PrimaryKey(autoGenerate = true)
    var id :Int=0,
    val latitude:Double,
    val longitude:Double)
