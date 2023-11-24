package com.monke.machnomusic3.domain.usecase.track

import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.repository.TrackRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadLikedTracksUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute() = withContext(Dispatchers.IO) {
        val user = userRepository.user.first()
            ?: return@withContext Result.failure(NotFoundException())
        trackRepository.loadLikedTracks(user)
    }
}