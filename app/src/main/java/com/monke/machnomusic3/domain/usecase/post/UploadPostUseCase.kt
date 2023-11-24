package com.monke.machnomusic3.domain.usecase.post

import android.net.Uri
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Post
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.PostRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class UploadPostUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) {

    suspend fun execute(
        text: String?,
        imagesList: List<Uri>,
        tracksList: List<Track>
    ): Result<Any?> = withContext(Dispatchers.IO) {
        val author = userRepository.user.first() ?: return@withContext Result.failure(NotFoundException())
        val imagesIds = imagesList.map { UUID.randomUUID().toString() }
        val post = Post(
            id = UUID.randomUUID().toString(),
            author = author,
            text = text,
            tracksIdsList = tracksList.map { it.id },
            imagesIdsList = imagesIds,
            creationDate = Calendar.getInstance().timeInMillis
        )
        return@withContext postRepository.uploadPost(
            post = post,
            imagesUriList = imagesList
        )
    }
}