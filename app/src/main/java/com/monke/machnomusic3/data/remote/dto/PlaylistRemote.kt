package com.monke.machnomusic3.data.remote.dto

data class PlaylistRemote(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val authorId: String = "",
    val coverId: String = "",
    val tracksIdsList: List<String> = emptyList(),
    val creationDate: Long = 0,
    val likes: Int = 0
)