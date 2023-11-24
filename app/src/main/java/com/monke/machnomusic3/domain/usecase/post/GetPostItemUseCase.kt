package com.monke.machnomusic3.domain.usecase.post

import com.monke.machnomusic3.domain.model.Post
import com.monke.machnomusic3.domain.usecase.track.GetTrackByIdUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.ui.uiModels.PostItem
import com.monke.machnomusic3.ui.uiModels.TrackItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPostItemUseCase @Inject constructor(
    private val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase,
    private val getPostImageUrlUseCase: GetPostImageUrlUseCase,
    private val getTrackByIdUseCase: GetTrackByIdUseCase
) {

    suspend fun execute(
        post: Post
    ): Result<PostItem> = withContext(Dispatchers.IO) {
        val imagesUrlsList = ArrayList<String>()
        for (imageId in post.imagesIdsList) {
            val response = getPostImageUrlUseCase.execute(imageId)
            val url = response.getOrNull()
                ?: return@withContext Result.failure(response.exceptionOrNull()!!)
            imagesUrlsList.add(url)
        }
        val tracksItems = ArrayList<TrackItem>()
        for (trackId in post.tracksIdsList) {
            val response = getTrackByIdUseCase.execute(trackId)
            val track = response.getOrNull()
                ?: return@withContext Result.failure(response.exceptionOrNull()!!)
            val trackCoverUrl = getTrackCoverUrlUseCase.execute(track.coverId).getOrNull()
                ?: return@withContext Result.failure(response.exceptionOrNull()!!)
            tracksItems.add(TrackItem(track, trackCoverUrl))
        }

        return@withContext Result.success(
            PostItem(
                post = post,
                imagesUrlsList = imagesUrlsList,
                tracksList = tracksItems
            )
        )
    }
}