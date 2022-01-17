package com.example.domain.models.logOut.successLogOut

import com.example.domain.models.logOut.errorLogOut.Error

data class LogOut(
    val response: Response,
    var error: Error
)