package com.example.foodorderapp.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FragmentMainPageBinding
import com.example.foodorderapp.databinding.TitlebarBinding
import com.example.foodorderapp.ui.adapters.FoodsAdapter
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.navigate
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type
import kotlin.random.Random

@AndroidEntryPoint
class MainPageFragment : Fragment(),SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var titleBarBinding: TitlebarBinding
    private lateinit var searchView: SearchView
    private lateinit var adapter:FoodsAdapter
    private var foodOrderList = listOf<FoodsCart>()
    private var foodCartSet= mutableSetOf<FoodsCart>()
    private var foodSet = mutableSetOf<Foods>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page, container, false)
        binding.mainPageFragment = this
        titleBarBinding = binding.include
        binding.rv.layoutManager = GridLayoutManager(context,2)
        searchView = titleBarBinding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(this@MainPageFragment)


        viewModel.foodsList.observe(viewLifecycleOwner){
            if (it!=null){
                adapter = FoodsAdapter(requireContext(),it,viewModel)
                binding.foodsAdapter = adapter
                foodSet.addAll(it)
            }
        }

        viewModel.cartList.observe(viewLifecycleOwner){
            foodCartSet.addAll(it)
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

    fun fabOnClick(view:View){
        Navigation.navigate(view,R.id.action_mainPageFragment_to_accountFragment)
    }

    fun surpriseMeOnClick(view: View){
        val foodsList = viewModel.foodsList.value!!
        val random =  Random.nextInt(0, foodsList.size)
        val food = foodsList.get(random)
        val navigate =  MainPageFragmentDirections.actionMainPageFragmentToFoodDetailsFragment(food = food)
        Navigation.navigate(view,navigate)
    }

    fun repeatLastOnClick(view: View){


        if (foodCartSet.isEmpty()){
            val sharedPreferences = requireContext().getSharedPreferences("shared preferences", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("cartOrderList", null)
            val type: Type = object : TypeToken<ArrayList<FoodsCart?>?>() {}.type

            foodOrderList = gson.fromJson<Any>(json, type) as ArrayList<FoodsCart>

            for (i in foodOrderList){
                viewModel.addToCart(i.yemek_adi,i.yemek_resim_adi,i.yemek_fiyat,i.yemek_siparis_adet,i.kullanici_adi)
            }
            Navigation.navigate(view,R.id.action_mainPageFragment_to_cartFragment)
            Snackbar.make(requireView(),"Last order repeated!",Snackbar.LENGTH_LONG).show()
        }else{
            Snackbar.make(requireView(),"The cart must be empty!",Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        filterList(newText)
        return true
    }

    private fun filterList(newText: String) {
        var filteredList = arrayListOf<Foods>()
        for (i in foodSet){
            if (i.yemek_adi.lowercase().contains(newText.lowercase())){
                filteredList.add(i)
            }
        }
        if (filteredList.isEmpty()){
            adapter.setFilteredList(filteredList)
        }else{
            adapter.setFilteredList(filteredList)

        }
    }


}