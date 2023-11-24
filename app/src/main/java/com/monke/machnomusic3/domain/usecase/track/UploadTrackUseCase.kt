package com.monke.machnomusic3.domain.usecase.track

import android.net.Uri
import com.monke.machnomusic3.data.remote.DEFAULT_TRACK_COVER_ID
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.TrackRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class UploadTrackUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute(
        trackUri: Uri,
        title: String,
        coverUri: Uri? = null,
        duration: Int
    ): Result<Any?> = withContext(Dispatchers.IO) {
        val user = userRepository.user.first()
            ?: return@withContext Result.failure(NotFoundException())

        val trackId = UUID.randomUUID().toString()
        val coverId = if (coverUri == null) DEFAULT_TRACK_COVER_ID else trackId

        val track = Track(
            id = trackId,
            title = title,
            coverId = coverId,
            author = user,
            duration = duration,
            releaseDate = Calendar.getInstance().timeInMillis
        )

        val uploadRes = trackRepository.uploadTrack(
            track = track,
            trackUri = trackUri,
            coverUri = coverUri
        )

        if (uploadRes.isFailure) return@withContext uploadRes

        return@withContext userRepository.updateUser(
            user = user.copy(
                tracksIdsList = user.tracksIdsList.toMutableList().apply { add(trackId)}
            )
        )
    }
}