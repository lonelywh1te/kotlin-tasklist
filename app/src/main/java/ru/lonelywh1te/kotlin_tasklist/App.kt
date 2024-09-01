package ru.lonelywh1te.kotlin_tasklist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import ru.lonelywh1te.kotlin_tasklist.di.appModule
import ru.lonelywh1te.kotlin_tasklist.di.dataModule
import ru.lonelywh1te.kotlin_tasklist.di.domainModule

const val NOTIFICATION_CHANNEL_ID = "kotlin-tasklist"

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, domainModule, dataModule))
        }

        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "taskReminder", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }
}