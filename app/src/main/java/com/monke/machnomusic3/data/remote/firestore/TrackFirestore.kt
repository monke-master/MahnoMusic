package com.monke.machnomusic3.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.monke.machnomusic3.data.extensions.toRemote
import com.monke.machnomusic3.data.remote.TRACKS_COLLECTION
import com.monke.machnomusic3.domain.model.Track
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class TrackFirestore @Inject constructor(
    private val firestore: FirebaseFirestore
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

}