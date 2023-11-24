package com.monke.machnomusic3.domain.model

data class Post(
    val id: String,
    val author: User,
    val text: String,
    val tracksIdsList: List<String> = emptyList(),
    val imagesIdsList: List<String> = emptyList(),
    val albumId: String?,
    val creationDate: Long,
    val likes: Int
)