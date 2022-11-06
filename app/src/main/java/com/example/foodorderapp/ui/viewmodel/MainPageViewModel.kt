package com.example.foodorderapp.ui.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.data.repo.FoodsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(var frepo: FoodsRepository): ViewModel() {

    var foodsList = MutableLiveData<List<Foods>>()
    var cartList = MutableLiveData<List<FoodsCart>>()
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var userName: String = auth.currentUser?.email.toString()

    init {
        showFoods()
        showCart(userName)
    }

    fun showFoods(){
        CoroutineScope(Dispatchers.Main).launch {
            if (frepo.showFoods().isNotEmpty()){
                foodsList.value = frepo.showFoods()
            }
        }
    }

    fun showCart(kullanici_adi:String) {
        CoroutineScope(Dispatchers.Main).launch {
            if(frepo.showFoods().isNotEmpty()){
                cartList.value = frepo.showCart(kullanici_adi)
            }
        }
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




}