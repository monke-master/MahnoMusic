package com.monke.machnomusic3.data.extensions

import com.monke.machnomusic3.data.remote.dto.AlbumRemote
import com.monke.machnomusic3.data.remote.dto.PlaylistRemote
import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.model.Playlist
import com.monke.machnomusic3.domain.model.User

fun Playlist.toRemote() =
    PlaylistRemote(
        id = this.id,
        title = this.title,
        authorId = this.author.id,
        coverId = this.coverId,
        description = this.description,
        tracksIdsList = this.tracksIdsList,
        creationDate = this.creationDate,
        likes = this.likes
    )

fun PlaylistRemote.toDomain(author: User) =
    Playlist(
        id = this.id,
        title = this.title,
        author = author,
        coverId = this.coverId,
        tracksIdsList = this.tracksIdsList,
        creationDate = this.creationDate,
        likes = this.likes,
        description = this.description
    )