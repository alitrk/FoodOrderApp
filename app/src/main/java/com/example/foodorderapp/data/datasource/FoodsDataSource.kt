package com.example.foodorderapp.data.datasource

import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.retrofit.FoodsDao
import com.example.foodorderapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class FoodsDataSource (private var fdao: FoodsDao){
    /*suspend fun showFoods(): List<Foods> =
        withContext(Dispatchers.IO){
            try {
                fdao.showFoods().yemekler
            }catch (e: Exception){
                emptyList()
            }
        }*/

    suspend fun showFoods():Flow<Resource<List<Foods>>> {
        return flow {
            emit(Resource.Loading)
            try {
                val foods = fdao.showFoods().yemekler
                emit(Resource.Success(foods))
            }catch (e: IOException) {
                emit(Resource.Error("Network error: ${e.localizedMessage}"))
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "Unauthorized"
                    404 -> "User not found"
                    else -> "Unknown error"
                }
                emit(Resource.Error("HTTP error (${e.code()}): $errorMessage"))
            } catch (e: Exception) {
                emit(Resource.Error("Unknown error: ${e.localizedMessage}"))
            }
        }
    }



    suspend fun addToCart(yemek_adi:String,
                          yemek_resim_adi:String,
                          yemek_fiyat:Int,
                          yemek_siparis_adet:Int,
                          kullanici_adi:String) = fdao.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

    suspend fun showCart(kullanici_adi: String): Resource<List<FoodsCart>> {
        return try {
            val response = fdao.showCart(kullanici_adi)
            if (response.isSuccessful) {
                response.body()?.let {foodsCart->
                    return@let Resource.success(foodsCart.sepet_yemekler.sortedBy { it.yemek_adi })
                } ?: Resource.error("Error")
            } else {
                Resource.error("Error")
            }
        } catch (e: Exception) {
            Resource.error("No data!")
        }
    }

    suspend fun delete(sepet_yemek_id:Int, kullanici_adi: String) = fdao.delete(sepet_yemek_id, kullanici_adi)

}