package com.monke.machnomusic3.data.repository

import android.net.Uri
import android.util.Log
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.remote.TRACKS_COVERS_STORAGE
import com.monke.machnomusic3.data.remote.TRACKS_STORAGE
import com.monke.machnomusic3.data.remote.firestore.TrackFirestore
import com.monke.machnomusic3.data.remote.firestore.UserFirestore
import com.monke.machnomusic3.data.remote.storage.Storage
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.TrackRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@MainScope
class TrackRepositoryImpl @Inject constructor(
    private val trackFirestore: TrackFirestore,
    private val userFirestore: UserFirestore,
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
                return imageResult
        }
        // Создание записи с треком
        val result = trackFirestore.setTrack(track)
        if (result.isFailure) return result
        userTracksList.value = userTracksList.value.toMutableList().apply { add(track) }
        return Result.success(null)
    }

    override suspend fun loadLikedTracks(user: User): Result<Any?> {
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

    override suspend fun getTrackById(trackId: String): Result<Track?> {
        return trackFirestore.getTrackById(trackId)
    }


    override suspend fun searchTracks(query: String): Result<List<Track>> {
        val result = trackFirestore.searchTrack(query)
        result.exceptionOrNull()?.let { return Result.failure(it) }
        val tracksList = ArrayList<Track>()
        for (trackRemote in result.getOrNull() ?: emptyList()) {
            if (trackRemote == null)
                return Result.failure(NotFoundException())
            val author = userFirestore
                .getUserById(trackRemote.authorId)
                .getOrNull() ?: return Result.failure(NotFoundException())
            tracksList.add(trackRemote.toDomain(author))
        }
        return Result.success(tracksList)
    }

}