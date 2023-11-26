package com.monke.machnomusic3.domain.repository

import android.net.Uri
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.flow.Flow


interface TrackRepository {

    val userTracksList: Flow<List<Track>>

    /**
     * Загружает трек на сервер
     * @param track - данные трека
     * @param trackUri - аудио-файл
     * @param coverUri - изображение обложки
     */
    suspend fun uploadTrack(
        track: Track,
        trackUri: Uri,
        coverUri: Uri? = null
    ): Result<Any?>


    suspend fun loadLikedTracks(
        user: User
    ): Result<Any?>

    suspend fun getTrackCoverUrl(trackId: String): Result<String>

    suspend fun getTrackUrl(trackId: String): Result<String>

    suspend fun getTrackById(trackId: String): Result<Track?>

    /**
     * Поиск треков в базе данных
     * @param query запрос
     * @return Список треков или ошибку
     */
    suspend fun searchTracks(query: String): Result<List<Track>>
}