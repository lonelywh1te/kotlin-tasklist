package ru.lonelywh1te.kotlin_tasklist.presentation.callback

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem

// TODO: заменить на лямбды
interface ItemClickListener {
    fun onItemClicked(taskItem: TaskItem)
    fun onTaskCheckboxClicked(taskId: Int)
}
