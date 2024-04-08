package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.data.repository.TaskRepositoryImpl
import ru.lonelywh1te.kotlin_tasklist.data.room.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskRepository
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.AddTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetFavouriteTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetTaskByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class TaskViewModelFactory(context: Context): ViewModelProvider.Factory {
    private val taskDao = MainDatabase.getDatabase(context).TaskDao()
    private val taskRepository: TaskRepository = TaskRepositoryImpl(taskDao)

    private val addTaskUseCase = AddTaskUseCase(taskRepository)
    private val deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    private val updateTaskUseCase = UpdateTaskUseCase(taskRepository)
    private val getAllTasksUseCase= GetAllTasksUseCase(taskRepository)
    private val getFavouriteTasksUseCase = GetFavouriteTasksUseCase(taskRepository)
    private val getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(
            addTaskUseCase,
            deleteTaskUseCase,
            updateTaskUseCase,
            getAllTasksUseCase,
            getFavouriteTasksUseCase,
            getTaskByIdUseCase,
        ) as T
    }
}