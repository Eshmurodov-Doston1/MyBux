package com.example.domain.models.sig

data class Response(
    val csrf_token: String,
    val sig: String
)