package com.example.sensors.fragments

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sensors.R
import com.example.sensors.databinding.FragmentGyroscopeBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class GyroscopeFragment:Fragment(),SensorEventListener
{

    private lateinit var sensorManager:SensorManager
    private var gyroscopeSensor:Sensor?=null

    private lateinit var chart:LineChart

    private lateinit var lineData: LineData
    private lateinit var lineDataSetX: LineDataSet
    private lateinit var lineDataSetY: LineDataSet
    private lateinit var lineDataSetZ: LineDataSet

    private lateinit var binding:FragmentGyroscopeBinding

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)

        sensorManager=requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    override fun onResume()
    {
        super.onResume()
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause()
    {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_gyroscope, container, false)
        chart=binding.gyroChart
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setupLineChart()
    }

    override fun onSensorChanged(event:SensorEvent)
    {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE)
        {
            setTextViews()

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lineData.addEntry(Entry(lineDataSetX.entryCount.toFloat(), x), 0)
            lineData.addEntry(Entry(lineDataSetY.entryCount.toFloat(), y), 1)
            lineData.addEntry(Entry(lineDataSetZ.entryCount.toFloat(), z), 2)

            lineData.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }

        else
        {
            setTextViews()
        }
    }

    override fun onAccuracyChanged(sensor:Sensor?, accuracy:Int)
    {

    }

    private fun setupLineChart()
    {
        chart.setTouchEnabled(false)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val description=Description()
        description.text="Gyroscope"
        chart.description=description


        lineData = LineData()
        lineDataSetX = createLineDataSet("X", requireContext().getColor(R.color.red))
        lineDataSetY = createLineDataSet("Y", requireContext().getColor(R.color.green))
        lineDataSetZ = createLineDataSet("Z", requireContext().getColor(R.color.blue))

        lineData.addDataSet(lineDataSetX)
        lineData.addDataSet(lineDataSetY)
        lineData.addDataSet(lineDataSetZ)

        chart.data = lineData
        chart.invalidate()


    }

    private fun createLineDataSet(label:String, color:Int):LineDataSet
    {
        val lineDataSet=LineDataSet(null, label)
        lineDataSet.color=color
        lineDataSet.lineWidth=1f
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawValues(false)
        return lineDataSet
    }

    private fun setTextViews()
    {
        if(gyroscopeSensor!=null)
        {
            binding.apply {
                gyroStatus.text="Available"
                gyroscopeSensor?.let {
                    gyroVendor.text=it.vendor
                    gyroName.text=it.name
                    gyroVersion.text=it.version.toString()
                    gyroPower.text="${it.power} mA"
                    gyroResolution.text=it.resolution.toString() +" m/s2"
                    gyroRange.text=it.maximumRange.toString() +" m/s2"
                }
            }
        }

        else
        {
            binding.apply {
                gyroStatus.text="Not Available"
                gyroName.text="Not Available"
                gyroVendor.text="Not Available"
                gyroVersion.text="Not Available"
                gyroPower.text="Not Available"
                gyroResolution.text="Not Available"
                gyroRange.text="Not Available"
            }
        }
    }
}