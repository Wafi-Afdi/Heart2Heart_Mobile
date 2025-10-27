package com.example.heart2heart.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.heart2heart.common.domain.model.Cookie

@Dao
interface CookieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCookie(cookie: Cookie)

    @Delete
    suspend fun deleteCookie(cookie: Cookie)

    @Update
    suspend fun updateCookie(cookie: Cookie)

    @Query("SELECT * FROM cookie WHERE id = :id")
    suspend fun getCookieByID(id: String): Cookie?
}