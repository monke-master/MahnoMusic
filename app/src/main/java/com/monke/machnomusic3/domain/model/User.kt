package com.monke.machnomusic3.domain.model


data class User (
    val id: String,
    val username: String,
    val login: String,
    val email: String,
    val password: String,
    val bio: String? = null,
    val profilePicId: String? = null,
    val tracksIdsList: List<String> = emptyList(),
    val albumsIdsList: List<String> = emptyList(),
    val playlistsIdsList: List<String> = emptyList(),
    val subscribersIdsList: List<String> = emptyList(),
    val subscriptionsIdsList: List<String> = emptyList(),
)