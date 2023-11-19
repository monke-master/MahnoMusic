package com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.MusicPlayerUseCases
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class MiniPlayerViewModel(
    musicPlayerUseCases: MusicPlayerUseCases
): ViewModel() {

    private val getCurrentTrackUseCase = musicPlayerUseCases.getCurrentTrackUseCase
    private val getMusicStateUseCase = musicPlayerUseCases.getMusicStateUseCase
    private val updateMusicStateUseCase = musicPlayerUseCases.updateMusicStateUseCase

    val track = getCurrentTrackUseCase.execute()
    val musicState = getMusicStateUseCase.execute()


    fun updateState() {
        viewModelScope.launch {
            if (musicState.first() is MusicState.Pause)
                updateMusicStateUseCase.execute(MusicState.Resume)
            else
                updateMusicStateUseCase.execute(MusicState.Pause)
        }
    }


    class Factory @Inject constructor(
        private val musicPlayerUseCases: MusicPlayerUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MiniPlayerViewModel(musicPlayerUseCases) as T
        }
    }
}