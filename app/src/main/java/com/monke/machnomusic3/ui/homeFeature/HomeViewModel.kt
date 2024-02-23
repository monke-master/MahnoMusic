package com.monke.machnomusic3.ui.homeFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.post.GetNewPostsUseCase
import com.monke.machnomusic3.domain.usecase.post.GetPostItemUseCase
import com.monke.machnomusic3.domain.usecase.user.GetCurrentUserUseCase
import com.monke.machnomusic3.ui.uiModels.PostItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(
    useCases: UseCases
) : ViewModel() {


    data class UseCases @Inject constructor(
        val getCurrentUserUseCase: GetCurrentUserUseCase,
        val getNewPostsUseCase: GetNewPostsUseCase,
        val getPostItemUseCase: GetPostItemUseCase,
        val playTrackListUseCase: PlayTrackListUseCase,
    )

    private val getCurrentUserUseCase = useCases.getCurrentUserUseCase
    private val getNewPostsUseCase = useCases.getNewPostsUseCase
    private val getPostItemUseCase = useCases.getPostItemUseCase
    private val playTrackListUseCase = useCases.playTrackListUseCase

    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    val posts = _posts.asStateFlow()

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase.execute().collect { user ->
                user?.let { collectPosts(it) }
            }
        }
    }

    private suspend fun collectPosts(user: User) {
        _uiState.value = UiState.Loading
        val result = getNewPostsUseCase.execute(user)
        if (result.isFailure) {
            result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
            return
        }
        val postsList = result.getOrNull() ?: return
        val postsItems = ArrayList<PostItem>()
        for (post in postsList) {
            val itemResult = getPostItemUseCase.execute(post)
            if (itemResult.isFailure) {
                itemResult.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return
            }
            val postItem = itemResult.getOrNull() ?: return
            postsItems += postItem
        }
        _posts.value = postsItems
        _uiState.value = UiState.Success()
    }

    fun playTrackList(
        trackList: List<Track>,
        index: Int
    ) {
        playTrackListUseCase.execute(trackList, index)
    }

    class Factory @Inject constructor(
        private val useCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(useCases) as T
        }

    }
}
