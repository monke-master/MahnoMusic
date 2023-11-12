package com.monke.machnomusic3.data.repository

import android.util.Log
import com.monke.machnomusic3.data.collection.TrackQueue
import com.monke.machnomusic3.di.AppScope
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@AppScope
class MusicRepositoryImpl @Inject constructor(): MusicRepository {

    private val tracksQueue = TrackQueue()

    override var isPlaying: Boolean = true

    override val currentTrack = MutableStateFlow<Track?>(null)

    init {
        Log.d("MusicRepositoryImpl", "init block")
    }

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

    override fun playFromPosition(position: Int): Track? {
        if (position in 0..tracksQueue.lastIndex) {
            val track = tracksQueue[position]
            currentTrack.value = track
            return track
        }
        return null
    }


}