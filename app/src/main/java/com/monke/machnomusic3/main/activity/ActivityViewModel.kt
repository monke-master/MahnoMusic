package com.monke.machnomusic3.main.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import javax.inject.Inject

class ActivityViewModel(
    activityUseCases: ActivityUseCases
): ViewModel() {


    private val getCurrentTrackUseCase = activityUseCases.getCurrentTrackUseCase
    private val getMusicStateUseCase = activityUseCases.getMusicStateUseCase

    val currentTrack = getCurrentTrackUseCase.execute()
    val musicState = getMusicStateUseCase.execute()


    class Factory @Inject constructor(
        private val activityUseCases: ActivityUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActivityViewModel(activityUseCases) as T
        }
    }
}