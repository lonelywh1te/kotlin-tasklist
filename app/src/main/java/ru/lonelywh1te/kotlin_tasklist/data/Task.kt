package ru.lonelywh1te.kotlin_tasklist.data

data class Task(
    var title: String,
    var description: String,
    var isFavourite: Boolean = false,
    val isCompleted: Boolean = false,
)
