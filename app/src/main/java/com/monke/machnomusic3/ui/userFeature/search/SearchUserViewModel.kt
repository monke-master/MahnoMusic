package com.monke.machnomusic3.ui.userFeature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.music.SearchMusicUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.domain.usecase.user.SearchUserUseCase
import com.monke.machnomusic3.ui.uiModels.TrackItem
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.uiModels.UserItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchUserViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val searchUserUseCase: SearchUserUseCase,
    )

    private val searchUserUseCase = useCases.searchUserUseCase

    private val _usersList = MutableStateFlow<List<UserItem>>(emptyList())
    val usersList = _usersList.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    var query = ""


    fun search(query: String) {
        if (query.isEmpty()) return
        this.query = query
        viewModelScope.launch {
            val result = searchUserUseCase.execute(query)
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _usersList.value = result.getOrNull()?.map {
                UserItem(
                    user = it,
                    profilePicUrl = ""
                )
            } ?: emptyList()
        }
    }

    class Factory @Inject constructor(
        private val useCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchUserViewModel(useCases) as T
        }
    }
}