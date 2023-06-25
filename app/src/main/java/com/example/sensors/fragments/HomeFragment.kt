package com.example.sensors.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.sensors.R
import com.example.sensors.databinding.FragmentHomeBinding

class HomeFragment : Fragment()
{
    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        currentDeviceDetails()
        binding.fragment=this
        return binding.root
    }


    private fun currentDeviceDetails()
    {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        binding.mainTv.text="${manufacturer.uppercase()} $model"
    }

    fun moveToAccelerometerFragment()
    {
        val action = HomeFragmentDirections.actionHomeFragmentToAccelerometerFragment()
        findNavController().navigate(action)
    }

    fun moveToGyroscopeFragment()
    {
        val action = HomeFragmentDirections.actionHomeFragmentToGyroscopeFragment()
        findNavController().navigate(action)
    }

    fun moveToMagnetometerFragment()
    {
        val action = HomeFragmentDirections.actionHomeFragmentToMagnetometerFragment()
        findNavController().navigate(action)
    }

    fun moveToGpsFragment()
    {
        val action = HomeFragmentDirections.actionHomeFragmentToGpsFragment()
        findNavController().navigate(action)
    }

    fun moveToCompassFragment()
    {
        val action = HomeFragmentDirections.actionHomeFragmentToCompassFragment()
        findNavController().navigate(action)
    }

    fun moveToProximityFragment()
    {
        val action = HomeFragmentDirections.actionHomeFragmentToProximityFragment()
        findNavController().navigate(action)
    }
}