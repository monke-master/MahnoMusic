package com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.GetMusicStateUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class MiniPlayerViewModel(
    miniPlayerUseCases: MiniPlayerUseCases
): ViewModel() {

    private val getCurrentTrackUseCase = miniPlayerUseCases.getCurrentTrackUseCase
    private val getMusicStateUseCase = miniPlayerUseCases.getMusicStateUseCase
    private val updateMusicStateUseCase = miniPlayerUseCases.updateMusicStateUseCase

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
        private val miniPlayerUseCases: MiniPlayerUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MiniPlayerViewModel(miniPlayerUseCases) as T
        }
    }
}