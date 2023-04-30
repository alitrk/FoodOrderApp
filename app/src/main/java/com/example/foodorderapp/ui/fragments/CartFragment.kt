package com.example.foodorderapp.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FragmentCartBinding
import com.example.foodorderapp.ui.adapters.FoodsCartAdapter
import com.example.foodorderapp.ui.viewmodel.CartViewModel
import com.example.foodorderapp.utils.Resource
import com.example.foodorderapp.utils.navigate
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private var tempList = listOf<FoodsCart>()
    private var totalPrice = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.cartFragment = this

        viewModel.cartList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarCart.isVisible = true
                }

                is Resource.Success -> {
                    val adapter = FoodsCartAdapter(requireContext(), resource.data, viewModel)
                    binding.apply {
                        foodsCartAdapter = adapter
                        progressBarCart.isVisible = false
                    }
                    totalPrice(resource.data)
                    binding.cartFragmentTotalPrice = totalPrice
                    tempList = resource.data
                }

                is Resource.Error -> {
                    viewModel.checkInternetConnection(requireContext())
                    if (tempList.size == 1 || viewModel.isInternetConnected.value == true) {
                        // show empty state
                        totalPrice = 0
                        binding.apply {
                            progressBarCart.isVisible = false
                            errorMessageCart.isVisible = false
                            retryBtnCart.isVisible = false
                            rvCart.isVisible = false
                        }

                    } else {
                        totalPrice = 0
                        binding.apply {
                            progressBarCart.isVisible = false
                            errorMessageCart.isVisible = true
                            retryBtnCart.isVisible = true
                        }
                    }
                }
            }
        }
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CartViewModel by viewModels()
        viewModel = tempViewModel

    }

    fun retryButtonOnClickCart() {
        viewModel.getShowCart()
        binding.apply {
            errorMessageCart.isVisible = false
            retryBtnCart.isVisible = false
        }
    }

    fun buttonOrderOnClick(view: View) {
        Navigation.navigate(view, R.id.action_cartFragment_to_orderFragment)
        saveData()
        for (i in tempList) {
            viewModel.delete(i.sepet_yemek_id, i.kullanici_adi)
        }
    }

    private fun totalPrice(list: List<FoodsCart>) {
        totalPrice = 0
        for (i in list) {
            totalPrice += (i.yemek_siparis_adet) * (i.yemek_fiyat)
        }
    }

    private fun saveData() {
        val sharedPreferences = requireContext().getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(tempList)
        editor.putString("cartOrderList", json)
        editor.apply()
    }

    fun closeOnClick(view: View) {
        Navigation.navigate(view, R.id.action_cartFragment_to_mainPageFragment)
    }


}