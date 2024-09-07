package ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases

import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository

class CompleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(id: Int) {
        val task = taskRepository.getTaskById(id)
        taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
    }
}