package com.monke.machnomusic3.domain.model


data class TrackProgress (
    val progress: Int,
    var changedFromUser: Boolean = false
)