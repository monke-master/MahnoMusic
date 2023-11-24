package com.monke.machnomusic3.ui.userFeature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.post.GetPostImageUrlUseCase
import com.monke.machnomusic3.domain.usecase.post.GetPostItemUseCase
import com.monke.machnomusic3.domain.usecase.post.GetUserPostsUseCase
import com.monke.machnomusic3.domain.usecase.post.LoadUserPostsUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.domain.usecase.user.GetUserUseCase
import com.monke.machnomusic3.ui.uiModels.PostItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(
    profileUseCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getUserUseCase: GetUserUseCase,
        val loadUserPostsUseCase: LoadUserPostsUseCase,
        val getUserPostsUseCase: GetUserPostsUseCase,
        val getPostItemUseCase: GetPostItemUseCase
    )

    private val getUserUseCase = profileUseCases.getUserUseCase
    private val loadUserPostsUseCase = profileUseCases.loadUserPostsUseCase
    private val getUserPostsUseCase = profileUseCases.getUserPostsUseCase
    private val getPostItemUseCase = profileUseCases.getPostItemUseCase

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _postsList = MutableStateFlow<List<PostItem>>(emptyList())
    val postsList = _postsList.asStateFlow()

    val user = getUserUseCase.execute()

    init {
        loadPosts()
        collectPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            loadUserPostsUseCase.execute()
        }
    }

    private fun collectPosts() {
        viewModelScope.launch {
            getUserPostsUseCase.execute().collect { posts ->
                _uiState.value = UiState.Loading
                val postsList = ArrayList<PostItem>()
                for (post in posts) {
                    val response = getPostItemUseCase.execute(post)
                    if (response.isFailure) {
                        response.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                        return@collect
                    }
                    response.getOrNull()?.let { postsList.add(it) }
                }
                _postsList.value = postsList
                _uiState.value = UiState.Success()
            }
        }
    }


    class Factory @Inject constructor(
        private val profileUseCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(profileUseCases) as T
        }

    }
}