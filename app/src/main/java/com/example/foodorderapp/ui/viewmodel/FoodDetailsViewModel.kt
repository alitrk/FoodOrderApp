package com.example.foodorderapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.data.repo.FoodsRepository
import com.example.foodorderapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(private var frepo: FoodsRepository): ViewModel() {

    private val _cartList = MutableLiveData<Resource<List<FoodsCart>>>()
    val cartList: LiveData<Resource<List<FoodsCart>>>
        get() = _cartList

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    init {
        val userName = auth.currentUser?.email.toString()
        showCart(userName)
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

    private fun showCart(kullanici_adi: String) {
        viewModelScope.launch {
            _cartList.postValue(frepo.showCart(kullanici_adi))
        }
    }

    fun delete(sepet_yemek_id:Int, kullanici_adi: String){
        viewModelScope.launch{
            frepo.delete(sepet_yemek_id, kullanici_adi)
            showCart(kullanici_adi)
        }
    }

}