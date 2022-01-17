package com.example.mybux.di.modules

import com.example.domain.constant.AppConstant
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideBaseURL():String = AppConstant.BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl:String):Retrofit{
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).
        build()
    }
}