package com.example.domain.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.database.entity.AuthEntity

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuth(authEntity: AuthEntity)

    @Delete
    fun deleteTable(authEntity: AuthEntity)

    @Query("SELECT*FROM auth")
    fun getAllAuth():LiveData<AuthEntity>
}