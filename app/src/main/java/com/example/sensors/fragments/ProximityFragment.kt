package com.example.sensors.fragments

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import com.example.sensors.R
import com.example.sensors.databinding.FragmentProximityBinding


class ProximityFragment:Fragment(),SensorEventListener
{
    private lateinit var sensorManager:SensorManager
    private var proximitySensor: Sensor? = null

    private lateinit var wakeLock:PowerManager.WakeLock

    private lateinit var binding:FragmentProximityBinding

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    }

    override fun onResume()
    {
        super.onResume()
        proximitySensor?.let { sensor -> sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)}
    }

    override fun onPause()
    {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_proximity, container, false)
        return binding.root
    }

    override fun onSensorChanged(event:SensorEvent?)
    {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PROXIMITY)
            {
                val distance = it.values[0]
                setTextViews()

                if (distance < proximitySensor?.maximumRange ?: 0f)
                {
                    turnOffScreen()
                }
                else
                {
                    turnOnScreen()
                }
            }
        }
    }

    private fun turnOffScreen()
    {
        val powerManager = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyTag:ProximityScreenOff")
        wakeLock.acquire()
    }

    private fun turnOnScreen()
    {
        val powerManager = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyTag:ProximityScreenOn")
        wakeLock.acquire()
    }

    override fun onAccuracyChanged(sensor:Sensor?, accuracy:Int)
    {

    }

    private fun setTextViews()
    {
        if(proximitySensor!=null)
        {
            binding.apply {
                proxStatus.text="Available"
                proximitySensor?.let {
                    proxVendor.text=it.vendor
                    proxName.text=it.name
                    proxVersion.text=it.version.toString()
                    proxPower.text="${it.power} mA"
                    proxResolution.text=it.resolution.toString() +" m/s2"
                    proxRange.text=it.maximumRange.toString() +" m/s2"
                }
            }
        }
    }
}