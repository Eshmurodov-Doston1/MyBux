package com.example.domain.database.products

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "products")
data class Products(
    var bar_code: String?=null,
    var cat_title: String?=null,
    var classCode: String?=null,
    var className: String?=null,
    var commissionTin: String?=null,
    var count: String?=null,
    var discount: String?=null,
    var group_id: String?=null,
    @PrimaryKey
    var id: Int,
    var label: String?=null,
    var measure_id: String?=null,
    var measure_name: String?=null,
    var name: String?=null,
    var photo: String?=null,
    var price: String?=null,
    var vat: String?=null
)
