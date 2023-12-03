package com.monke.machnomusic3.ui.musicFeature.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Playlist
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.playlist.GetLikedPlaylistsByIdUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackByIdUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getLikedPlaylistByIdUseCase: GetLikedPlaylistsByIdUseCase,
        val getTrackByIdUseCase: GetTrackByIdUseCase,
        val playTrackListUseCase: PlayTrackListUseCase,
        val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase,
    )

    private val getLikedPlaylistByIdUseCase = useCases.getLikedPlaylistByIdUseCase
    private val getTrackByIdUseCase = useCases.getTrackByIdUseCase
    private val playTrackListUseCase = useCases.playTrackListUseCase
    private val getTrackCoverUrlUseCase = useCases.getTrackCoverUrlUseCase


    private val _tracksList = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist = _playlist.asStateFlow()

    private val _cover = MutableStateFlow<String?>(null)
    val cover = _cover.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private suspend fun loadTracks(playlist: Playlist) {
        _uiState.value = UiState.Loading
        val trackItems = ArrayList<TrackItem>()
        for (trackId in playlist.tracksIdsList) {
            // Поиск трека по id
            val result = getTrackByIdUseCase.execute(trackId)
            val track = result.getOrNull()
            if (result.isFailure || track == null) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return
            }
            // Получение обложки трека
            val url = getTrackCoverUrlUseCase.execute(track.coverId).getOrNull()
            if (url == null) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return
            }
            trackItems.add(TrackItem(track, url))
        }
        _tracksList.value = trackItems
        _uiState.value = UiState.Success()
    }

    private suspend fun loadCover(id: String) {
        val result = getTrackCoverUrlUseCase.execute(id)
        if (result.isFailure) {
            result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
            return
        }
        _cover.value = result.getOrNull()
    }

    fun loadPlaylist(id: String) {
        viewModelScope.launch {
            _playlist.value = getLikedPlaylistByIdUseCase.execute(id)
            _playlist.value?.let { playlist ->
                loadTracks(playlist)
                loadCover(playlist.coverId)
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
        private val useCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaylistViewModel(useCases) as T
        }
    }
}