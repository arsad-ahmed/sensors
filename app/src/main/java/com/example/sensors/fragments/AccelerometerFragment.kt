package com.example.sensors.fragments

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import com.example.sensors.R
import com.example.sensors.databinding.FragmentAccelerometerBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.lang.Math.abs
import java.lang.Math.sqrt
import kotlin.math.sqrt

class AccelerometerFragment:Fragment(),SensorEventListener
{

    private lateinit var sensorManager:SensorManager
    private  var accelerometer:Sensor?=null


    private val THRESHOLD = 2.0f


    private lateinit var binding:FragmentAccelerometerBinding

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)

        sensorManager=requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


    }

    override fun onCreateView(inflater:LayoutInflater,
        container:ViewGroup?,
        savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_accelerometer, container, false)

        return binding.root
    }


    override fun onResume()
    {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause()
    {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor:Sensor?, accuracy:Int)
    {
        // Not needed for this example
    }

    override fun onSensorChanged(event:SensorEvent)
    {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER)
        {

            setTextViews()

            val x=event.values[0]
            val y=event.values[1]
            val z=event.values[2]


            val acceleration=sqrt(x * x + y * y + z * z)

            if(acceleration>THRESHOLD)
            {
                if(abs(x)>abs(y))
                {
                    if(x>0)
                    {
                        // Device is in landscape mode with the screen facing right
                        binding.accIv.rotation=90f
                    }
                    else
                    {
                        // Device is in landscape mode with the screen facing left
                        binding.accIv.rotation=270f
                    }
                }
                else
                {
                    if(y>0)
                    {
                        // Device is in portrait mode with the screen facing up
                        binding.accIv.rotation=0f
                    }
                    else
                    {
                        // Device is in portrait mode with the screen facing down
                        binding.accIv.rotation=180f
                    }
                }
            }
        }

    }
    private fun setTextViews()
    {
        if(accelerometer!=null)
        {
            binding.apply {
                accStatus.text="Available"
                accelerometer?.let {
                    accVendor.text=it.vendor
                    accName.text=it.name
                    accVersion.text=it.version.toString()
                    accPower.text="${it.power} mA"
                    accResolution.text=it.resolution.toString() +" m/s2"
                    accRange.text=it.maximumRange.toString() +" m/s2"
                }
            }
        }
    }
}

