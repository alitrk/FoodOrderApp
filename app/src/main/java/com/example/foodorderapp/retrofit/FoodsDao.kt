package com.example.foodorderapp.retrofit

import com.example.foodorderapp.data.entity.FoodsCartResponse
import com.example.foodorderapp.data.entity.FoodsResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodsDao {

    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun showFoods(): FoodsResponse

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun addToCart(
        @Field("yemek_adi") yemek_adi: String,
        @Field("yemek_resim_adi") yemek_resim_adi: String,
        @Field("yemek_fiyat") yemek_fiyat: Int,
        @Field("yemek_siparis_adet") yemek_siparis_adet: Int,
        @Field("kullanici_adi") kullanici_adi: String
    )

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun showCart(
        @Field("kullanici_adi") kullanici_adi: String
    ): Response<FoodsCartResponse>


    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun delete(
        @Field("sepet_yemek_id") sepet_yemek_id: Int,
        @Field("kullanici_adi") kullanici_adi: String
    )

}