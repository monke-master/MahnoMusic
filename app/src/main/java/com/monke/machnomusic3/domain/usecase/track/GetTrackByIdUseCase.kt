package com.monke.machnomusic3.domain.usecase.track

import com.monke.machnomusic3.domain.repository.TrackRepository
import javax.inject.Inject

class GetTrackByIdUseCase @Inject constructor(
    private val trackRepository: TrackRepository
) {


    suspend fun execute(trackId: String) = trackRepository.getTrackById(trackId)

}