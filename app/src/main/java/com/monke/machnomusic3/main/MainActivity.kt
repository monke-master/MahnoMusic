package com.monke.machnomusic3.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.monke.machnomusic3.R
import com.monke.machnomusic3.service.MusicService
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    // Music service connection
    private var musicService: MusicService? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            musicService = (binder as MusicService.MusicServiceBinder).getService()
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
            viewModel.currentTrack.collect { track ->
                Log.d("Activity", "yer")
                track?.let {
                    val ref = Firebase.storage.getReference("music/${it.id}")
                    ref.downloadUrl.addOnSuccessListener {
                        playTrack(it.toString())
                    }
                }
            }
        }
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

    fun mainComponent() = (application as App).appComponent.mainComponent().create()
}