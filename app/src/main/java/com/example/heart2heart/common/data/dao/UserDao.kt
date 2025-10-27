package com.example.heart2heart.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heart2heart.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserByUUID(id: UUID): User?

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

}