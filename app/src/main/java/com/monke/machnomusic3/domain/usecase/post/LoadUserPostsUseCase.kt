package com.monke.machnomusic3.domain.usecase.post

import com.monke.machnomusic3.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadUserPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend fun execute() = withContext(Dispatchers.IO) {
        postRepository.loadPosts()
    } 
}