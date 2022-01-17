package com.example.mybux.viewModul

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.DatabaseHelper
import com.example.domain.database.entity.AuthEntity
import com.example.domain.iteraktor.MyBuxIteraktor
import com.example.domain.models.auth.reqAuth.ReqAuth
import com.example.domain.models.auth.resAuth.Login
import com.example.domain.models.sig.Sig
import com.example.mybux.utils.MybuxResourse
import com.example.mybux.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelAuth @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val myBuxIteraktor: MyBuxIteraktor,
    private val databaseHelper: DatabaseHelper):ViewModel() {

    fun getSig():StateFlow<MybuxResourse>{
        val mutableStateFlow = MutableStateFlow<MybuxResourse>(MybuxResourse.Loading)
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()){
                myBuxIteraktor.getSig().collect {
                    if (it.isSuccess){
                        mutableStateFlow.emit(MybuxResourse.Success(it.getOrNull()?: Sig()))
                    }else{
                        it.onFailure { mutableStateFlow.emit(MybuxResourse.Error(it.message.toString())) }
                    }
                }
            }else{
                mutableStateFlow.emit(MybuxResourse.Error("503"))
            }
        }
        return mutableStateFlow;
    }

    fun getAuth(reqAuth: ReqAuth): MutableStateFlow<MybuxResourse> {
            val mutableStateFlow = MutableStateFlow<MybuxResourse>(MybuxResourse.Loading)
             viewModelScope.launch {
                 if (networkHelper.isNetworkConnected()){
                     myBuxIteraktor.getAuthRes(reqAuth).collect {
                         if (it.isSuccess){
                             Log.e("authEntity", it.getOrNull()?.toString().toString())
                             if (it.getOrNull()!=null){
                                 if (it.getOrNull()?.response!=null){
                                     val authEntity = AuthEntity(
                                         remember_token = it.getOrNull()?.response?.user_info?.remember_token.toString(),
                                         inn = it.getOrNull()?.response?.user_info?.inn.toString(),
                                         shortname = it.getOrNull()?.response?.user_info?.shortname.toString(),
                                         adress = it.getOrNull()?.response?.user_info?.adress.toString(),
                                         phone = it.getOrNull()?.response?.user_info?.phone.toString(),
                                         user_id = it.getOrNull()?.response?.user_id.toString())
                                     databaseHelper.insertAuth(authEntity)
                                     mutableStateFlow.emit(MybuxResourse.SuccessAuth(databaseHelper.getAllAuth()))
                                 }else{
                                     mutableStateFlow.emit(MybuxResourse.Error(it.getOrNull()?.error?.error_msg.toString()))
                                 }
                             }else{
                                 mutableStateFlow.emit(MybuxResourse.Error(it.getOrNull()?.error?.error_msg.toString()))
                             }
                         }else{
                             it.onFailure { mutableStateFlow.emit(MybuxResourse.Error(it.message.toString())) }
                         }
                     }
                 }else{
                     mutableStateFlow.emit(MybuxResourse.Error("503"))
                 }
             }
             return mutableStateFlow;
        }


    fun logUout(cookie:String,sig:String):MutableStateFlow<MybuxResourse>{
        val logOutListFlow = MutableStateFlow<MybuxResourse>(MybuxResourse.Loading)
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()){
                myBuxIteraktor.logOut(cookie,sig).collect {
                    if (it.isSuccess){
                        if (it.getOrNull()?.response!=null){
                             logOutListFlow.emit(MybuxResourse.SuccessLogOut(it.getOrNull()))
                        }else{
                            logOutListFlow.emit(MybuxResourse.ErrorLogOut(it.getOrNull()?.error))
                        }
                    }else if (it.isFailure){
                        it.onFailure { logOutListFlow.emit(MybuxResourse.Error(it.message.toString())) }
                    }
                }
            }else{
                logOutListFlow.emit(MybuxResourse.Error("503"))
            }
        }
        return logOutListFlow
    }


    fun getAuthDatabase():DatabaseHelper{
        return databaseHelper
    }
}