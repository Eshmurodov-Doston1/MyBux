package com.example.mybux.adapters.fmAdpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.helpers.InfoModel
import com.example.mybux.databinding.ItemFmBinding
import com.example.mybux.theme.MyAppTheme
import org.apache.commons.lang3.builder.Diff

class FmAdapter(var myAppTheme: MyAppTheme,var context: Context):ListAdapter<InfoModel,FmAdapter.Vh>(MyDiffUtill()) {
    inner class Vh(var itemFmBinding: ItemFmBinding):RecyclerView.ViewHolder(itemFmBinding.root){
        fun onBinf(infoModel: InfoModel){
            itemFmBinding.textFm.text = "${infoModel.name}:${infoModel.value}"
            itemFmBinding.textFm.setTextColor(myAppTheme.textColor(context))
        }
    }

    class MyDiffUtill:DiffUtil.ItemCallback<InfoModel>(){
        override fun areItemsTheSame(oldItem: InfoModel, newItem: InfoModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: InfoModel, newItem: InfoModel): Boolean {
            return oldItem.equals(newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemFmBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBinf(getItem(position))
    }
}