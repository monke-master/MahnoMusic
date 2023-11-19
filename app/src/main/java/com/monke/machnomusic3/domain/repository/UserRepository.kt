package com.monke.machnomusic3.domain.repository

import com.monke.machnomusic3.domain.model.User

interface UserRepository {

    suspend fun updateUser(user: User): Result<Any?>

    fun getUser() : User?

    suspend fun signIn(email: String, password: String): Result<User?>

    suspend fun createUser(user: User): Result<Any?>
}