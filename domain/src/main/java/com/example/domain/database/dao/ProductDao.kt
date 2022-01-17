package com.example.domain.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.database.products.Products

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(listProduct:List<Products>)

    @Query("SELECT*FROM products")
    fun getAllProducts():LiveData<List<Products>>

    @Query("SELECT*FROM products")
    fun getAllProductsMyApp():List<Products>
}