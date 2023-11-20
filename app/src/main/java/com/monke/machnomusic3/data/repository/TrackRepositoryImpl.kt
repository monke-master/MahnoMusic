package com.monke.machnomusic3.data.repository

import android.net.Uri
import com.monke.machnomusic3.data.remote.TRACKS_COVERS_STORAGE
import com.monke.machnomusic3.data.remote.firestore.TrackFirestore
import com.monke.machnomusic3.data.remote.storage.ImageStorage
import com.monke.machnomusic3.data.remote.storage.TrackStorage
import com.monke.machnomusic3.di.AppScope
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.TrackRepository
import javax.inject.Inject


@MainScope
class TrackRepositoryImpl @Inject constructor(
    private val trackFirestore: TrackFirestore,
    private val trackStorage: TrackStorage,
    private val imageStorage: ImageStorage
): TrackRepository {


    override suspend fun uploadTrack(
        track: Track,
        trackUri: Uri,
        coverUri: Uri?
    ): Result<Any?> {
        // Загрузка аудио-файла
        val trackStorageResult = trackStorage.uploadTrack(
            uri = trackUri,
            trackId = track.id
        )
        if (trackStorageResult.isFailure)
            return trackStorageResult
        // Загрузка обложки
        coverUri?.let {
            val imageResult = imageStorage.uploadImage(
                uri = it,
                path = "$TRACKS_COVERS_STORAGE/${track.id}"
            )
            if (imageResult.isFailure)
                return trackStorageResult
        }
        // Создание записи с треком
        return trackFirestore.setTrack(track)
    }


}