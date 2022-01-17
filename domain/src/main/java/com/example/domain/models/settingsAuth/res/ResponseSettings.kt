package com.example.domain.models.settingsAuth.res

import com.example.domain.models.settingsAuth.error.Error

data class ResponseSettings(
    var response: Response,
    val error: Error
)