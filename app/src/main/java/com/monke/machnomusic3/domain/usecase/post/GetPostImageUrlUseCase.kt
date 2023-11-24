package com.monke.machnomusic3.domain.usecase.post

import com.monke.machnomusic3.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPostImageUrlUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend fun execute(imageId: String) = withContext(Dispatchers.IO) {
        postRepository.getImageUrl(imageId)
    }
}