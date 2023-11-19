package com.monke.machnomusic3.main.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.NextTrackUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityViewModel(
    activityUseCases: ActivityUseCases
): ViewModel() {


    private val getCurrentTrackUseCase = activityUseCases.getCurrentTrackUseCase
    private val getMusicStateUseCase = activityUseCases.getMusicStateUseCase
    private val nextTrackUseCase = activityUseCases.nextTrackUseCase
    private val setTrackProgressUseCase = activityUseCases.setTrackProgressUseCase

    val currentTrack = getCurrentTrackUseCase.execute()
    val musicState = getMusicStateUseCase.execute()


    fun nextTrack() {
        nextTrackUseCase.execute()
    }

    fun setProgress(progress: Int) {
        setTrackProgressUseCase.execute(progress)
    }

    class Factory @Inject constructor(
        private val activityUseCases: ActivityUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActivityViewModel(activityUseCases) as T
        }
    }
}