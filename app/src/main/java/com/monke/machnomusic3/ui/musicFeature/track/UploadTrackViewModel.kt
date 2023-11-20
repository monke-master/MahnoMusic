package com.monke.machnomusic3.ui.musicFeature.track

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.track.UploadTrackUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadTrackViewModel(
    private val uploadTrackUseCase: UploadTrackUseCase
) : ViewModel() {


    private val _trackUri = MutableStateFlow<Uri>(Uri.EMPTY)
    val trackUri = _trackUri.asStateFlow()

    private val _coverUri = MutableStateFlow<Uri?>(null)
    val coverUri = _coverUri.asStateFlow()

    private val _trackTitle = MutableStateFlow("")
    val trackTitle = _trackTitle.asStateFlow()

    var trackDuration: Int = 0

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    fun setTrackUri(uri: Uri) {
        _trackUri.value = uri
    }

    fun setCoverUri(uri: Uri) {
        _coverUri.value = uri
    }

    fun setTrackTitle(title: String) {
        _trackTitle.value = title
    }


    fun uploadTrack() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = uploadTrackUseCase.execute(
                trackUri = trackUri.value,
                title = trackTitle.value,
                coverUri = coverUri.value,
                duration = trackDuration
            )
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }


    class Factory @Inject constructor(
        private val uploadTrackUseCase: UploadTrackUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UploadTrackViewModel(
                uploadTrackUseCase = uploadTrackUseCase
            ) as T
        }
    }


}