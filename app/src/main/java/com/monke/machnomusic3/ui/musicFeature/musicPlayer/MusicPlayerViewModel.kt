package com.monke.machnomusic3.ui.musicFeature.musicPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.model.TrackProgress
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
    private val setTrackProgressUseCase = musicPlayerUseCases.setTrackProgressUseCase

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

    fun setProgress(progress: Int) {
        setTrackProgressUseCase.execute(
            TrackProgress(
                progress = progress,
                changedFromUser = true
            )
        )
    }

    class Factory @Inject constructor(
        private val musicPlayerUseCases: MusicPlayerUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MusicPlayerViewModel(musicPlayerUseCases) as T
        }
    }
}