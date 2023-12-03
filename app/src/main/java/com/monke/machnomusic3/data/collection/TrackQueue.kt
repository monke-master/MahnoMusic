package com.monke.machnomusic3.data.collection

import com.monke.machnomusic3.domain.model.Track

class TrackQueue() {

    private val tracksList = ArrayList<Track>()
    private var currentTrack = 0

    val lastIndex: Int
        get() = tracksList.lastIndex

    fun addTracks(tracks: List<Track>) {
        tracksList.addAll(tracks)
    }

    fun next(): Track? {
        currentTrack++
        if (currentTrack < tracksList.size)
            return tracksList[currentTrack]
        return null
    }

    fun previous(): Track? {
        currentTrack--
        if (currentTrack > -1)
            return tracksList[currentTrack]
        return null
    }

    operator fun get(index: Int): Track {
        currentTrack = index
        return tracksList[index]
    }

    fun clear() {
        tracksList.clear()
        currentTrack = 0
    }




}