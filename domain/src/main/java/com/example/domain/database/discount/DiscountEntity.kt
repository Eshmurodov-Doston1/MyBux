package com.example.domain.database.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discount")
data class DiscountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Long?=null,
    @ColumnInfo(name = "persent")
    var persent:Double
)
