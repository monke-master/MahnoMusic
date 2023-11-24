package com.monke.machnomusic3.data.repository

import android.net.Uri
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.remote.POSTS_IMAGES_STORAGE
import com.monke.machnomusic3.data.remote.firestore.PostFirestore
import com.monke.machnomusic3.data.remote.storage.Storage
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Post
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@MainScope
class PostRepositoryImpl @Inject constructor(
    private val postFirestore: PostFirestore,
    private val storage: Storage
): PostRepository {

    override val userPostsList = MutableStateFlow<List<Post>>(emptyList())

    override suspend fun uploadPost(
        post: Post,
        imagesUriList: List<Uri>
    ): Result<Any?> {
        // Загрузка фотографий поста на сервер
        for (index in imagesUriList.indices) {
            val imageUri = imagesUriList[index]
            val imageId = post.imagesIdsList[index]
            val req = storage.uploadFileWithUri(imageUri, "$POSTS_IMAGES_STORAGE/$imageId")
            if (req.isFailure) return req
        }
        // Загрузка данных поста на сервер
        val response = postFirestore.setPost(post)
        if (response.isFailure) return response
        userPostsList.value = userPostsList.value.toMutableList().apply { add(post) }
        return response
    }

    override suspend fun loadPosts(user: User): Result<Any?> {
        val result = postFirestore.getPostsByAuthorId(user.id)
        val posts = result.getOrNull() ?: return result
        userPostsList.value = posts.map {
            if (it == null) return Result.failure(NotFoundException())
            it.toDomain(user)
        }
        return Result.success(null)
    }


    override suspend fun getImageUrl(imageId: String): Result<String?> {
        return storage.getDownloadUrl("$POSTS_IMAGES_STORAGE/$imageId")
    }
}