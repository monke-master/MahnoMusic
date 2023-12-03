package com.monke.machnomusic3.data.repository

import android.net.Uri
import android.util.Log
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.remote.TRACKS_COVERS_STORAGE
import com.monke.machnomusic3.data.remote.firestore.PlaylistFirestore
import com.monke.machnomusic3.data.remote.storage.Storage
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.domain.model.Playlist
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@MainScope
class PlaylistRepositoryImpl @Inject constructor(
    private val playlistFirestore: PlaylistFirestore,
    private val storage: Storage
): PlaylistRepository {


    init {
        Log.d("PlaylistRepositoryImpl", "Init block")
    }


    override val userPlaylistsList = MutableStateFlow<List<Playlist>>(emptyList())

    override suspend fun uploadPlaylist(
        playlist: Playlist,
        coverUri: Uri
    ): Result<Any?> {
        // Загрузка обложки
        val imageResult = storage.uploadFileWithUri(
            uri = coverUri,
            path = "$TRACKS_COVERS_STORAGE/${playlist.id}"
        )
        if (imageResult.isFailure)
            return imageResult
        // Создание записи с треком
        val result = playlistFirestore.setPlaylist(playlist)
        if (result.isFailure) return result
        userPlaylistsList.value = userPlaylistsList.value.toMutableList().apply { add(playlist) }
        return Result.success(null)
    }

    override suspend fun loadPlaylists(user: User): Result<Any?> {
        for (playlistId in user.playlistsIdsList) {
            val trackRequest = playlistFirestore.getPlaylistById(playlistId)
            if (trackRequest.isFailure) return trackRequest
            trackRequest.getOrNull()?.let {
                userPlaylistsList.value =
                    userPlaylistsList.value.toMutableList().apply { add(it.toDomain(user)) }
            }
        }
        return Result.success(null)
    }

    override suspend fun getPlaylistCoverUri(playlistId: String): Result<String> {
        return storage.getDownloadUrl("$TRACKS_COVERS_STORAGE/${playlistId}")
    }

}
