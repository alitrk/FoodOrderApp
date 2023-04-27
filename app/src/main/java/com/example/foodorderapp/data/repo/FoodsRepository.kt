package com.example.foodorderapp.data.repo

import com.example.foodorderapp.data.datasource.FoodsDataSource
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.utils.Resource
import kotlinx.coroutines.flow.Flow

class FoodsRepository(private var fds:FoodsDataSource){

    suspend fun showFoods() : Flow<Resource<List<Foods>>> = fds.showFoods()

    suspend fun addToCart(yemek_adi:String,
                          yemek_resim_adi:String,
                          yemek_fiyat:Int,
                          yemek_siparis_adet:Int,
                          kullanici_adi:String) = fds.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

    suspend fun showCart(kullanici_adi: String): Resource<List<FoodsCart>> = fds.showCart(kullanici_adi)

    suspend fun delete(sepet_yemek_id:Int, kullanici_adi: String) = fds.delete(sepet_yemek_id, kullanici_adi)
}