package com.monke.machnomusic3.di.component

import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.di.module.MusicModule
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.musicFeature.music.myMusic.MyMusicFragment
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.MusicPlayerFragment
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer.MiniPlayerFragment
import com.monke.machnomusic3.ui.musicFeature.track.UploadTrackFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MusicModule::class
    ]
)
@MainScope
interface MainComponent {

    fun inject(fragment: MiniPlayerFragment)

    fun inject(fragment: MyMusicFragment)

    fun inject(activity: MainActivity)

    fun inject(fragment: MusicPlayerFragment)

    fun inject(fragment: UploadTrackFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
}