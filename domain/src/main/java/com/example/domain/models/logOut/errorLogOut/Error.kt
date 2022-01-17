package com.example.domain.models.logOut.errorLogOut

data class Error(
    val error_code: Int,
    val error_msg: String,
    val request_params: List<Any>
)