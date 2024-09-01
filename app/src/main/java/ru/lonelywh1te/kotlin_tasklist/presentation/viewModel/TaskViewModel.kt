package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task

interface TaskCompleter {
    fun completeTask(task: Task)
}