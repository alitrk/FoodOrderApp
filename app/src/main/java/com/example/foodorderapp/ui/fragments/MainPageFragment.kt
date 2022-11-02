package com.example.foodorderapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FragmentMainPageBinding
import com.example.foodorderapp.databinding.TitlebarBinding
import com.example.foodorderapp.ui.adapters.FoodsAdapter
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.navigate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import kotlin.random.Random

@AndroidEntryPoint
class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var titleBarBinding: TitlebarBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page, container, false)
        binding.mainPageFragment = this
        titleBarBinding = binding.include
        binding.mainPageToolbarTitle = "Foods"
        binding.rv.layoutManager = GridLayoutManager(context,2)

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

    fun cartOnClick(view:View){
        Navigation.navigate(view,R.id.action_mainPageFragment_to_cartFragment)
    }

    fun surpriseMeOnClick(view: View){
        val foodsList = viewModel.foodsList.value!!
        val random =  Random.nextInt(0, foodsList.size)
        val food = foodsList.get(random)
        val navigate =  MainPageFragmentDirections.actionMainPageFragmentToFoodDetailsFragment(food = food)
        Navigation.navigate(view,navigate)
    }








}