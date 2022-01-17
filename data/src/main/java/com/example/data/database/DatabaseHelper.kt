package com.example.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.database.basket.Basket
import com.example.domain.database.dao.AuthDao
import com.example.domain.database.dao.BasketDao
import com.example.domain.database.dao.ProductDao
import com.example.domain.database.dao.discountDao.DiscountDao
import com.example.domain.database.discount.DiscountEntity
import com.example.domain.database.entity.AuthEntity
import com.example.domain.database.products.Products
import javax.inject.Inject

class DatabaseHelper @Inject constructor(
    private val authDao: AuthDao,
    private val productDao: ProductDao,
    private val basketDao: BasketDao,
    private val discountDao: DiscountDao
) {
    suspend fun insertAuth(authEntity: AuthEntity){
        authDao.insertAuth(authEntity)
    }

    fun getAllAuth():LiveData<AuthEntity>{
        return authDao.getAllAuth()
    }

    fun deleteAuth(authEntity: AuthEntity){
        authDao.deleteTable(authEntity)
    }
    suspend fun insertProducts(listProducts:List<Products>){
        productDao.insertProducts(listProducts)
    }

    fun getAllProduct():LiveData<List<Products>>{
        return productDao.getAllProducts()
    }

    fun getAllProductsMy():List<Products>{
        return productDao.getAllProductsMyApp()
    }

    fun insertBasket(basket:Basket){
        basketDao.insertBasket(basket)
    }
    fun findIdBasket(id:Long):Basket{
        return basketDao.findById(id)
    }

    fun updateCount(id: Long, pr_count: String){
        basketDao.updateCount(id,pr_count)
    }

    fun getBasketList():LiveData<List<Basket>>{
        return basketDao.getBasketList()
    }

    fun getBasketListMy():List<Basket>{
        return basketDao.getBasketListMy()
    }
    fun clearBasket(){
        basketDao.deleteAllBaskets()
    }
    fun deleteBasket(basket: Basket){
        basketDao.deleteBasket(basket)
    }

    fun updateBasket(basket: Basket){
        basketDao.updateBasket(basket)
    }

    fun insertDiscount(discountEntity: DiscountEntity){
        discountDao.insertDiscount(discountEntity)
    }

    fun deleteDiscount(discountEntity: DiscountEntity){
        discountDao.deleteDisCount(discountEntity)
    }

    fun updateDiscount(discountEntity: DiscountEntity){
        discountDao.updateDiscount(discountEntity)
    }

    fun getAllDisCount():LiveData<List<DiscountEntity>>{
        return discountDao.getAllDiscount()
    }
}