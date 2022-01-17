package com.example.domain.iteraktor

import com.example.domain.models.auth.reqAuth.ReqAuth
import com.example.domain.models.auth.resAuth.Login
import com.example.domain.models.getProductList.getProducts.Products
import com.example.domain.models.logOut.successLogOut.LogOut
import com.example.domain.models.settingsAuth.res.ResponseSettings
import com.example.domain.repository.MyBuxRepository
import com.example.domain.models.sig.Sig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MyBuxIteraktor @Inject constructor(private val myBuxRepository: MyBuxRepository){
    fun getSig(): Flow<Result<Sig?>> {
        return myBuxRepository.getSig().map {
                if (it.code()==404){
                    Result.failure(Throwable("404"))
                }else if (it.code()==500 || it.code()>500){
                    Result.failure(Throwable("500"))
                } else if (it.isSuccessful){
                    Result.success(it.body())
                }else{
                    Result.success(null)
                }
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
    }

    fun getAuthRes(reqAuth: ReqAuth): Flow<Result<Login?>> {
        return myBuxRepository.getAuthRes(reqAuth).map {
            if (it.code()==404){
                Result.failure(Throwable("404"))
            }else if (it.code()==500 || it.code()>500){
                Result.failure(Throwable("500"))
            } else if (it.isSuccessful){
                Result.success(it.body())
            }else{
                Result.success(null)
            }
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
    }


    fun getAllProducts(cuce:String): Flow<Result<Products?>> {
        return myBuxRepository.getAlProduct(cuce).map {
            if (it.code()==404){
                Result.failure(Throwable("404"))
            }else if (it.code()==500 || it.code()>500){
                Result.failure(Throwable("500"))
            } else if (it.isSuccessful){
                Result.success(it.body())
            }else{
                Result.success(null)
            }
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
    }

    fun authSettings(cuce:String,sig:String,phone:String,password:String):Flow<Result<ResponseSettings?>>{
        return myBuxRepository.authSettings(cuce,sig,phone,password).map {
            if (it.code()==404){
                Result.failure(Throwable("404"))
            }else if (it.code()==500 || it.code()>500){
                Result.failure(Throwable("500"))
            } else if (it.isSuccessful){
                Result.success(it.body())
            }else{
                Result.success(null)
            }
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
    }

    fun logOut(cuce:String,sig:String):Flow<Result<LogOut?>>{
        return myBuxRepository.logOut(cuce,sig).map {
            if (it.code()==404){
                Result.failure(Throwable("404"))
            }else if (it.code()==500 || it.code()>500){
                Result.failure(Throwable("500"))
            } else if (it.isSuccessful){
                Result.success(it.body())
            }else{
                Result.success(null)
            }
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
    }
}