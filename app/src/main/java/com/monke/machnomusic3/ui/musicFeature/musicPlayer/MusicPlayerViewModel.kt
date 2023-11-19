package com.monke.machnomusic3.ui.musicFeature.musicPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.usecase.music.NextTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.PrevTrackUseCase
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer.MiniPlayerViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicPlayerViewModel(
    musicPlayerUseCases: MusicPlayerUseCases
): ViewModel() {

    private val getCurrentTrackUseCase = musicPlayerUseCases.getCurrentTrackUseCase
    private val getMusicStateUseCase = musicPlayerUseCases.getMusicStateUseCase
    private val updateMusicStateUseCase = musicPlayerUseCases.updateMusicStateUseCase
    private val nextTrackUseCase = musicPlayerUseCases.nextTrackUseCase
    private val prevTrackUseCase = musicPlayerUseCases.prevTrackUseCase
    private val getTrackProgressUseCase = musicPlayerUseCases.getTrackProgressUseCase

    val track = getCurrentTrackUseCase.execute()
    val musicState = getMusicStateUseCase.execute()
    val trackProgress = getTrackProgressUseCase.execute()

    fun updateState() {
        viewModelScope.launch {
            if (musicState.first() is MusicState.Pause)
                updateMusicStateUseCase.execute(MusicState.Resume)
            else
                updateMusicStateUseCase.execute(MusicState.Pause)
        }
    }

    fun nextTrack() {
        viewModelScope.launch {
            nextTrackUseCase.execute()
        }
    }

    fun prevTrack() {
        viewModelScope.launch {
            prevTrackUseCase.execute()
        }
    }

    class Factory @Inject constructor(
        private val musicPlayerUseCases: MusicPlayerUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MusicPlayerViewModel(musicPlayerUseCases) as T
        }
    }
}