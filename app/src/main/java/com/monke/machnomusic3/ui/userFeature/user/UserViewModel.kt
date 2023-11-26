package com.monke.machnomusic3.ui.userFeature.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.usecase.post.GetPostItemUseCase
import com.monke.machnomusic3.domain.usecase.post.GetPostsListByAuthorUseCase
import com.monke.machnomusic3.domain.usecase.user.GetUserByIdUseCase
import com.monke.machnomusic3.ui.uiModels.PostItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getUserById: GetUserByIdUseCase,
        val getPostsListByAuthorUseCase: GetPostsListByAuthorUseCase,
        val getPostItemUseCase: GetPostItemUseCase
    )

    private val getUserByIdUseCase = useCases.getUserById
    private val getPostsListByAuthorUseCase= useCases.getPostsListByAuthorUseCase
    private val getPostItemUseCase = useCases.getPostItemUseCase

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _postsList = MutableStateFlow<List<PostItem>>(emptyList())
    val postsList = _postsList.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()



    fun loadUserData(userId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = getUserByIdUseCase.execute(userId)
            if (response.isFailure) {
                response.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _user.value = response.getOrNull()
            _user.value?.let { user -> loadPosts(user) }
        }
    }


    private fun loadPosts(user: User) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            // Получение списка постов пользователя
            val postsResult = getPostsListByAuthorUseCase.execute(user)
            if (postsResult.isFailure) {
                postsResult.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }

            // Получение прилагаемого контента поста
            val postsItemsList = ArrayList<PostItem>()
            val postsList = postsResult.getOrNull() ?: emptyList()
            for (post in postsList) {
                val postItemResult = getPostItemUseCase.execute(post)
                if (postItemResult.isFailure) {
                    postItemResult.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                    return@launch
                }
                postItemResult.getOrNull()?.let { postsItemsList.add(it) }
            }
            _postsList.value = postsItemsList
            _uiState.value = UiState.Success()
        }
    }


    class Factory @Inject constructor(
        private val profileUseCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(profileUseCases) as T
        }

    }
}