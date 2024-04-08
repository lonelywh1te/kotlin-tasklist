package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.data.repository.TaskGroupRepositoryImpl
import ru.lonelywh1te.kotlin_tasklist.data.room.MainDatabase
import ru.lonelywh1te.kotlin_tasklist.domain.repository.TaskGroupRepository
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.AddTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.DeleteTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetAllTaskGroupsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.UpdateTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel

class TaskGroupViewModelFactory(context: Context): ViewModelProvider.Factory {
    private val taskGroupDao = MainDatabase.getDatabase(context).TaskGroupDao()
    private val taskGroupRepository: TaskGroupRepository = TaskGroupRepositoryImpl(taskGroupDao)

    private val addTaskGroupUseCase = AddTaskGroupUseCase(taskGroupRepository)
    private val deleteTaskGroupUseCase = DeleteTaskGroupUseCase(taskGroupRepository)
    private val updateTaskGroupUseCase = UpdateTaskGroupUseCase(taskGroupRepository)
    private val getAllTaskGroupUseCase = GetAllTaskGroupsUseCase(taskGroupRepository)
    private val getTaskGroupByIdUseCase = GetTaskGroupByIdUseCase(taskGroupRepository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskGroupViewModel(
            addTaskGroupUseCase,
            deleteTaskGroupUseCase,
            updateTaskGroupUseCase,
            getAllTaskGroupUseCase,
            getTaskGroupByIdUseCase
        ) as T
    }
}