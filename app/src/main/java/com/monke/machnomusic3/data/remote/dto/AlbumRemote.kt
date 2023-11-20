package com.monke.machnomusic3.data.remote.dto


data class AlbumRemote(
    val id: String = "",
    val title: String = "",
    val authorId: String = "",
    val coverId: String = "",
    val tracksIdsList: List<String> = emptyList(),
    val releaseDate: Long = 0,
    val likes: Int = 0
)