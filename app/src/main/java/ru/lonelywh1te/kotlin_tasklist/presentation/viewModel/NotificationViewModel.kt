package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.content.Context
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.notifications.Notification
import ru.lonelywh1te.kotlin_tasklist.domain.notifications.NotificationScheduler

class NotificationViewModel(context: Context) {
    private val notificationScheduler = NotificationScheduler(context)

    fun setTaskNotification(task: Task, timeInMillis: Long) {
        val notification = Notification.taskNotification(task, timeInMillis)
        notificationScheduler.schedule(notification)
    }

    fun cancelTaskNotification(task: Task, timeInMillis: Long) {
        val notification = Notification.taskNotification(task, timeInMillis)
        notificationScheduler.cancel(notification)
    }
}