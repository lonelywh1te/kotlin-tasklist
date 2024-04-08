package ru.lonelywh1te.kotlin_tasklist.domain.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils

class NotificationScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val intent = Intent(context, NotificationReceiver::class.java)

    @SuppressLint("ScheduleExactAlarm")
    fun schedule(notification: Notification) {
        intent.putExtra(Notification.TITLE_EXTRA_NAME, notification.title)
        intent.putExtra(Notification.MESSAGE_EXTRA_NAME, notification.message)

        alarmManager.setExactAndAllowWhileIdle (
            AlarmManager.RTC_WAKEUP,
            notification.timeInMillis,
            getPendingIntent(notification)
        )
        Log.println(Log.DEBUG, "kotlin-tasklist", "SCHEDULE: notification_id: ${notification.id}")
        Log.println(Log.DEBUG, "kotlin-tasklist", "TIME_TO_NOTIFICATION: ${DateUtils.normalDateFormat(notification.timeInMillis)}")
    }

    fun cancel(notification: Notification) {
        Log.println(Log.DEBUG, "kotlin-tasklist", "CANCEL: notification_id: ${notification.id}")
        Log.println(Log.DEBUG, "kotlin-tasklist", "TIME_TO_NOTIFICATION: ${DateUtils.normalDateFormat(notification.timeInMillis)}")
        alarmManager.cancel(getPendingIntent(notification))
        getPendingIntent(notification).cancel()
    }

    private fun getPendingIntent(notification: Notification): PendingIntent {
        return PendingIntent.getBroadcast(context, notification.id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }
}