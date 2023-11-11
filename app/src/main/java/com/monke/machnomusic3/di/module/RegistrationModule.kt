package com.monke.machnomusic3.di.module

import com.monke.machnomusic3.data.repository.RegistrationRepositoryImpl
import com.monke.machnomusic3.domain.repository.RegistrationRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RegistrationModule {

    @Binds
    abstract fun bindsRegistrationRepository(
        repositoryImpl: RegistrationRepositoryImpl
    ): RegistrationRepository
}