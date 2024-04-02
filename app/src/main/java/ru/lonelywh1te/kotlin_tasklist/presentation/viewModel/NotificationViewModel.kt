package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.presentation.notifications.Notification
import ru.lonelywh1te.kotlin_tasklist.presentation.notifications.NotificationScheduler

class NotificationViewModel(app: Application): AndroidViewModel(app) {
    private val notificationScheduler = NotificationScheduler(app)

    fun setTaskNotification(task: Task, timeInMillis: Long) {
        val notification = Notification.taskNotification(task, timeInMillis)
        notificationScheduler.schedule(notification)
    }

    fun cancelTaskNotification(task: Task, timeInMillis: Long) {
        val notification = Notification.taskNotification(task, timeInMillis)
        notificationScheduler.cancel(notification)
    }
}