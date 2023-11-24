package com.monke.machnomusic3.ui.userFeature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.usecase.user.GetUserUseCase
import javax.inject.Inject

class ProfileViewModel(
    profileUseCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getUserUseCase: GetUserUseCase
    )

    private val getUserUseCase = profileUseCases.getUserUseCase


    val user = getUserUseCase.execute()


    class Factory @Inject constructor(
        private val profileUseCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(profileUseCases) as T
        }

    }
}