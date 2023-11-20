package com.monke.machnomusic3.domain.usecase.track

import com.monke.machnomusic3.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrackCoverUrlUseCase @Inject constructor(
    private val trackRepository: TrackRepository
) {

    suspend fun execute(
        coverId: String
    ) = withContext(Dispatchers.IO) {
        return@withContext trackRepository.getTrackCoverUrl(coverId)
    }
}