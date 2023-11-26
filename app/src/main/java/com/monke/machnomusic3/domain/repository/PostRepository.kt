package com.monke.machnomusic3.domain.repository

import android.net.Uri
import com.monke.machnomusic3.domain.model.Post
import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    val userPostsList: Flow<List<Post>>

    /**
     * Загружает пост на сервер
     */
    suspend fun uploadPost(
        post: Post,
        imagesUriList: List<Uri>
    ): Result<Any?>

    suspend fun loadPosts(
        user: User
    ): Result<Any?>

    suspend fun getImageUrl(imageId: String): Result<String?>

    suspend fun getPostsListByAuthor(author: User): Result<List<Post>>

}