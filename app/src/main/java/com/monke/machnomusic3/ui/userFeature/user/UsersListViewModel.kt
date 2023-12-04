package com.monke.machnomusic3.ui.userFeature.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.usecase.user.GetUserByIdUseCase
import com.monke.machnomusic3.domain.usecase.user.GetUserItemUseCase
import com.monke.machnomusic3.domain.usecase.user.GetUserItemsListUseCase
import com.monke.machnomusic3.domain.usecase.user.SearchUserUseCase
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.uiModels.UserItem
import com.monke.machnomusic3.ui.userFeature.search.SearchUserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsersListViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getUserItemsListUseCase: GetUserItemsListUseCase
    )

    private val getUserItemsListUseCase = useCases.getUserItemsListUseCase

    private val _usersList = MutableStateFlow<List<UserItem>>(emptyList())
    val usersList = _usersList.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    fun loadUsers(idsList: List<String>) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getUserItemsListUseCase.execute(idsList)
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _usersList.value = result.getOrNull() ?: emptyList()
            _uiState.value = UiState.Success()
        }
    }


    class Factory @Inject constructor(
        private val useCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UsersListViewModel(useCases) as T
        }
    }
}