package com.example.foodorderapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.data.repo.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(var frepo: FoodsRepository): ViewModel() {

    var foodsCartList = MutableLiveData<List<FoodsCart>>()
    init {
        showCart("alitrk")
    }
    fun addToCart(yemek_adi:String,
                  yemek_resim_adi:String,
                  yemek_fiyat:Int,
                  yemek_siparis_adet:Int,
                  kullanici_adi:String){
        CoroutineScope(Dispatchers.Main).launch {
            frepo.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }
    fun showCart(kullanici_adi:String){
        CoroutineScope(Dispatchers.Main).launch {
            foodsCartList.value = frepo.showCart(kullanici_adi)
        }
    }

    fun delete(sepet_yemek_id:Int, kullanici_adi: String){
        CoroutineScope(Dispatchers.Main).launch{
            frepo.delete(sepet_yemek_id, kullanici_adi)

            showCart(kullanici_adi)
        }
    }

}