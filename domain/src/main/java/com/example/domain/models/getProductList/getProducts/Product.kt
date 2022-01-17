package com.example.domain.models.getProductList.getProducts

data class Product(
    val bar_code: String?=null,
    val cat_title: String?=null,
    val classCode: String?=null,
    val className: String?=null,
    val commissionTin: String?=null,
    val count: String?=null,
    val discount: String?=null,
    val group_id: String?=null,
    val id: Int,
    val label: String?=null,
    val measure_id: String?=null,
    val measure_name: String?=null,
    val name: String?=null,
    val photo: String?=null,
    val price: String?=null,
    val vat: String?=null
)