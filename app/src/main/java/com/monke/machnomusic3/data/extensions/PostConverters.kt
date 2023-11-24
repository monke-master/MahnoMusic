package com.monke.machnomusic3.data.extensions

import com.monke.machnomusic3.data.remote.dto.PostRemote
import com.monke.machnomusic3.domain.model.Post
import com.monke.machnomusic3.domain.model.User

fun Post.toRemote() =
    PostRemote(
        id = this.id,
        authorId = this.author.id,
        text = this.text,
        tracksIdsList = this.tracksIdsList,
        imagesIdsList = this.imagesIdsList,
        albumId = this.albumId,
        creationDate = this.creationDate,
        likes = this.likes
    )

fun PostRemote.toDomain(author: User) =
    Post(
        id = this.id,
        author = author,
        text = this.text,
        tracksIdsList = this.tracksIdsList,
        imagesIdsList = this.imagesIdsList,
        albumId = this.albumId,
        creationDate = this.creationDate,
        likes = this.likes
    )