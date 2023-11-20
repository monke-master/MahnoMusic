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


    suspend fun loadTracks(
        user: User
    ): Result<Any?>

    suspend fun getTrackCoverUrl(trackId: String): Result<String>

    suspend fun getTrackUrl(trackId: String): Result<String>

}