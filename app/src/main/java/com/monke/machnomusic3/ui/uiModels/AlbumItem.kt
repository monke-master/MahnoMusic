package com.monke.machnomusic3.ui.uiModels

import com.monke.machnomusic3.domain.model.Album

data class AlbumItem (
    val album: Album,
    val coverUrl: String
)