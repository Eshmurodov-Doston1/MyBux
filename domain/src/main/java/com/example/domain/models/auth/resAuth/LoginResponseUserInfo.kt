package com.example.domain.models.auth.resAuth

data class LoginResponseUserInfo(
    var remember_token:String,
    var inn: String,
    var shortname: String,
    var adress: String,
    var phone: String
)