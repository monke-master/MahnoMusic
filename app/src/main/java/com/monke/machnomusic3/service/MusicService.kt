package com.monke.machnomusic3.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service(), MediaPlayer.OnPreparedListener {

    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicServiceBinder()

    var trackCompletionListener: TrackCompletionListener? = null


    inner class MusicServiceBinder: Binder() {

        fun getService(): MusicService = this@MusicService

    }


    fun play(url: String) {
        mediaPlayer?.release()
        mediaPlayer = initMediaPlayer()

        mediaPlayer?.setDataSource(url)
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.prepareAsync()
    }


    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()


    }

    fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
    }


    private fun initMediaPlayer(): MediaPlayer {
        return MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            trackCompletionListener?.onTrackComplete()
        }
    }

    override fun onDestroy() {
        stopPlaying()
    }
}