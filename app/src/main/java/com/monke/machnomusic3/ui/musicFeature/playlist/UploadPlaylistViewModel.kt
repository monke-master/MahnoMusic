package com.monke.machnomusic3.ui.musicFeature.playlist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.UploadingTrack
import com.monke.machnomusic3.domain.usecase.playlist.UploadPlaylistUseCase
import com.monke.machnomusic3.domain.usecase.post.UploadPostUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackByIdUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.userFeature.post.UploadPostViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class UploadPlaylistViewModel @Inject constructor(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val uploadPlaylistUseCase: UploadPlaylistUseCase,
        val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase,
        val getTrackByIdUseCase: GetTrackByIdUseCase
    )

    private val uploadPlaylistUseCase = useCases.uploadPlaylistUseCase
    private val getTrackCoverUrlUseCase = useCases.getTrackCoverUrlUseCase
    private val getTrackByIdUseCase = useCases.getTrackByIdUseCase

    private val _coverUri = MutableStateFlow<Uri>(Uri.EMPTY)
    val coverUri = _coverUri.asStateFlow()

    private val _tracksList = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow<String?>(null)
    val description = _description.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    var text: String? = null

    fun addTrack(trackId: String) {
        viewModelScope.launch {
            val result = getTrackByIdUseCase.execute(trackId)
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            result.getOrNull()?.let { track -> loadTrack(track) }
        }
    }

    private suspend fun loadTrack(track: Track) {
        val result = getTrackCoverUrlUseCase.execute(track.coverId)
        result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
        result.getOrNull()?.let { url ->
            _tracksList.value =
                _tracksList.value.toMutableList().apply { add(TrackItem(track, url)) }
        }
    }

    fun uploadPlaylist() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = uploadPlaylistUseCase.execute(
                tracksIdsList = _tracksList.value.map { it.track.id },
                title = _title.value,
                coverUri = _coverUri.value,
                description = _description.value
            )
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }

    fun setCoverUri(uri: Uri) {
        _coverUri.value = uri
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setDescription(description: String?) {
        _description.value = description
    }



    class Factory @Inject constructor(
        private val useCases: UseCases
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UploadPlaylistViewModel(useCases) as T
        }

    }

}