package com.example.domain.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.database.basket.Basket
import io.reactivex.Single

@Dao
interface BasketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBasket(basket: Basket)

    @Query("SELECT * FROM basket WHERE id = :id  LIMIT 1")
    fun findById(id: Long): Basket

    @Query("UPDATE basket SET count=:pr_count WHERE id = :id")
    fun updateCount(id: Long, pr_count: String)

    @Query("SELECT * FROM basket")
    fun getBasketList(): LiveData<List<Basket>>

    @Query("SELECT * FROM basket")
    fun getBasketListMy(): List<Basket>


    @Query("DELETE FROM basket")
    fun deleteAllBaskets()

    @Delete
    fun deleteBasket(basket: Basket)

    @Update
    fun updateBasket(basket: Basket?)
}