package com.monke.machnomusic3.data.repository

import com.monke.machnomusic3.data.collection.TrackQueue
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow

class MusicRepositoryImpl: MusicRepository {

    private val tracksQueue = TrackQueue()

    override var isPlaying: Boolean = true

    override val currentTrack = MutableStateFlow<Track?>(null)

    override fun addTracksToQueue(trackList: List<Track>) {
        tracksQueue.addTracks(trackList)
    }

    override fun nextTrack(): Track? {
        val track = tracksQueue.next()
        track?.let { currentTrack.value = it }
        return track
    }

    override fun previousTrack(): Track? {
        val track = tracksQueue.previous()
        track?.let { currentTrack.value = it }
        return track
    }

    override fun atPosition(position: Int): Track? {
        if (position in 0..tracksQueue.lastIndex)
            return tracksQueue[position]
        return null
    }


}