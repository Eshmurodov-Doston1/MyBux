package com.example.domain.models.getProductList.getProducts

data class Response(
    val product_list: List<Product>,
    val product_cat: List<ProductCat>
)