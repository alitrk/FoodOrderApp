package com.example.foodorderapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
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

        t.imageViewDelete.setOnClickListener {
            Snackbar.make(it,"${foodCart.yemek_adi} silinsin mi?", Snackbar.LENGTH_LONG)
                .setAction("EVET"){
                    viewModel.delete(foodCart.sepet_yemek_id, foodCart.kullanici_adi)
                }.show()
        }
        t.buttonMinusRow.setOnClickListener {
            viewModel.delete(foodCart.sepet_yemek_id, foodCart.kullanici_adi)
            viewModel.addToCart(foodCart.yemek_adi, foodCart.yemek_resim_adi, foodCart.yemek_fiyat, (foodCart.yemek_siparis_adet - 1), foodCart.kullanici_adi)
        }

        t.buttonPlusRow.setOnClickListener {
            viewModel.delete(foodCart.sepet_yemek_id, foodCart.kullanici_adi)
            viewModel.addToCart(foodCart.yemek_adi, foodCart.yemek_resim_adi, foodCart.yemek_fiyat, (foodCart.yemek_siparis_adet + 1), foodCart.kullanici_adi)
        }
    }


    override fun getItemCount(): Int {
        return foodsList.size
    }
}