package com.monke.machnomusic3.ui.musicFeature.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.album.GetLikedAlbumByIdUseCase
import com.monke.machnomusic3.domain.usecase.album.UploadAlbumUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackByIdUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



class AlbumViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getLikedAlbumByIdUseCase: GetLikedAlbumByIdUseCase,
        val getTrackByIdUseCase: GetTrackByIdUseCase,
        val playTrackListUseCase: PlayTrackListUseCase,
        val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase
    )

    private val getLikedAlbumByIdUseCase = useCases.getLikedAlbumByIdUseCase
    private val getTrackByIdUseCase = useCases.getTrackByIdUseCase
    private val playTrackListUseCase = useCases.playTrackListUseCase
    private val getTrackCoverUrlUseCase = useCases.getTrackCoverUrlUseCase


    private val _tracksList = MutableStateFlow<List<Track>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _album = MutableStateFlow<Album?>(null)
    val album = _album.asStateFlow()

    private val _cover = MutableStateFlow<String?>(null)
    val cover = _cover.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private suspend fun loadTracks(album: Album) {
        _uiState.value = UiState.Loading
        val tracks = ArrayList<Track>()
        for (trackId in album.tracksIdsList) {
            val result = getTrackByIdUseCase.execute(trackId)
            val track = result.getOrNull()
            if (result.isFailure || track == null) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return
            }
            tracks.add(track)
        }
        _tracksList.value = tracks
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

    fun loadAlbum(id: String) {
        viewModelScope.launch {
            _album.value = getLikedAlbumByIdUseCase.execute(id)
            _album.value?.let { album ->
                loadTracks(album)
                loadCover(album.coverId)
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
            return AlbumViewModel(
                useCases
            ) as T
        }
    }
}