package com.monke.machnomusic3.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.extensions.toRemote
import com.monke.machnomusic3.data.remote.TRACKS_COLLECTION
import com.monke.machnomusic3.data.remote.USERS_COLLECTION
import com.monke.machnomusic3.data.remote.dto.TrackRemote
import com.monke.machnomusic3.data.remote.dto.UserRemote
import com.monke.machnomusic3.domain.exception.NoUserException
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class TrackFirestore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun setTrack(track: Track): Result<Any?> {
        try {
            firestore.collection(TRACKS_COLLECTION).document(track.id).set(track.toRemote()).await()
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    suspend fun getTrackById(id: String): Result<Track?> {
        try {
            val response = firestore.collection(TRACKS_COLLECTION).document(id).get().await()
            val track = response.toObject<TrackRemote>()
            // Получение автора трека
            val authorId = track?.authorId ?: return Result.failure(NotFoundException())
            val authorRequest = userFirestore.getUserById(authorId)
            if (authorRequest.isFailure) return Result.failure(NotFoundException())
            val author = authorRequest.getOrNull() ?: return Result.failure(NotFoundException())

            return Result.success(track.toDomain(author))
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }


}