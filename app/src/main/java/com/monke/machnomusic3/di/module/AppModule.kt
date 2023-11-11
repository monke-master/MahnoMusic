package com.monke.machnomusic3.di.module

import com.monke.triviamasters.di.components.LoginComponent
import dagger.Module

@Module(
    subcomponents = [
        LoginComponent::class,
    ]
)
class AppModule {}