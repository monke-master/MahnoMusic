package com.monke.machnomusic3.domain.usecase.post

import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.repository.PostRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadUserPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute() = withContext(Dispatchers.IO) {
        val user = userRepository.user.first()
            ?: return@withContext Result.failure(NotFoundException())
        postRepository.loadPosts(user)
    }
}