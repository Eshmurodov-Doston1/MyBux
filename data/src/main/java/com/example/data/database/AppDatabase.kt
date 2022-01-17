package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domain.database.basket.Basket
import com.example.domain.database.dao.AuthDao
import com.example.domain.database.dao.BasketDao
import com.example.domain.database.dao.ProductDao
import com.example.domain.database.dao.discountDao.DiscountDao
import com.example.domain.database.discount.DiscountEntity
import com.example.domain.database.entity.AuthEntity
import com.example.domain.database.products.Products

@Database(entities = [AuthEntity::class,Products::class,Basket::class,DiscountEntity::class], version = 2)
abstract class AppDatabase:RoomDatabase(){
    abstract fun authDao():AuthDao
    abstract fun productsDao():ProductDao
    abstract fun basketDao():BasketDao
    abstract fun discountDao():DiscountDao
}