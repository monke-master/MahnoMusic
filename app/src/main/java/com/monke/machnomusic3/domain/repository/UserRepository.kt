package com.monke.machnomusic3.domain.repository

import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val user: Flow<User?>

    suspend fun updateUser(user: User): Result<Any?>

    suspend fun signIn(email: String, password: String): Result<User?>

    suspend fun createUser(user: User): Result<Any?>

    suspend fun getUserById(userId: String): Result<User?>
}