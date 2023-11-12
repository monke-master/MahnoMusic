package com.monke.machnomusic3.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.PlayTrackListUseCase
import com.monke.machnomusic3.ui.musicFeature.music.MyMusicViewModel
import javax.inject.Inject

class ActivityViewModel(
    private val getCurrentTrackUseCase: GetCurrentTrackUseCase
): ViewModel() {


    val currentTrack = getCurrentTrackUseCase.execute()

    class Factory @Inject constructor(
        private val getCurrentTrackUseCase: GetCurrentTrackUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActivityViewModel(
                getCurrentTrackUseCase = getCurrentTrackUseCase
            ) as T
        }
    }
}