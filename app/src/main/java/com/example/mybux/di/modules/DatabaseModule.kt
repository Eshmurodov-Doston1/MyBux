package com.example.mybux.di.modules

import android.content.Context
import androidx.room.Room
import com.example.data.database.AppDatabase
import com.example.domain.database.dao.AuthDao
import com.example.domain.database.dao.BasketDao
import com.example.domain.database.dao.ProductDao
import com.example.domain.database.dao.discountDao.DiscountDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context):AppDatabase{
        return Room.databaseBuilder(context,AppDatabase::class.java,"myBux.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthDao(appDatabase: AppDatabase):AuthDao = appDatabase.authDao()

    @Singleton
    @Provides
    fun provideProductDao(appDatabase: AppDatabase):ProductDao = appDatabase.productsDao()

    @Singleton
    @Provides
    fun provideBasketDao(appDatabase: AppDatabase):BasketDao = appDatabase.basketDao()

    @Singleton
    @Provides
    fun provideDiscountDao(appDatabase: AppDatabase):DiscountDao = appDatabase.discountDao()
}