package com.monke.machnomusic3.domain.usecase.album

import android.net.Uri
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.UploadingTrack
import com.monke.machnomusic3.domain.repository.AlbumRepository
import com.monke.machnomusic3.domain.repository.TrackRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class UploadAlbumUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val albumRepository: AlbumRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute(
        uploadingTracksList: List<UploadingTrack>,
        title: String,
        coverUri: Uri,
    ) = withContext(Dispatchers.IO) {
        val author = userRepository.user.first()
            ?: return@withContext Result.failure(NotFoundException())
        val albumId = UUID.randomUUID().toString()
        val releaseDate = Calendar.getInstance().timeInMillis
        val tracksList = uploadingTracksList.map {
            Track(
                id = it.id,
                title = it.title,
                coverId = albumId,
                author = author,
                duration = it.duration,
                releaseDate = releaseDate
            )
        }

        for (index in tracksList.indices) {
            val track = tracksList[index]
            val res = trackRepository.uploadTrack(track, uploadingTracksList[index].uri)
            if (res.isFailure)
                return@withContext res
        }

        val album = Album(
            id = albumId,
            author = author,
            title = title,
            tracksIdsList = tracksList.map { it.id },
            coverId = albumId,
            releaseDate = releaseDate
        )

        val result = albumRepository.uploadAlbum(album, coverUri)
        if (result.isFailure)
            return@withContext result

        return@withContext userRepository.updateUser(
            author.copy(
                tracksIdsList = author.tracksIdsList + tracksList.map { it.id },
                albumsIdsList = author.albumsIdsList + albumId
            )
        )
    }
}