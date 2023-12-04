package com.monke.machnomusic3.domain.usecase.post

import android.content.res.Resources.NotFoundException
import com.monke.machnomusic3.domain.model.Post
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.PostRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNewPostsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) {

    suspend fun execute(
        currentUser: User
    ): Result<List<Post>> = withContext(Dispatchers.IO) {
        val postsList = ArrayList<Post>()
        for (id in currentUser.subscriptionsIdsList) {
            val author = userRepository.getUserById(id).getOrNull()
                ?: return@withContext Result.failure(NotFoundException())
            val posts = postRepository.getPostsListByAuthor(author).getOrNull()
                ?: return@withContext Result.failure(NotFoundException())
            postsList += posts
        }
        return@withContext Result.success(postsList.sortedByDescending { it.creationDate })

    }
}