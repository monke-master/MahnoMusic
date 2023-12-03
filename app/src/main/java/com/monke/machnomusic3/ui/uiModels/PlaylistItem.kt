package com.monke.machnomusic3.ui.uiModels

import com.monke.machnomusic3.domain.model.Playlist

data class PlaylistItem(
    val playlist: Playlist,
    val coverUrl: String
)
