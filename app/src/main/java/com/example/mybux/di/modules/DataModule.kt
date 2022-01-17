package com.example.mybux.di.modules

import com.example.data.network.ApiService
import com.example.domain.repository.MyBuxRepository
import com.example.data.repository.MyBuxRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [DataModule.BindModule::class])
class DataModule {
    @Singleton
    @Provides
    fun provideApiservise(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    @Module
    abstract class BindModule{
        @Binds
        abstract fun bindsApiRepository(myBuxRepositoryImpl: MyBuxRepositoryImpl): MyBuxRepository
    }

}