package com.monke.machnomusic3.ui.uiModels

import com.monke.machnomusic3.domain.model.Post

data class PostItem (
    val post: Post,
    val userPictureUrl: String,
    val tracksUrlsList: List<String> = emptyList(),
    val imagesUrlsList: List<String> = emptyList(),
)