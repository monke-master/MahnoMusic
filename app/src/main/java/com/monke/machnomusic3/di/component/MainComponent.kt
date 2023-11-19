package com.monke.machnomusic3.di.component

import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.mainFeature.MainFragment
import com.monke.machnomusic3.ui.musicFeature.music.MyMusicFragment
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.MusicPlayerFragment
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer.MiniPlayerFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [

    ]
)
interface MainComponent {

    fun inject(fragment: MiniPlayerFragment)

    fun inject(fragment: MyMusicFragment)

    fun inject(activity: MainActivity)

    fun inject(fragment: MusicPlayerFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
}