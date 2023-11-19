package com.monke.machnomusic3.domain.repository

import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {


    val currentTrack: Flow<Track?>

    val musicState: Flow<MusicState>

    fun addTracksToQueue(trackList: List<Track>)

    fun nextTrack(): Track?

    fun previousTrack(): Track?

    fun playFromPosition(position: Int): Track?

    fun setMusicState(state: MusicState)

}