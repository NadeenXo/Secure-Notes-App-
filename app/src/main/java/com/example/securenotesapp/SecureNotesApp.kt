package com.example.securenotesapp

import android.app.Application
import com.example.securenotesapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SecureNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SecureNotesApp)
            modules(appModule)
        }
    }
}