package com.monke.machnomusic3.ui.musicFeature.music.myMusic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.ui.uiModels.AlbumItem
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
    private val getUserAlbumsUseCase = myMusicUseCases.getUserAlbumsUseCase
    private val loadAlbumsListUseCase = myMusicUseCases.loadAlbumsListUseCase

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _tracksList = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _albumsList = MutableStateFlow<List<AlbumItem>>(emptyList())
    val albumsList = _albumsList.asStateFlow()


    init {
        loadAlbums()
        loadTracks()
        collectAlbumsList()
        collectTracksList()
    }

    private fun loadAlbums() {
        // Получение альбомов с сервера
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = loadAlbumsListUseCase.execute()
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }

    private fun loadTracks() {
        // Получение треков с сервера
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = loadTrackUseCase.execute()
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }

    private fun collectAlbumsList() {
        // Подписка на изменение списка альбомов
        viewModelScope.launch {
            getUserAlbumsUseCase.execute().collect { albums ->
                val albumsList = ArrayList<AlbumItem>()
                for (album in albums) {
                    val result = getTrackCoverUrlUseCase.execute(album.coverId)
                    result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                    result.getOrNull()?.let { url -> albumsList.add(AlbumItem(album, url)) }
                }
                _albumsList.value = albumsList
            }
        }
    }

    private fun collectTracksList() {
        // Подписка на изменение списка треков
        viewModelScope.launch {
            getUserTracksUseCase.execute().collect { tracks ->
                val tracksItems = ArrayList<TrackItem>()
                for (track in tracks) {
                    val result = getTrackCoverUrlUseCase.execute(track.coverId)
                    result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                    result.getOrNull()?.let { url -> tracksItems.add(TrackItem(track, url)) }
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