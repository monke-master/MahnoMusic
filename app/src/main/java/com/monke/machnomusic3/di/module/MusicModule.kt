package com.monke.machnomusic3.di.module

import com.monke.machnomusic3.data.repository.AlbumRepositoryImpl
import com.monke.machnomusic3.data.repository.MusicRepositoryImpl
import com.monke.machnomusic3.data.repository.TrackRepositoryImpl
import com.monke.machnomusic3.domain.repository.AlbumRepository
import com.monke.machnomusic3.domain.repository.MusicRepository
import com.monke.machnomusic3.domain.repository.TrackRepository
import dagger.Binds
import dagger.Module

@Module
abstract class MusicModule {

    @Binds
    abstract fun bindsMusicRepository(
        repositoryImpl: MusicRepositoryImpl
    ): MusicRepository

    @Binds
    abstract fun bindTrackRepository(repositoryImpl: TrackRepositoryImpl): TrackRepository

    @Binds
    abstract fun bindAlbumRepository(repositoryImpl: AlbumRepositoryImpl): AlbumRepository

}