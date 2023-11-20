package com.monke.machnomusic3.main.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.model.TrackProgress
import javax.inject.Inject

class ActivityViewModel(
    activityUseCases: ActivityUseCases
): ViewModel() {


    private val getCurrentTrackUseCase = activityUseCases.getCurrentTrackUseCase
    private val getMusicStateUseCase = activityUseCases.getMusicStateUseCase
    private val nextTrackUseCase = activityUseCases.nextTrackUseCase
    private val setTrackProgressUseCase = activityUseCases.setTrackProgressUseCase
    private val getTrackProgressUseCase = activityUseCases.getTrackProgressUseCase

    val currentTrack = getCurrentTrackUseCase.execute()
    val musicState = getMusicStateUseCase.execute()
    val trackProgress = getTrackProgressUseCase.execute()


    fun nextTrack() {
        nextTrackUseCase.execute()
    }

    fun setProgress(progress: Int) {
        setTrackProgressUseCase.execute(TrackProgress(progress))
    }

    class Factory @Inject constructor(
        private val activityUseCases: ActivityUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActivityViewModel(activityUseCases) as T
        }
    }
}