package com.monke.machnomusic3.ui.signUpFeature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.usecase.password.IsPasswordValidUseCase
import com.monke.machnomusic3.domain.usecase.password.SavePasswordUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.triviamasters.domain.useCases.user.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

class PasswordViewModel @Inject constructor(
    private val savePasswordUseCase: SavePasswordUseCase,
    private val isPasswordValidUseCase: IsPasswordValidUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _repeatedPassword = MutableStateFlow("")
    val repeatedPassword = _repeatedPassword.asStateFlow()

    private val _isPasswordValid = MutableStateFlow(false)
    val isPasswordValid = _isPasswordValid.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    init {
        Log.d("PasswordViewModel", "init block")
    }

    fun setPassword(password: String) {
        _password.value = password
        _isPasswordValid.value =
            isPasswordValidUseCase.execute(_password.value) &&
                    _repeatedPassword.value == _password.value

    }

    fun setRepeatedPassword(repeatedPassword: String) {
        _repeatedPassword.value = repeatedPassword
        _isPasswordValid.value =
            isPasswordValidUseCase.execute(_password.value) &&
                    repeatedPassword == _password.value
    }

    fun savePassword() {
        savePasswordUseCase.execute(_password.value)
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = signUpUseCase.execute()
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("PasswordViewModel", "is cleared")
    }

    class Factory @Inject constructor(
        private val savePasswordUseCase: SavePasswordUseCase,
        private val isPasswordValidUseCase: IsPasswordValidUseCase,
        private val signUpUseCase: SignUpUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PasswordViewModel(
                savePasswordUseCase = savePasswordUseCase,
                isPasswordValidUseCase = isPasswordValidUseCase,
                signUpUseCase = signUpUseCase
            ) as T
        }
    }

}