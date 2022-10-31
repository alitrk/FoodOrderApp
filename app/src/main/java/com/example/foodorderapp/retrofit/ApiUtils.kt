package com.example.foodorderapp.retrofit


class ApiUtils {
    companion object{
        var BASE_URL = "http://kasimadalan.pe.hu"

        fun getFoodsDao(): FoodsDao{
            return RetrofitClient.getClient(BASE_URL).create(FoodsDao::class.java)
        }
    }
}