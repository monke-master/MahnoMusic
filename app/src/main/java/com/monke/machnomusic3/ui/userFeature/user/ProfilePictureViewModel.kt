package com.monke.machnomusic3.ui.userFeature.user

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.usecase.user.GetUserByIdUseCase
import com.monke.machnomusic3.domain.usecase.user.GetUserUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfilePictureViewModel(
    useCases: UseCases
): ViewModel() {

    data class UseCases @Inject constructor(
        val getUserUseCase: GetUserUseCase
    )

    private val getUserUseCase = useCases.getUserUseCase

    val user = getUserUseCase.execute()

    private val _profilePicture = MutableStateFlow<Uri?>(null)
    val profilePicture = _profilePicture.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    fun setProfilePicture(uri: Uri) {
        _profilePicture.value = uri
    }

    fun save() {
        viewModelScope.launch {

        }
    }

    class Factory @Inject constructor(
        private val useCases: ProfilePictureViewModel.UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfilePictureViewModel(useCases) as T
        }

    }
}