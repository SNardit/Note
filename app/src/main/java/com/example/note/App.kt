package com.example.note

import androidx.multidex.MultiDexApplication
import org.koin.core.context.startKoin

class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule, mainModule, splashModule, noteModule)
        }
    }
}