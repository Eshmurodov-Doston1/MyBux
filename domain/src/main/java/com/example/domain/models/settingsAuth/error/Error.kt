package com.example.domain.models.settingsAuth.error

data class Error(
    val error_code: Int,
    val error_msg: String,
    val request_params: List<Any>
)