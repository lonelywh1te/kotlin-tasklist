package ru.lonelywh1te.kotlin_tasklist.domain.notifications

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task

data class Notification (
    val id: Int,
    val title: String,
    val message: String,
    val timeInMillis: Long,
) {
    companion object {
        const val TITLE_EXTRA_NAME = "NOTIFICATION_TITLE"
        const val MESSAGE_EXTRA_NAME = "NOTIFICATION_MESSAGE"

        fun taskNotification(task: Task, timeInMillis: Long): Notification {
            return Notification (
                task.title.hashCode(),
                task.title,
                "Сроки поджимают, пора бы завершить задачу!",
                timeInMillis
            )
        }
    }
}
