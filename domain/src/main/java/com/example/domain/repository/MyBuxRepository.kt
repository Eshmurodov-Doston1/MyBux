package com.example.domain.repository

import com.example.domain.models.auth.reqAuth.ReqAuth
import com.example.domain.models.auth.resAuth.Login
import com.example.domain.models.getProductList.getProducts.Products
import com.example.domain.models.logOut.successLogOut.LogOut
import com.example.domain.models.settingsAuth.res.ResponseSettings
import com.example.domain.models.sig.Sig
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MyBuxRepository {
    fun getSig():Flow<Response<Sig>>
    fun getAuthRes(reqAuth: ReqAuth):Flow<Response<Login>>
    fun getAlProduct(cuce:String):Flow<Response<Products>>
    fun authSettings(cuce:String,sig:String,phone:String,password:String):Flow<Response<ResponseSettings>>
    fun logOut(cuce:String,sig:String):Flow<Response<LogOut>>
}