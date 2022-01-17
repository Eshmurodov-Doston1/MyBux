package com.example.domain.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth")
data class AuthEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int=0,
    @ColumnInfo(name = "remember_token")
    var remember_token:String,
    @ColumnInfo(name = "inn")
    var inn: String,
    @ColumnInfo(name = "shortname")
    var shortname: String,
    @ColumnInfo(name = "adress")
    var adress: String,
    @ColumnInfo(name = "phone")
    var phone: String,
    @ColumnInfo(name = "user_id")
    var  user_id:String)