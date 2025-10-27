package com.example.heart2heart.common.domain.repository

import com.example.heart2heart.common.data.dao.UserDao
import com.example.heart2heart.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class UserRepositoryImp(
    private val dao: UserDao
): UserRepository {


    override fun getUsers(): Flow<List<User>> {
        return dao.getUsers()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return dao.getUserByEmail(email)
    }

    override suspend fun getUserByUUID(uuid: UUID): User? {
        return dao.getUserByUUID(uuid)
    }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }

}