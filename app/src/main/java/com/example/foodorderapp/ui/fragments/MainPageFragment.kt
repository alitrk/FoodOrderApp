package com.example.foodorderapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentMainPageBinding
import com.example.foodorderapp.ui.adapters.FoodsAdapter
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page, container, false)
        binding.mainPageFragment = this
        binding.mainPageToolbarTitle = "Foods"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarMainPage)

        viewModel.foodsList.observe(viewLifecycleOwner){
            if (it!=null){
                val adapter = FoodsAdapter(requireContext(),it,viewModel)
                binding.foodsAdapter = adapter
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel:MainPageViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun fabOnClick(view:View){
        Navigation.navigate(view,R.id.action_mainPageFragment_to_cartFragment)
    }


}