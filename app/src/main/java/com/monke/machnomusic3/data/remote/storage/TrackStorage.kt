package com.monke.machnomusic3.data.remote.storage

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.monke.machnomusic3.data.remote.TRACKS_STORAGE
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class TrackStorage @Inject constructor(
    private val storage: FirebaseStorage
) {

    suspend fun uploadTrack(uri: Uri, trackId: String): Result<Any?> {
        try {
            val trackRef = storage.reference.child("$TRACKS_STORAGE/$trackId")
            val result = trackRef.putFile(uri).await()
            Log.d("TrackStorage", "Track $trackId has been uploaded")
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }
}