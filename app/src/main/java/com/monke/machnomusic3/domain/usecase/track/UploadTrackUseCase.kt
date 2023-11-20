package com.monke.machnomusic3.domain.usecase.track

import android.net.Uri
import com.monke.machnomusic3.data.remote.DEFAULT_TRACK_COVER_ID
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.TrackRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
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
        val user = userRepository.getUser()

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

        return@withContext trackRepository.uploadTrack(
            track = track,
            trackUri = trackUri,
            coverUri = coverUri
        )
    }
}