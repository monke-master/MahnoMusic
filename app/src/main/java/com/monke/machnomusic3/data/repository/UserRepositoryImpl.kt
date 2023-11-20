package com.monke.machnomusic3.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.monke.machnomusic3.data.remote.firestore.UserFirestore
import com.monke.machnomusic3.di.AppScope
import com.monke.machnomusic3.domain.exception.NoUserException
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.model.mockedUser1
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AppScope
class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: UserFirestore
) : UserRepository {

    private lateinit var user: User

    init {
        user = mockedUser1
    }

    override suspend fun updateUser(user: User): Result<Any?> {
        this.user = user
        try {
            return firestore.setUser(user)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    override fun getUser(): User = user


    override suspend fun signIn(email: String, password: String): Result<User?> {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result?.user?.let {
                return firestore.getUserById(it.uid)
            }
            return Result.failure(NoUserException())
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    override suspend fun createUser(user: User): Result<Any?> {
        return firestore.setUser(user)
    }

}