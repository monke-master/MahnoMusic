package com.monke.machnomusic3.di.component

import com.monke.machnomusic3.di.AppScope
import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.di.module.MusicModule
import com.monke.machnomusic3.main.MainActivity
import com.monke.machnomusic3.ui.musicFeature.music.MyMusicFragment
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.MiniPlayerFragment
import com.monke.triviamasters.di.components.LoginComponent
import dagger.Subcomponent

@Subcomponent(
    modules = [

    ]
)
interface MainComponent {

    fun inject(fragment: MiniPlayerFragment)

    fun inject(fragment: MyMusicFragment)

    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
}