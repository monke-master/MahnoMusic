package com.monke.machnomusic3.domain.usecase.music

import com.monke.machnomusic3.domain.model.TrackProgress
import com.monke.machnomusic3.domain.repository.MusicRepository
import javax.inject.Inject

class SetTrackProgressUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {

    fun execute(progress: TrackProgress) {
        musicRepository.setTrackProgress(progress)
    }
}