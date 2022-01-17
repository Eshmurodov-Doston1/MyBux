package com.example.data.repository

import com.example.data.network.ApiService
import com.example.domain.models.auth.reqAuth.ReqAuth
import com.example.domain.models.auth.resAuth.Login
import com.example.domain.models.getProductList.getProducts.Products
import com.example.domain.models.logOut.successLogOut.LogOut
import com.example.domain.models.settingsAuth.res.ResponseSettings
import com.example.domain.repository.MyBuxRepository
import com.example.domain.models.sig.Sig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MyBuxRepositoryImpl @Inject constructor(var apiService: ApiService): MyBuxRepository {
    override fun getSig(): Flow<Response<Sig>> {
     return flow { emit(apiService.getSig())}
    }

    override fun getAuthRes(reqAuth: ReqAuth): Flow<Response<Login>> {
        return flow { emit(apiService.authLogin(reqAuth.phone,reqAuth.password,reqAuth.sig,reqAuth.remember,reqAuth.terminal_id,reqAuth.device_id)) }
    }

    override fun getAlProduct(cuce: String): Flow<Response<Products>> {
        return flow { emit(apiService.getProductList(cuce)) }
    }

    override fun authSettings(
        cuce: String,
        sig: String,
        phone: String,
        password: String
    ): Flow<Response<ResponseSettings>> {
        return flow { emit(apiService.settingsLogin(cuce,sig,phone,password)) }
    }

    override fun logOut(cuce: String, sig: String): Flow<Response<LogOut>> {
        return flow { emit(apiService.logOut(cuce,sig))}
    }
}