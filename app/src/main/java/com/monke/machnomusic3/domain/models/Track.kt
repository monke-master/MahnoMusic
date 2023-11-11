package com.monke.machnomusic3.domain.models

data class Track(
    val id: String,
    val title: String,
    val coverId: String,
    val author: User
)