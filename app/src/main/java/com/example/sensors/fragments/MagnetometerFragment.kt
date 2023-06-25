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
import androidx.databinding.DataBindingUtil
import com.example.sensors.R
import com.example.sensors.databinding.FragmentMagnetometerBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MagnetometerFragment:Fragment(),SensorEventListener
{

    private lateinit var sensorManager:SensorManager
    private  var magnetometer:Sensor?=null
    private lateinit var chart:LineChart

    private lateinit var lineData:LineData
    private lateinit var lineDataSetX:LineDataSet
    private lateinit var lineDataSetY:LineDataSet
    private lateinit var lineDataSetZ:LineDataSet

    private lateinit var binding:FragmentMagnetometerBinding

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)

        sensorManager=requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magnetometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_magnetometer, container, false)

        chart=binding.magChart
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        configureChart()
    }
    override fun onResume()
    {
        super.onResume()
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause()
    {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event:SensorEvent)
    {
        if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD)
        {

            setTextViews()

            val x=event.values[0]
            val y=event.values[1]
            val z=event.values[2]

            lineData.addEntry(Entry(lineDataSetX.entryCount.toFloat(), x), 0)
            lineData.addEntry(Entry(lineDataSetY.entryCount.toFloat(), y), 1)
            lineData.addEntry(Entry(lineDataSetZ.entryCount.toFloat(), z), 2)

            lineData.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()

        }
    }

    override fun onAccuracyChanged(sensor:Sensor?, accuracy:Int)
    {

    }

    private fun configureChart()
    {
        chart.setTouchEnabled(false)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        val description=Description()
        description.text="Magnetometer"
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
        if(magnetometer!=null)
        {
            binding.apply {
                magStatus.text="Available"
                magnetometer?.let {
                    magVendor.text=it.vendor
                    magName.text=it.name
                    magVersion.text=it.version.toString()
                    magPower.text="${it.power} mA"
                    magResolution.text=it.resolution.toString() +" m/s2"
                    magRange.text=it.maximumRange.toString() +" m/s2"
                }
            }
        }
    }

}