package com.monke.machnomusic3.ui.musicFeature.album

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.UploadingTrack
import com.monke.machnomusic3.domain.usecase.album.UploadAlbumUseCase
import com.monke.machnomusic3.domain.usecase.track.UploadTrackUseCase
import com.monke.machnomusic3.ui.musicFeature.track.UploadTrackViewModel
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.UUID
import javax.inject.Inject

class UploadAlbumViewModel(
    private val uploadAlbumUseCase: UploadAlbumUseCase
) : ViewModel() {

    private val _tracksList = MutableStateFlow<List<UploadingTrack>>(emptyList())
    val tracksList = _tracksList.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _coverUri = MutableStateFlow<Uri>(Uri.EMPTY)
    val coverUri = _coverUri.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _trackTitle = MutableStateFlow("")
    val trackTitle = _trackTitle.asStateFlow()


    init {
        Log.d("UploadAlbumViewModel", "init block")
    }

    fun addTrack(
        uri: Uri,
        duration: Int
    ) {
        _tracksList.value = _tracksList.value.toMutableList().apply {
            add(UploadingTrack(
                id = UUID.randomUUID().toString(),
                uri = uri,
                title = _trackTitle.value,
                duration = duration)
            )
        }
        _trackTitle.value = ""
    }


    fun setCoverUri(uri: Uri) {
        _coverUri.value = uri
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setTrackTitle(title: String) {
        _trackTitle.value = title
    }

    fun uploadAlbum() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = uploadAlbumUseCase.execute(
                uploadingTracksList = _tracksList.value,
                title = _title.value,
                coverUri = coverUri.value
            )
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }


    class Factory @Inject constructor(
        private val uploadAlbumUseCase: UploadAlbumUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UploadAlbumViewModel(
                uploadAlbumUseCase
            ) as T
        }
    }
}