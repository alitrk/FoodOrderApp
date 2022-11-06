package com.example.foodorderapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.Foods
import com.example.foodorderapp.databinding.FoodCardDesignBinding
import com.example.foodorderapp.ui.fragments.MainPageFragmentDirections
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.navigate

class FoodsAdapter(var mContext: Context,
                   var foodsList: List<Foods>,
                   var viewModel: MainPageViewModel)
    : RecyclerView.Adapter<FoodsAdapter.CardViewHolder>() {

    inner class CardViewHolder(binding:FoodCardDesignBinding) : RecyclerView.ViewHolder(binding.root){
        var binding:FoodCardDesignBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding:FoodCardDesignBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.food_card_design ,parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val food = foodsList.get(position)
        val t = holder.binding
        t.foodObject = food

        showFoodImage(food.yemek_resim_adi,t)

        t.recyclerViewRowFood.setOnClickListener {
            val navigate = MainPageFragmentDirections.actionMainPageFragmentToFoodDetailsFragment(food = food)
            Navigation.navigate(it,navigate)
        }
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    private fun showFoodImage(foodImageName:String, binding: FoodCardDesignBinding){
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/$foodImageName"
        Glide.with(mContext).load(url).override(450,450).into(binding.imageViewFood)
    }

    fun setFilteredList(filteredList: List<Foods>){
        if (foodsList.isNotEmpty()){
            foodsList = filteredList
            notifyDataSetChanged()
        }
    }


}