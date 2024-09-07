package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository

class DeleteTaskGroupUseCase(private val taskGroupRepository: TaskGroupRepository) {
    suspend fun execute(taskGroup: TaskGroup) {
        taskGroupRepository.deleteTaskGroup(taskGroup)
        reorderTaskGroupsFrom(taskGroup)
    }

    private suspend fun reorderTaskGroupsFrom(taskGroup: TaskGroup) {
        val taskGroups = taskGroupRepository.getAllTaskGroups()
        taskGroups.filter { it.order > taskGroup.order }.forEach {
            taskGroupRepository.updateTaskGroup(it.copy(order = it.order - 1))
        }
    }
}