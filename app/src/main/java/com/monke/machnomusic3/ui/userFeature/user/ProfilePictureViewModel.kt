package com.monke.machnomusic3.ui.userFeature.user

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.usecase.user.GetProfilePicUrlUseCase
import com.monke.machnomusic3.domain.usecase.user.GetCurrentUserUseCase
import com.monke.machnomusic3.domain.usecase.user.UpdateProfilePictureUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfilePictureViewModel(
    useCases: UseCases
): ViewModel() {

    data class UseCases @Inject constructor(
        val getCurrentUserUseCase: GetCurrentUserUseCase,
        val updateProfilePictureUseCase: UpdateProfilePictureUseCase,
        val getProfilePicUrlUseCase: GetProfilePicUrlUseCase
    )

    private val getUserUseCase = useCases.getCurrentUserUseCase
    private val updateProfilePictureUseCase = useCases.updateProfilePictureUseCase
    private val getProfilePicUrlUseCase = useCases.getProfilePicUrlUseCase

    val user = getUserUseCase.execute()

    private val _profilePicture = MutableStateFlow<Uri?>(null)
    val profilePicture = _profilePicture.asStateFlow()

    private val _pictureUrl = MutableStateFlow<String?>(null)
    val pictureUrl = _pictureUrl.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()


    init {
        loadPicture()
    }

    private fun loadPicture() {
        viewModelScope.launch {
            val pictureId = user.first()?.profilePicId ?: return@launch

            val result = getProfilePicUrlUseCase.execute(pictureId)
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }

            _pictureUrl.value = result.getOrNull()
        }
    }

    fun setProfilePicture(uri: Uri) {
        _profilePicture.value = uri
    }

    fun save() {
        viewModelScope.launch {
            val uri = _profilePicture.value ?: return@launch
            _uiState.value = UiState.Loading
            val result = updateProfilePictureUseCase.execute(uri)
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
            return ProfilePictureViewModel(useCases) as T
        }

    }
}