package com.example.mybux.utils

import androidx.lifecycle.LiveData
import com.example.domain.database.entity.AuthEntity
import com.example.domain.database.products.Products
import com.example.domain.models.logOut.errorLogOut.Error
import com.example.domain.models.logOut.successLogOut.LogOut
import com.example.domain.models.settingsAuth.res.ResponseSettings
import com.example.domain.models.sig.Sig

sealed class MybuxResourse {
    object Loading:MybuxResourse()
    data class Error(val string: String):MybuxResourse()
    data class Success(val sig: Sig):MybuxResourse()
    data class SuccessAuth(val authEntity: LiveData<AuthEntity>):MybuxResourse()
    data class SuccessGetProduct(val listProduct:LiveData<List<Products>>):MybuxResourse()
    data class SuccessAuthSettings(val authSettings: ResponseSettings?):MybuxResourse()
    data class SuccessLogOut(val logOut: LogOut?):MybuxResourse()
    data class ErrorLogOut(val logOut: com.example.domain.models.logOut.errorLogOut.Error?):MybuxResourse()
}