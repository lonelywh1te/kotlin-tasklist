package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

class GetAllTasksUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(): List<Task> {
        return taskRepository.getAllTasks()
    }

    suspend fun execute(taskGroupId: Int?): List<Task> {
        return taskRepository.getAllTasksFromGroup(taskGroupId)
    }
}