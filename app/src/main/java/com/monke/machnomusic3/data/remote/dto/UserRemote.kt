package com.monke.machnomusic3.data.remote.dto


data class UserRemote(
    val id: String = "",
    val username: String = "",
    val login: String = "",
    val email: String = "",
    val password: String = "",
    val bio: String? = null,
    val profilePicId: String? = null,
    var tracksIdsList: List<String> = emptyList(),
    val albumsIdsList: List<String> = emptyList(),
    val playlistsIdsList: List<String> = emptyList(),
    val subscribersIdsList: List<String> = emptyList(),
    val subscriptionsIdsList: List<String> = emptyList(),
)