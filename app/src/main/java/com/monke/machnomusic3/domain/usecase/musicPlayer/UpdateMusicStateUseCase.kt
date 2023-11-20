package com.monke.machnomusic3.domain.usecase.musicPlayer

import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.repository.MusicRepository
import javax.inject.Inject


class UpdateMusicStateUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {

    fun execute(state: MusicState) = musicRepository.setMusicState(state)
}