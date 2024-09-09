package ru.lonelywh1te.kotlin_tasklist.presentation.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.lonelywh1te.kotlin_tasklist.NOTIFICATION_CHANNEL_ID
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.domain.models.Notification
import ru.lonelywh1te.kotlin_tasklist.presentation.MainActivity

class NotificationReceiver: BroadcastReceiver() {
    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent?) {
        val notification = intent?.getSerializableExtra(Notification.EXTRA_NAME) as Notification

        notificationManager = context.getSystemService(NotificationManager::class.java)
        showNotification(notification, context)
    }

    private fun showNotification(n: Notification, context: Context) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(n.title)
            .setContentText(n.message)
            .setSmallIcon(R.drawable.ic_tasks)
            .setColor(ContextCompat.getColor(context, R.color.blue))
            .setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE))
            .build()

        notificationManager.notify(notification.hashCode(), notification)
    }
}