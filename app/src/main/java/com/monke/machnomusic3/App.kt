package com.monke.machnomusic3

import android.app.Application
import com.monke.machnomusic3.di.component.DaggerAppComponent

class App: Application() {

    val appComponent = DaggerAppComponent.builder().context(this).build()
}