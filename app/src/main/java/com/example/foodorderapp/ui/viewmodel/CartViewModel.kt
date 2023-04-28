package com.example.foodorderapp.ui.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.data.repo.FoodsRepository
import com.example.foodorderapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private var frepo: FoodsRepository) : ViewModel() {

    private val _cartList = MutableLiveData<Resource<List<FoodsCart>>>()
    val cartList: LiveData<Resource<List<FoodsCart>>>
        get() = _cartList

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userName: String = auth.currentUser?.email.toString()

    init {
        getShowCart()
    }

    fun getShowCart() {
        showCart(userName)
    }

    private fun showCart(kullanici_adi: String) {
        viewModelScope.launch {
            _cartList.postValue(frepo.showCart(kullanici_adi))
        }
    }

    fun delete(sepet_yemek_id: Int, kullanici_adi: String) {
        viewModelScope.launch {
            frepo.delete(sepet_yemek_id, kullanici_adi)
            showCart(kullanici_adi)
        }
    }

    fun addToCart(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        viewModelScope.launch {
            frepo.addToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}