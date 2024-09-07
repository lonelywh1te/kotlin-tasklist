package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: Task) {
        taskRepository.deleteTask(task)
        reorderTasksFrom(task)
    }

    private suspend fun reorderTasksFrom(task: Task) {
        val tasks = if (task.taskGroupId == null) {
            taskRepository.getAllTasks()
        } else {
            taskRepository.getAllTasksFromGroup(task.taskGroupId)
        }

        tasks.filter { it.order > task.order }.forEach {
            taskRepository.updateTask(it.copy(order = it.order - 1))
        }
    }
}