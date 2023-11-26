package com.monke.machnomusic3.domain.usecase.post

import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPostsListByAuthorUseCase @Inject constructor(
    private val postRepository: PostRepository
) {


    suspend fun execute(
        author: User
    ) = withContext(Dispatchers.IO) {
        postRepository.getPostsListByAuthor(author)
    }


}