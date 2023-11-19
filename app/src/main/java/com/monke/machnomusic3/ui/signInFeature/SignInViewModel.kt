package com.monke.machnomusic3.ui.signInFeature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.usecase.email.IsEmailValidUseCase
import com.monke.machnomusic3.domain.usecase.password.IsPasswordValidUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.triviamasters.domain.useCases.user.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val isEmailValidUseCase: IsEmailValidUseCase,
    private val isPasswordValidUseCase: IsPasswordValidUseCase
): ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _isDataValid = MutableStateFlow(false)
    val isDataValid = _isDataValid.asStateFlow()

    init {
        Log.d("SignInViewModel", "init block")
    }

    fun setEmail(emailAddress: String) {
        _email.value = emailAddress
        _isDataValid.value =
            isEmailValidUseCase.execute(emailAddress) &&
                    isPasswordValidUseCase.execute(password.value)
    }

    fun setPassword(password: String) {
        _password.value = password
        _isDataValid.value =
            isEmailValidUseCase.execute(email.value) &&
                    isPasswordValidUseCase.execute(password)
    }

    fun signIn() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val result = signInUseCase.execute(email = _email.value, password = _password.value)
            if (result.isFailure) {
                result.exceptionOrNull()?.let {
                    _uiState.value = UiState.Error(it)
                }
                return@launch
            }
            _uiState.value = UiState.Success()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SignInViewModel", "is cleared")
    }

    class Factory @Inject constructor(
        private val signInUseCase: SignInUseCase,
        private val isEmailValidUseCase: IsEmailValidUseCase,
        private val isPasswordValidUseCase: IsPasswordValidUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignInViewModel(
                signInUseCase = signInUseCase,
                isEmailValidUseCase = isEmailValidUseCase,
                isPasswordValidUseCase = isPasswordValidUseCase
            ) as T
        }
    }


}