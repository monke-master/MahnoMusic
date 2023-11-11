package com.monke.machnomusic3.ui.signUpFeature.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.usecase.email.GetConfirmationStatusUseCase
import com.monke.machnomusic3.domain.usecase.email.SaveEmailUseCase
import com.monke.machnomusic3.domain.usecase.email.SendConfirmationLetterUseCase
import com.monke.machnomusic3.domain.usecase.login.SaveLoginUseCase
import com.monke.machnomusic3.domain.usecase.username.SaveUsernameUseCase
import com.monke.machnomusic3.ui.signUpFeature.email.EmailViewModel
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UsernameViewModel(
    private val saveLoginUseCase: SaveLoginUseCase,
    private val saveUsernameUseCase: SaveUsernameUseCase
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setLogin(login: String) {
        _login.value = login
    }

    fun saveData() {
        saveLoginUseCase.execute(login.value)
        saveUsernameUseCase.execute(username.value)
    }


    class Factory @Inject constructor(
        private val saveLoginUseCase: SaveLoginUseCase,
        private val saveUsernameUseCase: SaveUsernameUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UsernameViewModel(
                saveLoginUseCase = saveLoginUseCase,
                saveUsernameUseCase = saveUsernameUseCase
            ) as T
        }

    }
}