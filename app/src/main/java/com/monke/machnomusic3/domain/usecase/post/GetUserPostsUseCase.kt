package com.monke.machnomusic3.domain.usecase.post

import com.monke.machnomusic3.domain.repository.PostRepository
import javax.inject.Inject

class GetUserPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    fun execute() = postRepository.userPostsList

}