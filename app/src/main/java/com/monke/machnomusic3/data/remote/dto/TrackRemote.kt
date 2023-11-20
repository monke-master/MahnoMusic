package com.monke.machnomusic3.data.remote.dto


data class TrackRemote (
    val id: String = "",
    val title: String = "",
    val coverId: String = "",
    val authorId: String = "",
    val duration: Int = 0,
    val releaseDate: Long = 0,
    val plays: Int = 0,
    val likes: Int = 0
)