package com.monke.machnomusic3.domain.repository

import android.net.Uri
import com.monke.machnomusic3.domain.model.Playlist
import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    val userPlaylistsList: Flow<List<Playlist>>

    /**
     * Загружает плейлист на сервер
     * @param track данные трека
     * @param coverUri изображение обложки
     */
    suspend fun uploadPlaylist(
        playlist: Playlist,
        coverUri: Uri
    ): Result<Any?>


    suspend fun loadPlaylists(
        user: User
    ): Result<Any?>

    suspend fun getPlaylistCoverUri(playlistId: String): Result<String>
}