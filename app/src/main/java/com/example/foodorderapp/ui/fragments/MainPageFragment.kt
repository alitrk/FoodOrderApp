package com.example.foodorderapp.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FragmentMainPageBinding
import com.example.foodorderapp.databinding.TitlebarBinding
import com.example.foodorderapp.ui.adapters.FoodsAdapter
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.Resource
import com.example.foodorderapp.utils.navigate
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Type

@AndroidEntryPoint
class MainPageFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var titleBarBinding: TitlebarBinding
    private lateinit var searchView: SearchView
    private lateinit var adapter: FoodsAdapter
    private var cartSize = ""
    private var foodOrderList = listOf<FoodsCart>()
    private var foodCartSet = mutableSetOf<FoodsCart>()
    private var foodSet = mutableSetOf<Foods>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_page, container, false)
        binding.mainPageFragment = this
        titleBarBinding = binding.include
        searchView = titleBarBinding.searchView
        searchView.setOnQueryTextListener(this@MainPageFragment)

        observeFoodList()
        //observeCartList()


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MainPageViewModel by viewModels()
        viewModel = tempViewModel
    }

    private fun observeFoodList() {
        lifecycleScope.launch {
            viewModel.foodsList.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Show loading indicator
                        binding.progressBar.isVisible = true
                    }

                    is Resource.Success -> {
                        // Update UI with food data
                        adapter = FoodsAdapter(requireContext(), resource.data)
                        binding.apply {
                            foodsAdapter = adapter
                            progressBar.isVisible = false
                        }
                        foodSet.addAll(resource.data)
                    }

                    is Resource.Error -> {
                        // Show error message
                        binding.apply {
                            progressBar.isVisible = false
                            errorMessage.isVisible = true
                            retryBtn.isVisible = true
                        }
                    }
                }
            }
        }
    }

    private fun observeCartList() {
        lifecycleScope.launch {
            viewModel.cartList.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = false
                    }

                    is Resource.Success -> {
                        binding.apply {
                            progressBar.isVisible = false
                        }
                        foodCartSet.addAll(resource.data)
                        cartSizeFun(foodCartSet)
                    }

                    is Resource.Error -> {
                        binding.apply {
                            progressBar.isVisible = false
                            errorMessage.isVisible = true
                            retryBtn.isVisible = true
                        }
                    }
                }
            }
        }
    }

    fun cartOnClick(view: View) {
        Navigation.navigate(view, R.id.action_mainPageFragment_to_cartFragment)
    }

    fun fabOnClick(view: View) {
        Navigation.navigate(view, R.id.action_mainPageFragment_to_accountFragment)
    }

    fun surpriseMeOnClick(view: View) {
        val food = foodSet.random()
        val navigate = MainPageFragmentDirections.actionMainPageFragmentToFoodDetailsFragment(food = food)
        Navigation.navigate(view, navigate)
    }

    fun repeatLastOnClick(view: View) {
        if (foodCartSet.isEmpty()) {
            val sharedPreferences = requireContext().getSharedPreferences("shared preferences", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("cartOrderList", null)
            if (json.isNullOrEmpty()) {
                Snackbar.make(requireView(), "Last order not found", Snackbar.LENGTH_LONG).show()
            } else {
                val type: Type = object : TypeToken<ArrayList<FoodsCart?>?>() {}.type

                foodOrderList = gson.fromJson<Any>(json, type) as ArrayList<FoodsCart>

                for (i in foodOrderList) {
                    viewModel.addToCart(
                        i.yemek_adi,
                        i.yemek_resim_adi,
                        i.yemek_fiyat,
                        i.yemek_siparis_adet,
                        i.kullanici_adi
                    )
                }
                Navigation.navigate(view, R.id.action_mainPageFragment_to_cartFragment)
                Snackbar.make(requireView(), "Last order repeated!", Snackbar.LENGTH_LONG).show()
            }

        } else {
            Snackbar.make(requireView(), "The cart must be empty!", Snackbar.LENGTH_LONG).show()
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
        val filteredList = arrayListOf<Foods>()
        for (i in foodSet) {
            if (i.yemek_adi.lowercase().contains(newText.lowercase())) {
                filteredList.add(i)
            }
        }
        if (filteredList.isNotEmpty()) {
            adapter.setFilteredList(filteredList)
        } else {
            adapter.setFilteredList(filteredList)
        }
    }

    private fun cartSizeFun(foodCartSet: MutableSet<FoodsCart>) {
        cartSize = foodCartSet.size.toString()
        binding.cartText = cartSize
    }


}