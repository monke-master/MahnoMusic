package com.monke.machnomusic3.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class UserFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
) {


    suspend fun setUser(user: User): Result<Any?> {
        try {
            firestore.collection(USERS_COLLECTION).document(user.id).set(user).await()
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    suspend fun getUserById(id: String): Result<User?> {
        try {
            val response = firestore.collection(USERS_COLLECTION).document(id).get().await()
            val user = response.toObject<User>()
            return Result.success(user)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

}