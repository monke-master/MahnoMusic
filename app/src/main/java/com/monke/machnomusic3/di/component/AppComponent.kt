package com.monke.machnomusic3.di.component

import android.app.Application
import com.monke.machnomusic3.di.AppScope
import com.monke.machnomusic3.di.module.AppModule
import com.monke.machnomusic3.di.module.FirebaseModule
import com.monke.machnomusic3.di.module.MusicModule
import com.monke.triviamasters.di.components.LoginComponent
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [
        AppModule::class,
        FirebaseModule::class,
        MusicModule::class
    ]
)
@AppScope
interface AppComponent {

    fun loginComponent(): LoginComponent.Factory

    fun mainComponent(): MainComponent.Factory


    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Application): Builder

        fun build() : AppComponent
    }
}