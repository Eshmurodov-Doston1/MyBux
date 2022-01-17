package com.example.domain.models.getProductList.getProducts

import com.example.domain.models.getProductList.errorProduct.Error

data class Products(
    val response: Response,
    val error:Error
)