package com.monke.machnomusic3.data.extensions

import com.monke.machnomusic3.data.remote.dto.UserRemote
import com.monke.machnomusic3.domain.model.User


fun UserRemote.toDomain() =
    User(
        id = this.id,
        username = this.username,
        login = this.login,
        email = this.email,
        password = this.password,
        bio = this.bio,
        profilePicId = this.profilePicId,
        tracksIdsList = this.tracksIdsList,
        albumsIdsList = this.albumsIdsList,
        playlistsIdsList = this.playlistsIdsList
)