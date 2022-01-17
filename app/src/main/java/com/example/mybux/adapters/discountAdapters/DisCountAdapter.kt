package com.example.mybux.adapters.discountAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.database.discount.DiscountEntity
import com.example.mybux.databinding.ItemDiscountBinding
import com.example.mybux.theme.MyAppTheme

class DisCountAdapter(var context: Context,var myAppTheme: MyAppTheme,var onItemClickListener: OnItemClickListener):ListAdapter<DiscountEntity,DisCountAdapter.Vh>(MyDiffUtill()){
    inner class Vh(var itemDiscountBinding: ItemDiscountBinding):RecyclerView.ViewHolder(itemDiscountBinding.root){
        fun onBind(discountEntity: DiscountEntity,position: Int){
            itemDiscountBinding.percent.text = discountEntity.persent.toString()
            itemDiscountBinding.percent.setTextColor(myAppTheme.textColor(context))
            itemView.setOnLongClickListener {
                onItemClickListener.onItemLongClick(discountEntity,position,itemDiscountBinding)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemDiscountBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }


    class MyDiffUtill:DiffUtil.ItemCallback<DiscountEntity>(){
        override fun areItemsTheSame(oldItem: DiscountEntity, newItem: DiscountEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DiscountEntity, newItem: DiscountEntity): Boolean {
            return oldItem.id == newItem.id
        }

    }

    interface OnItemClickListener{
        fun onItemLongClick(discountEntity: DiscountEntity,position: Int,itemDiscountBinding: ItemDiscountBinding)
    }
}