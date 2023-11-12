package com.monke.machnomusic3.domain.usecase.music

import com.monke.machnomusic3.domain.repository.MusicRepository
import javax.inject.Inject

class GetCurrentTrackUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {

    fun execute() = musicRepository.currentTrack
}