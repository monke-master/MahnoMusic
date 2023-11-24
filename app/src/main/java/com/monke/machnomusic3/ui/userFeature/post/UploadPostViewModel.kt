package com.monke.machnomusic3.ui.userFeature.post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.post.UploadPostUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadPostViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val uploadPostUseCase: UploadPostUseCase
    )

    private val uploadPostUseCase = useCases.uploadPostUseCase


    private val _imagesList = MutableStateFlow<List<Uri>>(emptyList())
    val imagesList = _imagesList.asStateFlow()

    private val _tracksList = MutableStateFlow<List<Track>>(emptyList())
    val tracksList = _imagesList.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    var text: String? = null

    fun addImage(uri: Uri) {
        _imagesList.value = _imagesList.value.toMutableList().apply { add(uri) }
    }

    fun addTrack(track: Track) {
        _tracksList.value = _tracksList.value.toMutableList().apply { add(track) }
    }


    fun uploadPost() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = uploadPostUseCase.execute(text, _imagesList.value, _tracksList.value)
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