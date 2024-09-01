package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.Notification
import ru.lonelywh1te.kotlin_tasklist.data.notification.NotificationSchedulerImpl
import ru.lonelywh1te.kotlin_tasklist.domain.notification.NotificationScheduler

class NotificationViewModel(
    private val notificationScheduler: NotificationScheduler,
    app: Application
): AndroidViewModel(app) {
    fun setTaskNotification(task: Task, timeInMillis: Long) {
        val notification = Notification.taskNotification(task, timeInMillis)
        notificationScheduler.schedule(notification)
    }

    fun cancelTaskNotification(task: Task) {
        val notification = Notification.taskNotification(task, task.completionDateInMillis!!)
        notificationScheduler.cancel(notification)
    }
}