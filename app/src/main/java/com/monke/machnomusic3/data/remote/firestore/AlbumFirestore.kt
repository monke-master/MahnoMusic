package com.monke.machnomusic3.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.extensions.toRemote
import com.monke.machnomusic3.data.remote.ALBUM_COLLECTION
import com.monke.machnomusic3.data.remote.dto.AlbumRemote
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Album
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AlbumFirestore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun setAlbum(album: Album): Result<Any?> {
        try {
            firestore.collection(ALBUM_COLLECTION).document(album.id).set(album.toRemote()).await()
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    suspend fun getAlbumById(id: String): Result<Album> {
        try {
            val response = firestore.collection(ALBUM_COLLECTION).document(id).get().await()
            val album = response.toObject<AlbumRemote>()
            // Получение автора альбома
            val authorId = album?.authorId ?: return Result.failure(NotFoundException())
            val authorRequest = userFirestore.getUserById(authorId)
            if (authorRequest.isFailure) return Result.failure(NotFoundException())
            val author = authorRequest.getOrNull() ?: return Result.failure(NotFoundException())

            return Result.success(album.toDomain(author))
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }


}