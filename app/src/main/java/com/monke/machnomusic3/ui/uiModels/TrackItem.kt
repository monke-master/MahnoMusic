package com.monke.machnomusic3.ui.uiModels

import com.monke.machnomusic3.domain.model.Track

data class TrackItem (
    val track: Track,
    val coverUrl: String
)