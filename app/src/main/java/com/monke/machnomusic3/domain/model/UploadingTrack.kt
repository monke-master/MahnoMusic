package com.monke.machnomusic3.domain.model

import android.net.Uri

data class UploadingTrack (
    val id: String,
    val uri: Uri,
    val title: String,
    val duration: Int
)