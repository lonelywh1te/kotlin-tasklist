package ru.lonelywh1te.kotlin_tasklist.domain.models

sealed class TaskItem(
    open val id: Int = 0,
    open val order: Int = 0
)