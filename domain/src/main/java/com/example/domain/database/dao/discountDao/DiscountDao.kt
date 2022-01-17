package com.example.domain.database.dao.discountDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.database.discount.DiscountEntity

@Dao
interface DiscountDao {
    @Insert
    fun insertDiscount(discountEntity: DiscountEntity)

    @Update
    fun updateDiscount(discountEntity: DiscountEntity)

    @Delete
    fun deleteDisCount(discountEntity: DiscountEntity)

    @Query("SELECT*FROM discount")
    fun getAllDiscount():LiveData<List<DiscountEntity>>
}