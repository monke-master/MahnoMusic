package com.monke.machnomusic3.data.remote.dto

data class PostRemote(
    val id: String = "",
    val authorId: String = "",
    val text: String? = null,
    val tracksIdsList: List<String> = emptyList(),
    val imagesIdsList: List<String> = emptyList(),
    val albumId: String? = null,
    val creationDate: Long = 0,
    val likes: Int = 0
)