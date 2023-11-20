package com.monke.machnomusic3.ui.musicFeature.music.myMusic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.track.GetUserTracksUseCase
import com.monke.machnomusic3.domain.usecase.track.LoadTrackUseCase
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyMusicViewModel(
    myMusicUseCases: MyMusicUseCases
): ViewModel() {

    private val playTrackListUseCase = myMusicUseCases.playTrackListUseCase
    private val loadTrackUseCase = myMusicUseCases.loadTrackUseCase
    private val getUserTracksUseCase = myMusicUseCases.getUserTracksUseCase
    private val getTrackCoverUrlUseCase = myMusicUseCases.getTrackCoverUrlUseCase

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _tracksList = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracksList = _tracksList.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = loadTrackUseCase.execute()
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
        viewModelScope.launch {
            getUserTracksUseCase.execute().collect { tracks ->
                val tracksItems = ArrayList<TrackItem>()
                for (track in tracks) {
                    val result = getTrackCoverUrlUseCase.execute(track.coverId)
                    result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                    result.getOrNull()?.let { tracksItems.add(TrackItem(track, it)) }
                }
                _tracksList.value = tracksItems
            }
        }
    }


    fun playTrackList(
        trackList: List<Track>,
        index: Int
    ) {
        playTrackListUseCase.execute(trackList, index)
    }

    class Factory @Inject constructor(
        private val myMusicUseCases: MyMusicUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyMusicViewModel(
                myMusicUseCases = myMusicUseCases
            ) as T
        }
    }

}