package com.monke.machnomusic3.di.module

import com.monke.machnomusic3.data.repository.PostRepositoryImpl
import com.monke.machnomusic3.data.repository.UserRepositoryImpl
import com.monke.machnomusic3.domain.repository.PostRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class UserModule {

    @Binds
    abstract fun bindUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository


}