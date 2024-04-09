package ru.lonelywh1te.kotlin_tasklist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import ru.lonelywh1te.kotlin_tasklist.di.appModule
import ru.lonelywh1te.kotlin_tasklist.di.dataModule
import ru.lonelywh1te.kotlin_tasklist.di.domainModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }
}