package com.monke.machnomusic3.domain.repository

import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.TrackProgress
import kotlinx.coroutines.flow.Flow

interface MusicRepository {


    val currentTrack: Flow<Track?>

    val musicState: Flow<MusicState>

    val trackProgress: Flow<TrackProgress>

    fun addTracksToQueue(trackList: List<Track>)

    fun nextTrack(): Track?

    fun previousTrack(): Track?

    fun playFromPosition(position: Int): Track?

    fun setMusicState(state: MusicState)

    fun setTrackProgress(progress: TrackProgress)

}