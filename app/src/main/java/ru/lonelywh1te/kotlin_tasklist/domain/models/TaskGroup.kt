package ru.lonelywh1te.kotlin_tasklist.domain.models

data class TaskGroup (
    val name: String,
    val description: String,
    val id: Int = 0,
) : TaskItem
