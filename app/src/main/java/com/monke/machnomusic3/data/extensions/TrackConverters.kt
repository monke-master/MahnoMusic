package com.monke.machnomusic3.data.extensions

import com.monke.machnomusic3.data.remote.dto.TrackRemote
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User

fun Track.toRemote() =
    TrackRemote(
        id = this.id,
        title = this.title,
        coverId = this.coverId,
        authorId = this.author.id,
        duration = this.duration,
        releaseDate = this.releaseDate,
        plays = this.plays,
        likes = this.likes
    )

fun TrackRemote.toDomain(author: User) =
    Track(
        id = this.id,
        title = this.title,
        coverId = this.coverId,
        author = author,
        duration = this.duration,
        releaseDate = this.releaseDate,
        plays = this.plays,
        likes = this.likes
    )