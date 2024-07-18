package ru.lonelywh1te.kotlin_tasklist.domain.usecase

import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

class GetAllTaskItemsUseCase(
    private val taskRepository: TaskRepository,
    private val taskGroupRepository: TaskGroupRepository,
) {
    suspend fun execute(): List<TaskItem> {
        return taskGroupRepository.getAllTaskGroups() + taskRepository.getAllTasks()
    }
}