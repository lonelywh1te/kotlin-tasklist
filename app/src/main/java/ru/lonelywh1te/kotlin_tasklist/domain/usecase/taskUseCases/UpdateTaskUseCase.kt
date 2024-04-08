package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

class UpdateTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: Task) {
        taskRepository.updateTask(task)
    }
}