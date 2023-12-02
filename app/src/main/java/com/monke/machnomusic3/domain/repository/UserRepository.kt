package com.monke.machnomusic3.domain.repository

import android.net.Uri
import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val user: Flow<User?>

    suspend fun updateUser(user: User): Result<Any?>

    suspend fun signIn(email: String, password: String): Result<User?>

    suspend fun createUser(user: User): Result<Any?>

    suspend fun getUserById(userId: String): Result<User?>

    suspend fun searchUser(query: String): Result<List<User>>

    /**
     * Saves new picture to storage
     * @return result
     */
    suspend fun updateProfilePicture(
        uri: Uri,
        pictureId: String
    ): Result<Any?>

    suspend fun getProfilePicUrlUseCase(id: String): Result<String>
}