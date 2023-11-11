package com.monke.machnomusic3.ui.signUpFeature.email

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.usecase.email.GetConfirmationStatusUseCase
import com.monke.machnomusic3.domain.usecase.email.SaveEmailUseCase
import com.monke.machnomusic3.domain.usecase.email.SendConfirmationLetterUseCase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
class EmailViewModel(
    private val saveEmailUseCase: SaveEmailUseCase,
    private val sendConfirmationLetterUseCase: SendConfirmationLetterUseCase,
    private val getConfirmationStatusUseCase: GetConfirmationStatusUseCase,
) : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    var emailConfirmed = getConfirmationStatusUseCase.execute()

    init {
        Log.d("EmailViewModel", "init block")
    }

    fun setEmail(emailAddress: String) {
        _email.value = emailAddress
    }

    fun saveEmail() {
        saveEmailUseCase.execute(_email.value)
        viewModelScope.launch {
            sendConfirmationLetterUseCase.execute()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("EmailViewModel", "is cleared")
    }

    class Factory @Inject constructor(
        private val saveEmailUseCase: SaveEmailUseCase,
        private val sendConfirmationLetterUseCase: SendConfirmationLetterUseCase,
        private val getConfirmationStatusUseCase: GetConfirmationStatusUseCase,
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EmailViewModel(
                saveEmailUseCase = saveEmailUseCase,
                sendConfirmationLetterUseCase = sendConfirmationLetterUseCase,
                getConfirmationStatusUseCase = getConfirmationStatusUseCase,
            ) as T
        }

    }

}