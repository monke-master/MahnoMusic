package com.monke.machnomusic3.di.module

import com.monke.machnomusic3.di.component.MainComponent
import com.monke.machnomusic3.di.component.LoginComponent
import dagger.Module

@Module(
    subcomponents = [
        LoginComponent::class,
        MainComponent::class
    ]
)
class AppModule {}