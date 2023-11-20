package com.monke.machnomusic3.domain.usecase.track

import com.monke.machnomusic3.domain.repository.TrackRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadTrackUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute() = withContext(Dispatchers.IO) {
        trackRepository.loadTracks(userRepository.getUser())
    }
}