package com.example.mybux.adapters.mainAdapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.domain.database.products.Products
import com.example.mybux.R
import com.example.mybux.databinding.RowItemBinding
import com.example.mybux.theme.MyAppTheme
import com.realpacific.clickshrinkeffect.ClickShrinkEffect
import com.realpacific.clickshrinkeffect.applyClickShrink
import com.squareup.picasso.Picasso
import java.lang.String
import java.math.BigInteger

class MainListAdapter(var context:Context,var myAppTheme: MyAppTheme,var onItemClickListener: OnItemClickListener):ListAdapter<Products, MainListAdapter.Vh>(
    MyDiffUtil()
) {
    inner class Vh(var rowItemBinding:RowItemBinding):RecyclerView.ViewHolder(rowItemBinding.root){
        fun onBind(products: Products, position: Int){
            if (products.photo!="null"){
                Picasso.get().load(products.photo).into(rowItemBinding.productIcon)
            }else{
                val generator = ColorGenerator.MATERIAL
                val color2 = generator.getColor(products.name)
                val drawable = TextDrawable.builder().buildRect(String.valueOf(products.name?.get(0)), color2)
                rowItemBinding.productIcon.setImageDrawable(drawable)
            }
            val price = BigInteger.valueOf(products.price?.toLong() ?: 0)
            rowItemBinding.productPrice.text = "${price.divide(BigInteger.valueOf(100))} ${context.getString(R.string.price)}"
            rowItemBinding.productMeasure.text = "/${products.measure_name}"
            rowItemBinding.productName.text = products.name
            rowItemBinding.productName.setTextColor(myAppTheme.textColor(context))
            rowItemBinding.productMeasure.setTextColor(myAppTheme.textColor(context))
            rowItemBinding.productPrice.setTextColor(myAppTheme.textColor(context))
            rowItemBinding.productItem.setBackgroundColor(myAppTheme.ActivityBackgroundColor(context))
            rowItemBinding.productItem.setBackgroundColor(Color.TRANSPARENT)
//            rowItemBinding.productItem.applyClickShrink()
//            ClickShrinkEffect(rowItemBinding.productItem)
            itemView.setOnClickListener {
                onItemClickListener.OnItemClick(products,position)
            }
        }
    }

    class MyDiffUtil:DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }

    interface OnItemClickListener{
        fun OnItemClick(products: Products,position: Int)
    }

    fun filter(listProduct:ArrayList<Products>){
        submitList(listProduct)
        notifyDataSetChanged()
    }
}