package com.monke.machnomusic3.ui.userFeature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ProfileViewModel(
    profileUseCases: ProfileUseCases
) : ViewModel() {

    private val getUserUseCase = profileUseCases.getUserUseCase




    class Factory @Inject constructor(
        private val profileUseCases: ProfileUseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(profileUseCases) as T
        }

    }
}