package com.example.heart2heart.common.domain.repository

import com.example.heart2heart.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface UserRepository {
    fun getUsers(): Flow<List<User>>

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserByUUID(uuid: UUID): User?

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)


}