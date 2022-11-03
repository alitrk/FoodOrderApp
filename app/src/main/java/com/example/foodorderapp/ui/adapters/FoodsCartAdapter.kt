package com.example.foodorderapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entity.FoodsCart
import com.example.foodorderapp.databinding.FoodsCartCardDesignBinding
import com.example.foodorderapp.ui.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar

class FoodsCartAdapter (var mContext: Context,
                        var foodsList: List<FoodsCart>,
                        var viewModel: CartViewModel)
: RecyclerView.Adapter<FoodsCartAdapter.CardCartViewHolder>() {

    inner class CardCartViewHolder(binding: FoodsCartCardDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: FoodsCartCardDesignBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCartViewHolder {
        val binding: FoodsCartCardDesignBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.foods_cart_card_design, parent, false
        )
        return CardCartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardCartViewHolder, position: Int) {

        val foodCart = foodsList.get(position)
        val t = holder.binding
        t.foodCartObject = foodCart

        var totalPrice = totalPriceCartRow(foodCart)
        t.cartRowTotalPrice = totalPrice
        showFoodImage(foodCart.yemek_resim_adi,t)


        t.imageViewDelete.setOnClickListener {
            Snackbar.make(it,"Are you sure you want to delete ${foodCart.yemek_adi}?", Snackbar.LENGTH_LONG)
                .setAction("Yes"){
                    viewModel.delete(foodCart.sepet_yemek_id, foodCart.kullanici_adi)
                }.show()
        }
        t.buttonMinusRow.setOnClickListener {
            if (foodCart.yemek_siparis_adet>1){
                viewModel.delete(foodCart.sepet_yemek_id, foodCart.kullanici_adi)
                viewModel.addToCart(foodCart.yemek_adi, foodCart.yemek_resim_adi, foodCart.yemek_fiyat, (foodCart.yemek_siparis_adet - 1), foodCart.kullanici_adi)
                totalPrice = totalPriceCartRow(foodCart)
                t.cartRowTotalPrice = totalPrice
            }else if(foodCart.yemek_siparis_adet==1) {
                Snackbar.make(it,"Are you sure you want to delete ${foodCart.yemek_adi}?", Snackbar.LENGTH_LONG)
                    .setAction("Yes"){
                        viewModel.delete(foodCart.sepet_yemek_id, foodCart.kullanici_adi)
                    }.show()
            }else{
                Snackbar.make(it,"This number cannot be less than 1", Snackbar.LENGTH_LONG).show()
            }
        }

        t.buttonPlusRow.setOnClickListener {
            viewModel.delete(foodCart.sepet_yemek_id, foodCart.kullanici_adi)
            viewModel.addToCart(foodCart.yemek_adi, foodCart.yemek_resim_adi, foodCart.yemek_fiyat, (foodCart.yemek_siparis_adet + 1), foodCart.kullanici_adi)
            totalPrice = totalPriceCartRow(foodCart)
            t.cartRowTotalPrice = totalPrice
        }
    }


    override fun getItemCount(): Int {
        return foodsList.size
    }

    private fun showFoodImage(foodImageName:String, binding: FoodsCartCardDesignBinding){
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/$foodImageName"
        Glide.with(mContext).load(url).override(108,108).into(binding.imageViewFoodCartCard)
    }


    private fun totalPriceCartRow(foodCartObject: FoodsCart): Int{
        return (foodCartObject.yemek_fiyat) * (foodCartObject.yemek_siparis_adet)
    }

}