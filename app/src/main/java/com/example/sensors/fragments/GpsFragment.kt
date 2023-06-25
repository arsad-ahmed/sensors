package com.example.sensors.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.room.RoomDatabase
import com.example.sensors.R
import com.example.sensors.database.LocationDao
import com.example.sensors.databinding.FragmentGpsBinding
import com.example.sensors.model.LocationModel
import com.example.sensors.viewmodel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


@AndroidEntryPoint
class GpsFragment:Fragment(),LocationListener
{

    private lateinit var locationManager:LocationManager
    private lateinit var binding:FragmentGpsBinding

    private val locationViewModel by viewModels<LocationViewModel>()

    private var latitude=0.0
    private var longitude=0.0

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted ->
        if (isGranted)
        {
            checkGpsEnabled()
        }
        else
        {
            Toast.makeText(requireContext(), "Location Permission is required for this app. Please enable Location.", Toast.LENGTH_SHORT).show()
        }
    }

    private val gpsSettingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK)
        {
            startLocationUpdates()
        }
        else
        {
            checkGpsEnabled()
        }
    }

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        locationManager=requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkPermissions()


    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_gps, container, false)
        binding.fragment=this
        return binding.root
    }

    private fun checkPermissions()
    {
        val permissions=Manifest.permission.ACCESS_FINE_LOCATION
        locationPermissionLauncher.launch(permissions)
    }

    private fun startLocationUpdates()
    {
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }

    }

    override fun onLocationChanged(location:Location)
    {
        latitude=location.latitude
        longitude=location.longitude

        binding.apply {
            gpsLatTv.text=latitude.toString()
            gpsLonTv.text=longitude.toString()
        }
    }

    fun saveCurrentLocation()
    {
        if(latitude==0.0 && longitude==0.0)
        {
            Toast.makeText(requireContext(), "please wait,fetching your current location", Toast.LENGTH_SHORT).show()
        }
        else
        {
            locationViewModel.saveLocation(LocationModel(latitude = latitude, longitude = longitude))
            Toast.makeText(requireContext(), "Location saved successfully to local database", Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkGpsEnabled()
    {
        val isGpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!isGpsEnabled)
        {
            AlertDialog.Builder(requireContext()).setTitle("Enable GPS")
                .setMessage("GPS is required for this app. Please enable GPS.")
                .setPositiveButton("OK") {dialog, _ ->
                    dialog.dismiss()
                    val intent=Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    gpsSettingsLauncher.launch(intent)
                }.setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }.setCancelable(false).show()
        }
        else
        {
            startLocationUpdates()
        }
    }

}