package com.monke.machnomusic3.ui.musicFeature.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.music.SearchMusicUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.domain.usecase.track.UploadTrackUseCase
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectTracksViewModel(
    useCases: UseCases
) : ViewModel() {

    private val searchMusicUseCase = useCases.searchMusicUseCase
    private val getTrackCoverUrlUseCase = useCases.getTrackCoverUrlUseCase

    private val _tracksList = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    var query = ""

    data class UseCases @Inject constructor(
        val searchMusicUseCase: SearchMusicUseCase,
        val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase
    )

    private suspend fun loadTracks(tracksList: List<Track>) {
        val tracksItems = ArrayList<TrackItem>()
        for (track in tracksList) {
            val result = getTrackCoverUrlUseCase.execute(track.coverId)
            result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
            result.getOrNull()?.let { url -> tracksItems.add(TrackItem(track, url)) }
        }
        _tracksList.value = tracksItems
    }

    fun search(query: String) {
        if (query.isEmpty()) return
        this.query = query
        viewModelScope.launch {
            val result = searchMusicUseCase.execute(query)
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            loadTracks(result.getOrNull() ?: emptyList())
        }
    }




    class Factory @Inject constructor(
        private val useCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SelectTracksViewModel(useCases) as T
        }
    }
}