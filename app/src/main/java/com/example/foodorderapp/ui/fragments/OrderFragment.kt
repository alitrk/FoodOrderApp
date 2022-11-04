package com.example.foodorderapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentOrderBinding
import com.example.foodorderapp.utils.navigate


class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order, container, false)
        binding.orderFragment = this

        return binding.root
    }

    fun buttonContinueOnClick(view: View){
        Navigation.navigate(view,R.id.action_orderFragment_to_mainPageFragment)
    }

}