package com.monke.machnomusic3.main.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.TrackProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityViewModel(
    activityUseCases: ActivityUseCases
): ViewModel() {


    private val getCurrentTrackUseCase = activityUseCases.getCurrentTrackUseCase
    private val getMusicStateUseCase = activityUseCases.getMusicStateUseCase
    private val nextTrackUseCase = activityUseCases.nextTrackUseCase
    private val setTrackProgressUseCase = activityUseCases.setTrackProgressUseCase
    private val getTrackProgressUseCase = activityUseCases.getTrackProgressUseCase
    private val getTrackUrlUseCase = activityUseCases.getTrackUrlUseCase

    val currentTrack = getCurrentTrackUseCase.execute()
    val musicState = getMusicStateUseCase.execute()
    val trackProgress = getTrackProgressUseCase.execute()

    private val _trackUrl = MutableStateFlow<String?>(null)
    val trackUrl = _trackUrl.asStateFlow()


    fun nextTrack() {
        nextTrackUseCase.execute()
    }

    fun setProgress(progress: Int) {
        setTrackProgressUseCase.execute(TrackProgress(progress))
    }

    fun getTrackUrl(trackId: String) {
        viewModelScope.launch {
            _trackUrl.value = getTrackUrlUseCase.execute(trackId).getOrNull()
        }
    }

    class Factory @Inject constructor(
        private val activityUseCases: ActivityUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActivityViewModel(activityUseCases) as T
        }
    }
}