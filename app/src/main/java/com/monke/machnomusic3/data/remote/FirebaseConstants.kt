package com.monke.machnomusic3.data.remote

// Collection
const val USERS_COLLECTION = "users"
const val TRACKS_COLLECTION = "tracks"
const val ALBUMS_COLLECTION = "albums"
const val POSTS_COLLECTION = "posts"
const val PLAYLISTS_COLLECTION = "playlists"

object TrackFields {
    const val title = "title"
}

object UserFields {
    const val username = "username"
    const val login = "login"
}

// Storage
const val TRACKS_STORAGE = "tracks"
const val TRACKS_COVERS_STORAGE = "tracks_covers"
const val POSTS_IMAGES_STORAGE = "posts_images"
const val USERS_PROFILE_PICTURES = "profile_pictures"

// Default
const val DEFAULT_TRACK_COVER_ID = "default"
const val DEFAULT_USER_PICTURE_ID = "default"