package com.monke.machnomusic3.data.remote.firestore

import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.remote.TRACKS_COLLECTION
import com.monke.machnomusic3.data.remote.TrackFields
import com.monke.machnomusic3.data.remote.USERS_COLLECTION
import com.monke.machnomusic3.data.remote.UserFields
import com.monke.machnomusic3.data.remote.dto.TrackRemote
import com.monke.machnomusic3.data.remote.dto.UserRemote
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
            val user = response.toObject<UserRemote>()
            return Result.success(user?.toDomain())
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    suspend fun searchUser(query: String): Result<List<UserRemote?>> {
        try {
            val response = firestore
                .collection(USERS_COLLECTION)
                .where(Filter.greaterThanOrEqualTo(UserFields.username, query))
                .where(Filter.lessThanOrEqualTo(UserFields.username, query + '\uf8ff'))
                .get()
                .await()
            return Result.success(response.documents.map { it.toObject<UserRemote>() })
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

}