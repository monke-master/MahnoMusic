package com.monke.machnomusic3.ui.musicFeature.music.myMusic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.album.GetUserAlbumsUseCase
import com.monke.machnomusic3.domain.usecase.album.LoadAlbumsListUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.playlist.GetUserPlaylistsUseCase
import com.monke.machnomusic3.domain.usecase.playlist.LoadPlaylistsListUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.domain.usecase.track.GetUserTracksUseCase
import com.monke.machnomusic3.domain.usecase.track.LoadLikedTracksUseCase
import com.monke.machnomusic3.ui.uiModels.AlbumItem
import com.monke.machnomusic3.ui.uiModels.PlaylistItem
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyMusicViewModel(
    myMusicUseCases: UseCases
): ViewModel() {

    data class UseCases @Inject constructor(
        val playTrackListUseCase: PlayTrackListUseCase,
        val loadLikedTracksUseCase: LoadLikedTracksUseCase,
        val getUserTracksUseCase: GetUserTracksUseCase,
        val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase,
        val getUserAlbumsUseCase: GetUserAlbumsUseCase,
        val loadAlbumsListUseCase: LoadAlbumsListUseCase,
        val loadPlaylistsListUseCase: LoadPlaylistsListUseCase,
        val getUserPlaylistsUseCase: GetUserPlaylistsUseCase
    )

    // Tracks
    private val playTrackListUseCase = myMusicUseCases.playTrackListUseCase
    private val loadTrackUseCase = myMusicUseCases.loadLikedTracksUseCase
    private val getUserTracksUseCase = myMusicUseCases.getUserTracksUseCase
    private val getTrackCoverUrlUseCase = myMusicUseCases.getTrackCoverUrlUseCase
    // Albums
    private val getUserAlbumsUseCase = myMusicUseCases.getUserAlbumsUseCase
    private val loadAlbumsListUseCase = myMusicUseCases.loadAlbumsListUseCase
    // Playlists
    private val loadPlaylistsListUseCase = myMusicUseCases.loadPlaylistsListUseCase
    private val getUserPlaylistsUseCase = myMusicUseCases.getUserPlaylistsUseCase


    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _tracksList = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _albumsList = MutableStateFlow<List<AlbumItem>>(emptyList())
    val albumsList = _albumsList.asStateFlow()

    private val _playlistsList = MutableStateFlow<List<PlaylistItem>>(emptyList())
    val playlistsList = _playlistsList.asStateFlow()


    init {
        loadAlbums()
        loadPlaylists()
        loadTracks()
        collectAlbumsList()
        collectTracksList()
        collectPlaylists()
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

    private fun loadPlaylists() {
        // Получение плейлистов с сервера
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = loadPlaylistsListUseCase.execute()
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

    private fun collectPlaylists() {
        // Подписка на изменение списка плейлистов
        viewModelScope.launch {
            getUserPlaylistsUseCase.execute().collect { playlists ->
                val playlistItems = ArrayList<PlaylistItem>()
                for (playlist in playlists) {
                    val result = getTrackCoverUrlUseCase.execute(playlist.coverId)
                    result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                    result.getOrNull()?.let { url -> playlistItems.add(PlaylistItem(playlist, url)) }
                }
                _playlistsList.value = playlistItems
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
        private val myMusicUseCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyMusicViewModel(
                myMusicUseCases = myMusicUseCases
            ) as T
        }
    }

}