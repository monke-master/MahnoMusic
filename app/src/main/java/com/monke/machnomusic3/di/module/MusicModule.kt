package com.monke.machnomusic3.di.module

import com.monke.machnomusic3.data.repository.MusicRepositoryImpl
import com.monke.machnomusic3.domain.repository.MusicRepository
import dagger.Binds
import dagger.Module

@Module
abstract class MusicModule {

    @Binds
    abstract fun bindsMusicRepository(
        repositoryImpl: MusicRepositoryImpl
    ): MusicRepository

}