package com.monke.machnomusic3.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.monke.machnomusic3.data.extensions.toRemote
import com.monke.machnomusic3.data.remote.PLAYLISTS_COLLECTION
import com.monke.machnomusic3.data.remote.dto.PlaylistRemote
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Playlist
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class PlaylistFirestore @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    suspend fun setPlaylist(playlist: Playlist): Result<Any?> {
        try {
            firestore
                .collection(PLAYLISTS_COLLECTION)
                .document(playlist.id)
                .set(playlist.toRemote())
                .await()
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    suspend fun getPlaylistById(id: String): Result<PlaylistRemote> {
        try {
            val response = firestore.collection(PLAYLISTS_COLLECTION).document(id).get().await()
            val playlist = response.toObject<PlaylistRemote>()
                ?: return Result.failure(NotFoundException())

            return Result.success(playlist)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }
}