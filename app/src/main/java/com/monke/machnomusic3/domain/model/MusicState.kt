package com.monke.machnomusic3.domain.model

sealed class MusicState {


    object Start : MusicState()

    object Pause: MusicState()

    object Resume: MusicState()

    object Stop: MusicState()
}