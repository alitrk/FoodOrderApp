package com.example.foodorderapp.data.datasource

import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.retrofit.FoodsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.EOFException

class FoodsDataSource (var fdao: FoodsDao){
    suspend fun showFoods(): List<Foods> =
        withContext(Dispatchers.IO){
            try {
                fdao.showFoods().yemekler
            }catch (e: Exception){
                emptyList()
            }
        }

    suspend fun addToCart(yemek_adi:String,
                          yemek_resim_adi:String,
                          yemek_fiyat:Int,
                          yemek_siparis_adet:Int,
                          kullanici_adi:String) = fdao.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

    suspend fun showCart(kullanici_adi:String): List<FoodsCart> =
        withContext(Dispatchers.IO){
            try {
                fdao.showCart(kullanici_adi).sepet_yemekler
            }catch (e: EOFException){
                //Log.d("Exception","Cart is empty!")
                emptyList()
            }
        }

    suspend fun delete(sepet_yemek_id:Int, kullanici_adi: String) = fdao.delete(sepet_yemek_id, kullanici_adi)

}