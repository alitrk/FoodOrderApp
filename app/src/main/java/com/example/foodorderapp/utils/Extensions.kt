package com.example.foodorderapp.utils

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

fun Navigation.navigate(v: View, id:Int){
    findNavController(v).navigate(id)
}

fun Navigation.navigate(v: View, id: NavDirections){
    findNavController(v).navigate(id)
}
