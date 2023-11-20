package com.monke.machnomusic3.data.extensions

import com.monke.machnomusic3.data.remote.dto.AlbumRemote
import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.model.User

fun Album.toRemote() =
    AlbumRemote(
        id = this.id,
        title = this.title,
        authorId = this.author.id,
        coverId = this.coverId,
        tracksIdsList = this.tracksIdsList,
        releaseDate = this.releaseDate,
        likes = this.likes
    )

fun AlbumRemote.toDomain(author: User) =
    Album(
        id = this.id,
        title = this.title,
        author = author,
        coverId = this.coverId,
        tracksIdsList = this.tracksIdsList,
        releaseDate = this.releaseDate,
        likes = this.likes
    )