package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

class CreateTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: Task) {
        taskRepository.createTask(task.copy(order = getOrder(task.taskGroupId)))
    }

    private suspend fun getOrder(taskGroupId: Int?): Int {
        return if (taskGroupId == null) {
            taskRepository.getAllTasks().size
        } else {
            taskRepository.getAllTasksFromGroup(taskGroupId).size
        }
    }
}