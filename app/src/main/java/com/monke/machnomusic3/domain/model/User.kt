package com.monke.machnomusic3.domain.model

data class User (
    val id: String,
    val username: String,
    val login: String,
    val email: String,
    val password: String,
    val bio: String,
    val profilePicId: String
)