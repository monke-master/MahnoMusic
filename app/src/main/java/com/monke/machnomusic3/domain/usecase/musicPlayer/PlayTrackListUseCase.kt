package com.monke.machnomusic3.domain.usecase.musicPlayer

import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.MusicRepository
import javax.inject.Inject

class PlayTrackListUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {

    fun execute(
        tracksList: List<Track>,
        playFromIndex: Int? = null
    ) {
        musicRepository.setMusicState(MusicState.Start)
        musicRepository.setTrackQueue(tracksList)
        musicRepository.playFromPosition(playFromIndex ?: 0)
        musicRepository.setMusicState(MusicState.Resume)
    }

}