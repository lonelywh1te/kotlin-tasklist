package ru.lonelywh1te.kotlin_tasklist.domain.models

data class TaskGroup (
    val name: String,
    val description: String,
    override val id: Int = 0,
    override val order: Int = 0
) : TaskItem(id, order)
