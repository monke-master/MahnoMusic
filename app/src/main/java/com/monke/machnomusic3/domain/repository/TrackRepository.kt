package com.monke.machnomusic3.domain.repository

import android.net.Uri
import com.monke.machnomusic3.domain.model.Track

interface TrackRepository {

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

}