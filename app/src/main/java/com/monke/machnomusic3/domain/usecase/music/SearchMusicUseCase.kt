package com.monke.machnomusic3.domain.usecase.music

import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchMusicUseCase @Inject constructor(
    private val trackRepository: TrackRepository
) {

    suspend fun execute(
        query: String
    ): Result<List<Track>> = withContext(Dispatchers.IO) {
        trackRepository.searchTracks(query)
    }

}