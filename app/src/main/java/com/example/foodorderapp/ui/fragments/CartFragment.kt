package com.example.foodorderapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentCartBinding
import com.example.foodorderapp.ui.adapters.FoodsAdapter
import com.example.foodorderapp.ui.adapters.FoodsCartAdapter
import com.example.foodorderapp.ui.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false)
        binding.cartFragment = this
        binding.cartFragmentToolbarTitle = "My Cart"

        viewModel.cartList.observe(viewLifecycleOwner){
            if (it!=null){
                val adapter = FoodsCartAdapter(requireContext(),it,viewModel)
                binding.foodsCartAdapter = adapter
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel:CartViewModel by viewModels()
        viewModel = tempViewModel

    }

}