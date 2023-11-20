package com.monke.machnomusic3.data.repository

import android.net.Uri
import android.util.Log
import com.monke.machnomusic3.data.remote.TRACKS_COVERS_STORAGE
import com.monke.machnomusic3.data.remote.firestore.AlbumFirestore
import com.monke.machnomusic3.data.remote.storage.Storage
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@MainScope
class AlbumRepositoryImpl @Inject constructor(
    private val albumFirestore: AlbumFirestore,
    private val storage: Storage
): AlbumRepository {

    override val userAlbumsList = MutableStateFlow<List<Album>>(emptyList())

    init {
        Log.d("AlbumRepositoryImpl", "Init block")
    }

    override suspend fun uploadAlbum(
        album: Album,
        coverUri: Uri
    ): Result<Any?> {
        // Загрузка обложки
        val imageResult = storage.uploadFileWithUri(
            uri = coverUri,
            path = "$TRACKS_COVERS_STORAGE/${album.id}"
        )
        if (imageResult.isFailure)
            return imageResult
        // Создание записи с треком
        val result = albumFirestore.setAlbum(album)
        if (result.isFailure) return result
        userAlbumsList.value = userAlbumsList.value.toMutableList().apply { add(album) }
        return Result.success(null)
    }

    override suspend fun loadAlbums(user: User): Result<Any?> {
        for (albumsId in user.albumsIdsList) {
            val trackRequest = albumFirestore.getAlbumById(albumsId)
            if (trackRequest.isFailure) return trackRequest
            trackRequest.getOrNull()?.let {
                userAlbumsList.value = userAlbumsList.value.toMutableList().apply { add(it) }
            }
        }
        return Result.success(null)
    }

    override suspend fun getAlbumCoverUrl(albumId: String): Result<String> {
        return storage.getDownloadUrl("$TRACKS_COVERS_STORAGE/${albumId}")
    }

}
