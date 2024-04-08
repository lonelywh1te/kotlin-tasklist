package ru.lonelywh1te.kotlin_tasklist.domain.models

import java.io.Serializable

data class Task (
    val title: String,
    val description: String,
    val isFavourite: Boolean = false,
    val isCompleted: Boolean = false,
    val completionDateInMillis: Long? = null,
    val taskGroupId: Int? = null,
    val id: Int = 0,
) : TaskItem(), Serializable
