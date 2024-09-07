package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository

class CreateTaskGroupUseCase(private val taskGroupRepository: TaskGroupRepository) {
    suspend fun execute(taskGroup: TaskGroup) {
        taskGroupRepository.createTaskGroup(taskGroup.copy(order = getOrder()))
    }

    private suspend fun getOrder(): Int {
        return taskGroupRepository.getAllTaskGroups().size
    }
}