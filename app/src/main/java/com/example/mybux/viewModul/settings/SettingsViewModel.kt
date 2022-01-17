package com.example.mybux.viewModul.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.DatabaseHelper
import com.example.domain.iteraktor.MyBuxIteraktor
import com.example.mybux.utils.MybuxResourse
import com.example.mybux.utils.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val myBuxIteraktor: MyBuxIteraktor,
    private val databaseHelper: DatabaseHelper):ViewModel(){

        fun sendSettingsAuth(cuce:String,sig:String,phone:String,password:String):MutableStateFlow<MybuxResourse>{
            val listSettings = MutableStateFlow<MybuxResourse>(MybuxResourse.Loading)
            viewModelScope.launch {
                if (networkHelper.isNetworkConnected()){
                    myBuxIteraktor.authSettings(cuce,sig,phone,password).collect {
                        Log.e("IT", "$it")
                    if (it.isSuccess){
                        if (it.getOrNull()?.response!=null){
                            listSettings.emit(MybuxResourse.SuccessAuthSettings(it.getOrNull()!!))
                        }else{
                            listSettings.emit(MybuxResourse.Error(it.getOrNull()?.error?.error_msg?:""))
                        }
                    }else if (it.isFailure){
                        it.onFailure { listSettings.emit(MybuxResourse.Error(it.message.toString())) }
                    }else{
                        listSettings.emit(MybuxResourse.Error(it.exceptionOrNull()?.message.toString()))
                    }
                    }
                }else{
                    listSettings.emit(MybuxResourse.Error("internet"))
                }
            }
            return listSettings
        }
}