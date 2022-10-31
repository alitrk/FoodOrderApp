package com.example.foodorderapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FragmentFoodDetailsBinding
import com.example.foodorderapp.ui.adapters.FoodsAdapter
import com.example.foodorderapp.ui.viewmodel.FoodDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.absoluteValue

@AndroidEntryPoint
class FoodDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFoodDetailsBinding
    private lateinit var viewModel: FoodDetailsViewModel
    private var foodsCartListDetails: List<FoodsCart> = listOf()
    private var orderNumber = 1
        set(value) {
            field = value
            binding.foodDetailsNumber = value
        }
    private val testUserName = "alitrk"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_food_details, container, false)
        binding.foodDetailsFragment = this
        binding.foodDetailsToolbarTitle = "Details"
        orderNumber = 1
        val bundle:FoodDetailsFragmentArgs by navArgs()
        val receivedFood = bundle.food
        binding.foodObject = receivedFood
        binding.testUserName = testUserName

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
        }else{
            getActivity()?.let {
                Snackbar.make(
                    it.findViewById(android.R.id.content),
                    "This number cannot be less than 1", Snackbar.LENGTH_LONG).show()
            };
        }

    }

    fun btnPlusOnClick(){
        orderNumber += 1
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

    }
    fun addToCardSimple(yemek_adi:String,
                        yemek_resim_adi:String,
                        yemek_fiyat:Int,
                        yemek_siparis_adet:Int,
                        kullanici_adi:String){
        viewModel.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
    }

}