package com.monke.machnomusic3.ui.musicFeature.musicPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import javax.inject.Inject

class MiniPlayerViewModel(
    private val getCurrentTrackUseCase: GetCurrentTrackUseCase
): ViewModel() {

    val track = getCurrentTrackUseCase.execute()

    class Factory @Inject constructor(
        private val getCurrentTrackUseCase: GetCurrentTrackUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MiniPlayerViewModel(getCurrentTrackUseCase) as T
        }
    }
}