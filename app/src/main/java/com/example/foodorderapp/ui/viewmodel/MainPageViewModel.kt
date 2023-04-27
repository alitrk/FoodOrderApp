package com.example.foodorderapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.data.repo.FoodsRepository
import com.example.foodorderapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(private var frepo: FoodsRepository) : ViewModel() {

    private val _foodsList = MutableStateFlow<Resource<List<Foods>>>(Resource.Loading)
    val foodsList: StateFlow<Resource<List<Foods>>> = _foodsList

    private val _cartList = MutableLiveData<Resource<List<FoodsCart>>>()
    val cartList: LiveData<Resource<List<FoodsCart>>>
        get() = _cartList

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userName: String = auth.currentUser?.email.toString()

    init {
        showFoods()
        showCart(userName)
    }

    private fun showFoods() {
        viewModelScope.launch {
            frepo.showFoods().stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)
                .collect { result ->
                    _foodsList.value = result
                }
        }
    }


    private fun showCart(kullanici_adi: String) {
        viewModelScope.launch {
            _cartList.postValue(frepo.showCart(kullanici_adi))
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


}