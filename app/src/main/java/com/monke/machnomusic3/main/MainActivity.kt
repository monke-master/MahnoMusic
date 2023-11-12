package com.monke.machnomusic3.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.monke.machnomusic3.R
import com.monke.machnomusic3.service.MusicService

class MainActivity : AppCompatActivity() {

    private var musicService: MusicService? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            musicService = (binder as MusicService.MusicServiceBinder).getService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            musicService = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun playTrack(trackUrl: String) {
        musicService?.play(trackUrl)
    }

    fun loginComponent() = (application as App).appComponent.loginComponent().create()
}