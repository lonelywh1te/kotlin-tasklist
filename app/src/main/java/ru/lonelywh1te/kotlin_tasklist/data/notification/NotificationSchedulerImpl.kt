package ru.lonelywh1te.kotlin_tasklist.data.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.lonelywh1te.kotlin_tasklist.domain.models.Notification
import ru.lonelywh1te.kotlin_tasklist.domain.notification.NotificationScheduler
import ru.lonelywh1te.kotlin_tasklist.presentation.receiver.NotificationReceiver

class NotificationSchedulerImpl(private val context: Context): NotificationScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val intent = Intent(context, NotificationReceiver::class.java)

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(notification: Notification) {
        intent.putExtra(Notification.EXTRA_NAME, notification)

        alarmManager.setExactAndAllowWhileIdle (AlarmManager.RTC_WAKEUP,
            notification.timeInMillis,
            getPendingIntent(notification)
        )
    }

    override fun cancel(notification: Notification) {
        alarmManager.cancel(getPendingIntent(notification))
        getPendingIntent(notification).cancel()
    }

    private fun getPendingIntent(notification: Notification): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}