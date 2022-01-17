package com.example.mybux.adapters.basketAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.animation.content.Content
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.domain.database.basket.Basket
import com.example.mybux.R
import com.example.mybux.databinding.RowItemBasketBinding
import com.example.mybux.theme.MyAppTheme
import com.squareup.picasso.Picasso
import java.lang.String
import java.math.BigDecimal
import java.math.BigInteger

class BasketAdapter(var myAppTheme: MyAppTheme,var context:Context,var onItemClickListener: OnItemClickListener):ListAdapter<Basket,BasketAdapter.Vh>(MyDiffUtil()) {
    inner class Vh(var rowItemBasketBinding:RowItemBasketBinding):RecyclerView.ViewHolder(rowItemBasketBinding.root){
        @SuppressLint("SetTextI18n")
        fun onBind(basket: Basket, position: Int){
            if (basket.photo!="null"){
                Picasso.get().load(basket.photo).into(rowItemBasketBinding.productIcon)
            }else{
                val generator = ColorGenerator.MATERIAL
                val color2 = generator.getColor(basket.name)
                val drawable = TextDrawable.builder().buildRect(String.valueOf(basket.name?.get(0)), color2)
                rowItemBasketBinding.productIcon.setImageDrawable(drawable)
            }
            val price = BigInteger.valueOf(basket.price?.toLong() ?: 0).divide(BigInteger.valueOf(100))
            val allPrice: BigDecimal = BigDecimal(price.multiply(basket.count))
            rowItemBasketBinding.productName.text= basket.name
            rowItemBasketBinding.productName.setTextColor(myAppTheme.textColor(context))
            rowItemBasketBinding.productPrice.text = Html.fromHtml("<b>" + com.example.domain.helpers.numberFromat.NumberFormats.parseNumberFormat(allPrice.toDouble()).toString() + " " + context.getString(R.string.price) + "</b> • (" + com.example.domain.helpers.numberFromat.NumberFormats.parseNumberFormat(price.toDouble()).toString() + " " + context.getString(R.string.price) + ") × " + basket.count.toString() + " " + "/" + basket.measure_name,
                    Html.FROM_HTML_MODE_COMPACT)
            rowItemBasketBinding.productPrice.setTextColor(myAppTheme.textColor(context))
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(basket,position)
            }
        }
    }

    class MyDiffUtil:DiffUtil.ItemCallback<Basket>(){
        override fun areItemsTheSame(oldItem: Basket, newItem: Basket): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Basket, newItem: Basket): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RowItemBasketBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }

    interface OnItemClickListener{
        fun onItemClick(basket: Basket,position: Int)
    }
}