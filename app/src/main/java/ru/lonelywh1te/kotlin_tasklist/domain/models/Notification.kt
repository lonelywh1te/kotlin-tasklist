package ru.lonelywh1te.kotlin_tasklist.domain.models

import java.io.Serializable

data class Notification (
    val id: Int,
    val title: String,
    val message: String,
    val timeInMillis: Long,
): Serializable {
    companion object {
        const val EXTRA_NAME = "notification_extra"

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
