package com.monke.machnomusic3.domain.model

data class Playlist(
    val id: String,
    val title: String,
    val description: String? = null,
    val author: User,
    val coverId: String,
    val tracksIdsList: List<String>,
    val creationDate: Long,
    val likes: Int = 0
)