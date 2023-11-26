package com.monke.machnomusic3.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.remote.firestore.UserFirestore
import com.monke.machnomusic3.di.AppScope
import com.monke.machnomusic3.domain.exception.NoUserException
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AppScope
class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userFirestore: UserFirestore
) : UserRepository {

    override val user = MutableStateFlow<User?>(null)

    override suspend fun updateUser(user: User): Result<Any?> {
        this.user.value = user
        try {
            return userFirestore.setUser(user)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User?> {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result?.user?.let {
                return userFirestore.getUserById(it.uid)
            }
            return Result.failure(NoUserException())
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    override suspend fun createUser(user: User): Result<Any?> {
        return userFirestore.setUser(user)
    }

    override suspend fun getUserById(userId: String): Result<User?> {
        return userFirestore.getUserById(userId)
    }

    override suspend fun searchUser(query: String): Result<List<User>> {
        val result = userFirestore.searchUser(query)
        result.exceptionOrNull()?.let { return Result.failure(it)}
        val users = result.getOrNull()?.map { user ->
                user?.toDomain() ?: return Result.failure(NotFoundException())
        } ?: return Result.failure(NotFoundException())
        return Result.success(users)
    }

}