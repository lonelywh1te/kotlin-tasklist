package ru.lonelywh1te.kotlin_tasklist.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

val appModule = module {
    viewModel<TaskViewModel> {
        TaskViewModel(
            addTaskUseCase = get(),
            deleteTaskUseCase = get(),
            updateTaskUseCase = get(),
            getAllTasksUseCase = get(),
            getFavouriteTasksUseCase = get(),
            getTaskByIdUseCase = get()
        )
    }

    viewModel<TaskGroupViewModel> {
        TaskGroupViewModel(
            addTaskGroupUseCase = get(),
            deleteTaskGroupUseCase = get(),
            updateTaskGroupUseCase = get(),
            getAllTaskGroupUseCase = get(),
            getTaskGroupByIdUseCase = get()
        )
    }
}