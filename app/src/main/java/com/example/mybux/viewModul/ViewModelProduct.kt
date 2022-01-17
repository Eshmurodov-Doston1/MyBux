package com.example.mybux.viewModul

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.DatabaseHelper
import com.example.domain.database.products.Products
import com.example.domain.iteraktor.MyBuxIteraktor
import com.example.mybux.utils.MybuxResourse
import com.example.mybux.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelProduct @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val databaseHelper: DatabaseHelper,
    private val myBuxIteraktor: MyBuxIteraktor
):ViewModel() {
    private val TAG = "ViewModelProduct"
    fun getAllProduct(cuce:String):StateFlow<MybuxResourse>{
        var productList = MutableStateFlow<MybuxResourse>(MybuxResourse.Loading)
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()){
                myBuxIteraktor.getAllProducts(cuce).collect {
                    if (it.isSuccess){
                        var listProducts = ArrayList<Products>()
                        Log.e("Productlar", it.getOrNull()?.response.toString() )
                        it.getOrNull()?.response?.product_list?.forEach {
                            val products = Products(
                                it.bar_code,
                                it.cat_title,
                                it.classCode.toString(),
                                it.className,
                                it.commissionTin,
                                it.count,
                                it.discount,
                                it.group_id,
                                it.id,
                                it.label,
                                it.measure_id,
                                it.measure_name,
                                it.name,
                                it.photo.toString(),
                                it.price,
                                it.vat)
                            listProducts.add(products)
                        }
                        databaseHelper.insertProducts(listProducts)
                        productList.emit(MybuxResourse.SuccessGetProduct(databaseHelper.getAllProduct()))
                    }else{
                        productList.emit(MybuxResourse.Error(it.getOrNull()?.error?.error_msg.toString()))
                    }
                }
            }else{
                if (databaseHelper.getAllProductsMy().isNotEmpty()){
                    productList.emit(MybuxResourse.SuccessGetProduct(databaseHelper.getAllProduct()))
                }else{
                    productList.emit(MybuxResourse.Error("503"))
                }
            }
        }
        return productList
    }


    fun getDatabase():DatabaseHelper{
        return databaseHelper
    }
}