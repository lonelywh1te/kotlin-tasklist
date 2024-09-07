package ru.lonelywh1te.kotlin_tasklist.domain.models

data class Task (
    val title: String,
    val description: String,
    val isFavourite: Boolean = false,
    val isCompleted: Boolean = false,
    val completionDateInMillis: Long? = null,
    val taskGroupId: Int? = null,
    override val id: Int = 0,
    override val order: Int = 0
) : TaskItem(id, order)
