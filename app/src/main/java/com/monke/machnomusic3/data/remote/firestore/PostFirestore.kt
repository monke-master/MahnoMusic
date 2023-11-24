package com.monke.machnomusic3.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.monke.machnomusic3.data.extensions.toDomain
import com.monke.machnomusic3.data.extensions.toRemote
import com.monke.machnomusic3.data.remote.POSTS_COLLECTION
import com.monke.machnomusic3.data.remote.dto.AlbumRemote
import com.monke.machnomusic3.data.remote.dto.PostRemote
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.model.Post
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class PostFirestore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun setPost(post: Post): Result<Any?> {
        try {
            firestore.collection(POSTS_COLLECTION)
                .document(post.id)
                .set(post.toRemote())
                .await()
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }


    suspend fun getPostsByAuthorId(authorId: String): Result<List<PostRemote?>> {
        try {
            val response = firestore.collection(POSTS_COLLECTION)
                .whereEqualTo("authorId", authorId)
                .get()
                .await()
            return Result.success(response.documents.map { it.toObject<PostRemote>() })
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }


}