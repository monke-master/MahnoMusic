package com.monke.machnomusic3.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service(), MediaPlayer.OnPreparedListener {

    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicServiceBinder()



    inner class MusicServiceBinder: Binder() {

        fun getService(): MusicService = this@MusicService

    }

    fun play(url: String) {
        if (mediaPlayer == null)
            mediaPlayer = initMediaPlayer()

        mediaPlayer?.setDataSource(url)
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.prepareAsync()
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

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}