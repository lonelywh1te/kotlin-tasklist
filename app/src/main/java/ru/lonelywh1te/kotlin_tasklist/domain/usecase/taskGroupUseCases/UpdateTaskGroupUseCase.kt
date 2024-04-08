package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository

class UpdateTaskGroupUseCase(private val taskGroupRepository: TaskGroupRepository) {
    suspend fun execute(taskGroup: TaskGroup) {
        taskGroupRepository.updateTaskGroup(taskGroup)
    }
}