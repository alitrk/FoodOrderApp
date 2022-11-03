package com.example.foodorderapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FragmentFoodDetailsBinding
import com.example.foodorderapp.ui.adapters.FoodsAdapter
import com.example.foodorderapp.ui.viewmodel.FoodDetailsViewModel
import com.example.foodorderapp.utils.navigate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.absoluteValue

@AndroidEntryPoint
class FoodDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFoodDetailsBinding
    private lateinit var viewModel: FoodDetailsViewModel
    private var foodsCartListDetails: List<FoodsCart> = listOf()
    private var orderNumber = 1

    private val testUserName = "alitrk"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_food_details, container, false)
        binding.foodDetailsFragment = this
        binding.foodDetailsToolbarTitle = "Details"
        val bundle:FoodDetailsFragmentArgs by navArgs()
        val receivedFood = bundle.food
        binding.foodObject = receivedFood
        binding.testUserName = testUserName

        binding.foodDetailsNumber = orderNumber

        showFoodImage(receivedFood.yemek_resim_adi)

        binding.totalPrice = totalPrice(orderNumber,receivedFood.yemek_fiyat)
        viewModel.foodsCartList.observe(viewLifecycleOwner){
            foodsCartListDetails = it
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FoodDetailsViewModel by viewModels()
        viewModel = tempViewModel

    }

    fun btnMinusOnClick(){
        if (orderNumber>1){
            orderNumber -= 1
            binding.foodDetailsNumber = orderNumber
            binding.totalPrice = totalPrice(orderNumber,binding.foodObject!!.yemek_fiyat)
        }else{
            Snackbar.make(requireView(), "This number cannot be less than 1", Snackbar.LENGTH_LONG).show()

        }

    }

    fun btnPlusOnClick(){
        orderNumber += 1
        binding.foodDetailsNumber = orderNumber
        binding.totalPrice = totalPrice(orderNumber,binding.foodObject!!.yemek_fiyat)
    }

    fun addToCard(yemek_adi:String,
                  yemek_resim_adi:String,
                  yemek_fiyat:Int,
                  yemek_siparis_adet:Int,
                  kullanici_adi:String){

        var tempSum = 0
        var sum = 0
        if (foodsCartListDetails.isNotEmpty()){
            for (i in foodsCartListDetails){
                if(i.yemek_adi == yemek_adi){
                    tempSum += i.yemek_siparis_adet
                }
            }
        }
        if (tempSum == 0){
            viewModel.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }else{
            for (i in foodsCartListDetails){
                if(i.yemek_adi == yemek_adi){
                    viewModel.delete(i.sepet_yemek_id, i.kullanici_adi)
                    sum += i.yemek_siparis_adet
                }
            }
            addToCardSimple(yemek_adi,yemek_resim_adi, yemek_fiyat, (sum+yemek_siparis_adet), kullanici_adi)
        }
        Snackbar.make(requireView(),"Added to cart successfully",Snackbar.LENGTH_LONG).show()
        Navigation.navigate(requireView(),R.id.action_foodDetailsFragment_to_mainPageFragment)



    }
    fun addToCardSimple(yemek_adi:String,
                        yemek_resim_adi:String,
                        yemek_fiyat:Int,
                        yemek_siparis_adet:Int,
                        kullanici_adi:String){
        viewModel.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
    }

    private fun showFoodImage(foodImageName:String){
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/$foodImageName"
        Glide.with(requireContext()).load(url).override(800,800).into(binding.imageViewFoodDetails)
    }

    private fun totalPrice(orderNumber:Int, foodPrice:Int): Int{
        return orderNumber*foodPrice
    }

    fun cartOnClickDetails(view:View){
        Navigation.navigate(view,R.id.action_foodDetailsFragment_to_cartFragment)
    }




}