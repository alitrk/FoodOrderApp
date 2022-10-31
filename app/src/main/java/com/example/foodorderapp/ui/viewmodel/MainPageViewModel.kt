package com.example.foodorderapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.data.repo.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(var frepo: FoodsRepository): ViewModel() {

    var foodsList = MutableLiveData<List<Foods>>()

    init {
        showFoods()
    }

    fun showFoods(){
        CoroutineScope(Dispatchers.Main).launch {
            foodsList.value = frepo.showFoods()
        }
    }
}