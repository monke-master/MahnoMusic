package com.monke.machnomusic3.di.component

import com.monke.machnomusic3.di.MainScope
import com.monke.machnomusic3.di.module.MusicModule
import com.monke.machnomusic3.di.module.ProfileModule
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.mainFeature.MainFragment
import com.monke.machnomusic3.ui.musicFeature.album.AlbumFragment
import com.monke.machnomusic3.ui.musicFeature.album.UploadAlbumFragment
import com.monke.machnomusic3.ui.musicFeature.music.myMusic.MyMusicFragment
import com.monke.machnomusic3.ui.musicFeature.music.search.SearchMusicFragment
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.MusicPlayerFragment
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer.MiniPlayerFragment
import com.monke.machnomusic3.ui.musicFeature.playlist.PlaylistFragment
import com.monke.machnomusic3.ui.musicFeature.playlist.UploadPlaylistFragment
import com.monke.machnomusic3.ui.musicFeature.track.SelectTracksFragment
import com.monke.machnomusic3.ui.musicFeature.track.UploadTrackFragment
import com.monke.machnomusic3.ui.userFeature.search.SearchUserFragment
import com.monke.machnomusic3.ui.userFeature.post.UploadPostFragment
import com.monke.machnomusic3.ui.userFeature.profile.ProfileFragment
import com.monke.machnomusic3.ui.userFeature.profile.ProfilePictureFragment
import com.monke.machnomusic3.ui.userFeature.user.UserFragment
import com.monke.machnomusic3.ui.userFeature.user.UsersListFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MusicModule::class,
        ProfileModule::class
    ]
)
@MainScope
interface MainComponent {

    fun inject(fragment: MiniPlayerFragment)

    fun inject(fragment: MyMusicFragment)

    fun inject(activity: MainActivity)

    fun inject(fragment: MusicPlayerFragment)

    fun inject(fragment: UploadTrackFragment)

    fun inject(fragment: UploadAlbumFragment)

    fun inject(fragment: UploadPlaylistFragment)

    fun inject(fragment: UploadPostFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(fragment: AlbumFragment)

    fun inject(fragment: SearchMusicFragment)

    fun inject(fragment: SelectTracksFragment)

    fun inject(fragment: SearchUserFragment)

    fun inject(fragment: UserFragment)

    fun inject(fragment: ProfilePictureFragment)

    fun inject(fragment: PlaylistFragment)

    fun inject(mainFragment: MainFragment)

    fun inject(usersListFragment: UsersListFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
}