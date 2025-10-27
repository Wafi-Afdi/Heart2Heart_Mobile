package com.example.heart2heart.common.domain.repository

import com.example.heart2heart.common.domain.model.User

interface ECGSignalRepository {
    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)
}