package com.monke.machnomusic3.domain.repository

import com.monke.machnomusic3.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    var isPlaying: Boolean

    val currentTrack: Flow<Track?>

    fun addTracksToQueue(trackList: List<Track>)

    fun nextTrack(): Track?

    fun previousTrack(): Track?

    fun atPosition(position: Int): Track?

}