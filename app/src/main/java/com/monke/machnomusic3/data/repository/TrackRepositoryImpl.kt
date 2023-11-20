package com.monke.machnomusic3.data.repository

import android.net.Uri
import android.util.Log
import com.monke.machnomusic3.data.remote.TRACKS_COVERS_STORAGE
import com.monke.machnomusic3.data.remote.TRACKS_STORAGE
import com.monke.machnomusic3.data.remote.firestore.TrackFirestore
import com.monke.machnomusic3.data.remote.storage.Storage
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.TrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@MainScope
class TrackRepositoryImpl @Inject constructor(
    private val trackFirestore: TrackFirestore,
    private val storage: Storage
): TrackRepository {


    override val userTracksList = MutableStateFlow<List<Track>>(emptyList())


    init {
        Log.d("TrackRepositoryImpl", "Init block")
    }

    override suspend fun uploadTrack(
        track: Track,
        trackUri: Uri,
        coverUri: Uri?
    ): Result<Any?> {
        // Загрузка аудио-файла
        val trackStorageResult = storage.uploadFileWithUri(
            uri = trackUri,
            path = "$TRACKS_STORAGE/${track.id}"
        )
        if (trackStorageResult.isFailure)
            return trackStorageResult
        // Загрузка обложки
        coverUri?.let {
            val imageResult = storage.uploadFileWithUri(
                uri = it,
                path = "$TRACKS_COVERS_STORAGE/${track.id}"
            )
            if (imageResult.isFailure)
                return trackStorageResult
        }
        // Создание записи с треком
        val result = trackFirestore.setTrack(track)
        if (result.isFailure) return result
        userTracksList.value = userTracksList.value.toMutableList().apply { add(track) }
        return Result.success(null)
    }

    override suspend fun loadTracks(user: User): Result<Any?> {
        for (trackId in user.tracksIdsList) {
            val trackRequest = trackFirestore.getTrackById(trackId)
            if (trackRequest.isFailure) return trackRequest
            trackRequest.getOrNull()?.let {
                userTracksList.value = userTracksList.value.toMutableList().apply { add(it) }
            }
        }
        return Result.success(null)
    }

    override suspend fun getTrackCoverUrl(trackId: String): Result<String> {
        return storage.getDownloadUrl("$TRACKS_COVERS_STORAGE/${trackId}")
    }

    override suspend fun getTrackUrl(trackId: String): Result<String> {
        return storage.getDownloadUrl("$TRACKS_STORAGE/${trackId}")
    }


}