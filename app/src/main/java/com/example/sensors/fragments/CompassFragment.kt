package com.example.sensors.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import com.example.sensors.R
import com.example.sensors.databinding.FragmentCompassBinding


class CompassFragment:Fragment(),SensorEventListener
{

    private lateinit var sensorManager:SensorManager
    private lateinit var compassImage: AppCompatImageView
    private var currentDegree = 0f

    private var accelerometer:Sensor?=null
    private var magnetometer:Sensor?=null

    private lateinit var binding:FragmentCompassBinding


    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_compass, container, false)
        compassImage=binding.compassIv

        initSensor()
        return binding.root
    }


    private fun initSensor()
    {
        sensorManager=requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (accelerometer != null && magnetometer != null)
        {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        } else
        {
            Toast.makeText(requireContext(), "Sensors not available", Toast.LENGTH_SHORT).show()
            binding.compassInfo.text="Required sensors are not available in your device"
        }

    }

    override fun onResume()
    {
        super.onResume()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause()
    {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event:SensorEvent)
    {
        if (event.sensor.type == Sensor.TYPE_ORIENTATION)
        {
            val degree = event.values[0]
            val direction = getDirection(degree)

            val rotateAnimation = RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 200
            rotateAnimation.fillAfter = true

            compassImage.startAnimation(rotateAnimation)
            currentDegree = -degree

            binding.compassTv.text="${degree.toInt()}° $direction"
        }
    }

    override fun onAccuracyChanged(sensor:Sensor?, accuracy:Int)
    {


    }

    private fun getDirection(degree: Float): String
    {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val index = ((degree + 22.5) / 45).toInt() and 7
        return directions[index]
    }
}