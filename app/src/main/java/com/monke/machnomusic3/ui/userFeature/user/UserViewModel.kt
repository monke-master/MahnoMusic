package com.monke.machnomusic3.ui.userFeature.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.post.GetPostItemUseCase
import com.monke.machnomusic3.domain.usecase.post.GetPostsListByAuthorUseCase
import com.monke.machnomusic3.domain.usecase.user.FollowUserUseCase
import com.monke.machnomusic3.domain.usecase.user.GetProfilePicUrlUseCase
import com.monke.machnomusic3.domain.usecase.user.GetUserByIdUseCase
import com.monke.machnomusic3.domain.usecase.user.GetCurrentUserUseCase
import com.monke.machnomusic3.domain.usecase.user.UnfollowUserUseCase
import com.monke.machnomusic3.ui.uiModels.PostItem
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getCurrentUserUseCase: GetCurrentUserUseCase,
        val getUserById: GetUserByIdUseCase,
        val getPostsListByAuthorUseCase: GetPostsListByAuthorUseCase,
        val getPostItemUseCase: GetPostItemUseCase,
        val playTrackListUseCase: PlayTrackListUseCase,
        val getProfilePicUrlUseCase: GetProfilePicUrlUseCase,
        val followUserUseCase: FollowUserUseCase,
        val unfollowUserUseCase: UnfollowUserUseCase
    )

    private val getCurrentUserUseCase = useCases.getCurrentUserUseCase
    private val getUserByIdUseCase = useCases.getUserById
    private val getPostsListByAuthorUseCase= useCases.getPostsListByAuthorUseCase
    private val getPostItemUseCase = useCases.getPostItemUseCase
    private val playTrackListUseCase = useCases.playTrackListUseCase
    private val getProfilePicUrlUseCase = useCases.getProfilePicUrlUseCase
    private val followUserUseCase = useCases.followUserUseCase
    private val unfollowUserUseCase = useCases.unfollowUserUseCase

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _postsList = MutableStateFlow<List<PostItem>>(emptyList())
    val postsList = _postsList.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _pictureUrl = MutableStateFlow<String?>(null)
    val pictureUrl = _pictureUrl.asStateFlow()

    private val _isSubscription = MutableStateFlow<Boolean>(false)
    val isSubscription = _isSubscription.asStateFlow()

    private fun loadPicture(user: User) {
        viewModelScope.launch {
            val pictureId = user.profilePicId ?: return@launch

            _uiState.value = UiState.Loading
            val result = getProfilePicUrlUseCase.execute(pictureId)
            if (result.isFailure) {
                result.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }

            _pictureUrl.value = result.getOrNull()
        }
    }

    fun loadUserData(userId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = getUserByIdUseCase.execute(userId)
            if (response.isFailure) {
                response.exceptionOrNull()?.let { _uiState.value = UiState.Error(it) }
                return@launch
            }
            _user.value = response.getOrNull()
            _user.value?.let { user ->
                loadPicture(user)
                loadPosts(user)
                getSubscriptionData(user)
            }
        }
    }

    fun changeSubscriptionStatus() {
        viewModelScope.launch {
            _isSubscription.value = !_isSubscription.value
            val currentUser = getCurrentUserUseCase.execute().first() ?: return@launch
            val user = user.value ?: return@launch
            if (isSubscription.value) {
                followUserUseCase.execute(
                    user = user,
                    follower = currentUser
                )
            } else {
                unfollowUserUseCase.execute(
                    user = user,
                    follower = currentUser
                )
            }
        }

    }

    private fun getSubscriptionData(user: User) {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase.execute().first() ?: return@launch
            _isSubscription.value = currentUser.subscriptionsIdsList.contains(user.id)
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

    fun playTrackList(
        trackList: List<Track>,
        index: Int
    ) {
        playTrackListUseCase.execute(trackList, index)
    }


    class Factory @Inject constructor(
        private val profileUseCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(profileUseCases) as T
        }

    }
}