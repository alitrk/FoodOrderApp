package com.example.foodorderapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FragmentFoodDetailsBinding
import com.example.foodorderapp.ui.viewmodel.FoodDetailsViewModel
import com.example.foodorderapp.utils.Resource
import com.example.foodorderapp.utils.navigate
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFoodDetailsBinding
    private lateinit var viewModel: FoodDetailsViewModel
    private lateinit var auth: FirebaseAuth
    private var foodsCartListDetails: List<FoodsCart> = listOf()
    private var orderNumber = 1
    private var cartSizeFood = ""
    private var foodCartSet = mutableSetOf<FoodsCart>()
    private lateinit var userName: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_details, container, false)
        binding.foodDetailsFragment = this
        binding.foodDetailsToolbarTitle = "Details"
        val bundle: FoodDetailsFragmentArgs by navArgs()
        val receivedFood = bundle.food
        binding.foodObject = receivedFood
        auth = FirebaseAuth.getInstance()
        userName = auth.currentUser?.email.toString()
        binding.userName = userName
        binding.foodDetailsNumber = orderNumber
        showFoodImage(receivedFood.yemek_resim_adi)

        binding.totalPrice = totalPrice(orderNumber, receivedFood.yemek_fiyat)

        observeCartList()


        return binding.root
    }

    private fun cartSizeFun(foodCartSet: MutableSet<FoodsCart>) {
        cartSizeFood = foodCartSet.size.toString()
        binding.cartTextDesign = cartSizeFood
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FoodDetailsViewModel by viewModels()
        viewModel = tempViewModel

    }

    private fun observeCartList() {
        lifecycleScope.launch {
            viewModel.cartList.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        //binding.progressBar.isVisible = false
                    }

                    is Resource.Success -> {
                        binding.apply {
                            //progressBar.isVisible = false
                        }
                        foodsCartListDetails = resource.data
                        foodCartSet.addAll(resource.data)
                        cartSizeFun(foodCartSet)
                    }

                    is Resource.Error -> {
                        binding.apply {
                            //progressBar.isVisible = false
                            //errorMessage.isVisible = true
                            //retryBtn.isVisible = true
                        }
                    }
                }
            }
        }
    }

    fun btnMinusOnClick() {
        if (orderNumber > 1) {
            orderNumber -= 1
            binding.foodDetailsNumber = orderNumber
            binding.totalPrice = totalPrice(orderNumber, binding.foodObject!!.yemek_fiyat)
        } else {
            Snackbar.make(requireView(), "This number cannot be less than 1", Snackbar.LENGTH_LONG).show()
        }
    }

    fun btnPlusOnClick() {
        orderNumber += 1
        binding.foodDetailsNumber = orderNumber
        binding.totalPrice = totalPrice(orderNumber, binding.foodObject!!.yemek_fiyat)
    }

    fun addToCart(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        // check if internet connection available
        viewModel.checkInternetConnection(requireContext())

        if (viewModel.isInternetConnected.value == true) {
            // initialize temporary and total sum variables to keep track of item quantity
            var tempSum = 0
            val totalSum: Int

            // check if cart is already populated with any items
            if (foodsCartListDetails.isNotEmpty()) {
                // if cart has items, iterate through them and check if the item already exists
                for (food in foodsCartListDetails) {
                    if (food.yemek_adi == yemek_adi) {
                        // if the item already exists, add its quantity to the temporary sum
                        tempSum += food.yemek_siparis_adet
                    }
                }
            }

            // if the item is not already in the cart
            if (tempSum == 0) {
                // add it to the cart with the specified quantity
                viewModel.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
            } else {
                // if the item already exists, delete all instances of it from the cart and
                // add it back with the total quantity
                totalSum = deleteIfExists(yemek_adi, kullanici_adi) + tempSum
                addToCartSimple(yemek_adi, yemek_resim_adi, yemek_fiyat, totalSum, kullanici_adi)
            }

            // display a message to the user that the item was added to the cart
            Snackbar.make(requireView(), "Added to cart successfully", Snackbar.LENGTH_LONG).show()

            // navigate back to the main page fragment
            Navigation.navigate(requireView(), R.id.action_foodDetailsFragment_to_mainPageFragment)
        } else {
            Snackbar.make(requireView(), "Check your internet connection and try again later.", Snackbar.LENGTH_LONG)
                .show()
        }

    }

    private fun deleteIfExists(yemek_adi: String, kullanici_adi: String): Int {
        var sum = 0
        for (i in foodsCartListDetails) {
            if (i.yemek_adi == yemek_adi) {
                viewModel.delete(i.sepet_yemek_id, kullanici_adi)
                sum += i.yemek_siparis_adet
            }
        }
        return sum
    }

    fun addToCartSimple(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        viewModel.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
    }

    private fun showFoodImage(foodImageName: String) {
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/$foodImageName"
        Glide.with(requireContext()).load(url).override(800, 800).into(binding.imageViewFoodDetails)
    }

    private fun totalPrice(orderNumber: Int, foodPrice: Int): Int {
        return orderNumber * foodPrice
    }

    fun cartOnClickDetails(view: View) {
        Navigation.navigate(view, R.id.action_foodDetailsFragment_to_cartFragment)
    }

}