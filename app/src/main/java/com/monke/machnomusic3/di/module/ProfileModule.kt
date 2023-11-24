package com.monke.machnomusic3.di.module

import com.monke.machnomusic3.data.repository.PostRepositoryImpl
import com.monke.machnomusic3.domain.repository.PostRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ProfileModule {

    @Binds
    abstract fun bindPostRepository(repositoryImpl: PostRepositoryImpl): PostRepository
}