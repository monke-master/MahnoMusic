package com.monke.machnomusic3.ui.userFeature.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.post.UploadPostUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackByIdUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadPostViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val uploadPostUseCase: UploadPostUseCase,
        val getTrackByIdUseCase: GetTrackByIdUseCase,
        val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase
    )

    private val uploadPostUseCase = useCases.uploadPostUseCase
    private val getTrackByIdUseCase = useCases.getTrackByIdUseCase
    private val getTrackCoverUrlUseCase = useCases.getTrackCoverUrlUseCase


    private val _imagesList = MutableStateFlow<List<Uri>>(emptyList())
    val imagesList = _imagesList.asStateFlow()

    private val _tracksList = MutableStateFlow<List<TrackItem>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    var text: String? = null

    fun addImage(uri: Uri) {
        _imagesList.value = _imagesList.value.toMutableList().apply { add(uri) }
    }

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
                _tracksList.value.toMutableList().apply { add(TrackItem(track, url))  }
        }
    }


    fun uploadPost() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = uploadPostUseCase.execute(
                text = text,
                imagesList = _imagesList.value,
                tracksList = _tracksList.value.map { it.track }
            )
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }


    class Factory @Inject constructor(
        private val useCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UploadPostViewModel(useCases) as T
        }

    }
}