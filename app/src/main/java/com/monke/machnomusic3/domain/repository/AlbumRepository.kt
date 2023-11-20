package com.monke.machnomusic3.domain.repository

import android.net.Uri
import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    val userAlbumsList: Flow<List<Album>>

    /**
     * Загружает альбом на сервер
     * @param track - данные трека
     * @param coverUri - изображение обложки
     */
    suspend fun uploadAlbum(
        album: Album,
        coverUri: Uri
    ): Result<Any?>


    suspend fun loadAlbums(
        user: User
    ): Result<Any?>

    suspend fun getAlbumCoverUrl(albumId: String): Result<String>

}