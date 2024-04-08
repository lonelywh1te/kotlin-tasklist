package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository

class GetTaskGroupByIdUseCase(private val taskGroupRepository: TaskGroupRepository) {
    suspend fun execute(id: Int): TaskGroup {
        return taskGroupRepository.getTaskGroupById(id)
    }
}