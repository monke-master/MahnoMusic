package com.monke.machnomusic3.main.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.monke.machnomusic3.R
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.main.App
import com.monke.machnomusic3.service.MusicService
import com.monke.machnomusic3.service.TrackCompletionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity(), TrackCompletionListener {

    // Music service connection
    private var musicService: MusicService? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            musicService = (binder as MusicService.MusicServiceBinder).getService()
            musicService?.trackCompletionListener = this@MainActivity
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            musicService = null
        }

    }

    @Inject
    lateinit var factory: ActivityViewModel.Factory
    private val viewModel: ActivityViewModel by viewModels { factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainComponent().inject(this)

        lifecycleScope.launch {
            launch {
                viewModel.musicState.collect { state ->
                    when (state) {
                        MusicState.Pause -> pauseTrack()
                        MusicState.Resume -> {
                            resumeTrack()
                            getTrackProgress()
                        }
                        MusicState.Start -> {
                            if (musicService == null) {
                                Intent(this@MainActivity, MusicService::class.java).also { intent ->
                                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                                }
                            }

                        }
                        MusicState.Stop -> stopTrack()
                    }
                }
            }

            launch {
                viewModel.currentTrack.collect { track ->
                    track?.let {
                        val ref = Firebase.storage.getReference("music/${track.id}")
                        ref.downloadUrl.addOnSuccessListener {
                            playTrack(it.toString())
                        }
                    }
                }
            }

            launch {

            }
        }
    }


    private suspend fun getTrackProgress() {
        while (viewModel.musicState.first() is MusicState.Resume) {
            musicService?.getProgress()?.let { viewModel.setProgress(it) }
            delay(1000)
        }
    }



    private fun playTrack(trackUrl: String) {
        musicService?.play(trackUrl)
    }

    private fun pauseTrack() {
        musicService?.pause()
    }

    private fun resumeTrack() {
        musicService?.resume()
    }

    private fun stopTrack() {
        musicService?.stopPlaying()
    }

    fun loginComponent() = (application as App).appComponent.loginComponent().create()

    fun mainComponent() = (application as App).appComponent.mainComponent().create()

    override fun onTrackComplete() {
        viewModel.nextTrack()
    }
}