package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

class MoveTaskToTaskGroupUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: Task, taskGroupId: Int?) {
        taskRepository.updateTask(task.copy(taskGroupId = taskGroupId, order = getTasks(taskGroupId).size))
        reorderTasksFrom(task)
    }

    private suspend fun reorderTasksFrom(task: Task) {
        val tasks = getTasks(task.taskGroupId)

        tasks.filter { it.order > task.order }.forEach {
            taskRepository.updateTask(it.copy(order = it.order - 1))
        }
    }

    private suspend fun getTasks(taskGroupId: Int?): List<Task> {
        return if (taskGroupId == null) {
            taskRepository.getAllTasks()
        } else {
            taskRepository.getAllTasksFromGroup(taskGroupId)
        }
    }
}