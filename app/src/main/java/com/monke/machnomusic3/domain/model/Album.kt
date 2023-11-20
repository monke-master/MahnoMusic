package com.monke.machnomusic3.domain.model

data class Album (
    val id: String,
    val title: String,
    val author: User,
    val coverId: String,
    val tracksIdsList: List<String>,
    val releaseDate: Long,
    val likes: Int = 0
)