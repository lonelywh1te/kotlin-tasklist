package ru.lonelywh1te.kotlin_tasklist.domain.notification

import ru.lonelywh1te.kotlin_tasklist.domain.models.Notification

interface NotificationScheduler {
    fun schedule(notification: Notification)
    fun cancel(notification: Notification)
}