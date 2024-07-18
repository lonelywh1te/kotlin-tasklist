package ru.lonelywh1te.kotlin_tasklist.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.CreateTaskActivityViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.CreateTaskGroupActivityViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.FavouriteTasksFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskActivityViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupActivityViewModel

val appModule = module {
    viewModel<MainFragmentViewModel> {
        MainFragmentViewModel(
            getAllTaskItemsUseCase = get(),
            updateTaskUseCase = get()
        )
    }

    viewModel<FavouriteTasksFragmentViewModel> {
        FavouriteTasksFragmentViewModel(
            getFavouriteTasksUseCase = get(),
            updateTaskUseCase = get()
        )
    }

    viewModel<CreateTaskActivityViewModel> {
        CreateTaskActivityViewModel(
            createTaskUseCase = get()
        )
    }

    viewModel<CreateTaskGroupActivityViewModel> {
        CreateTaskGroupActivityViewModel(
            createTaskGroupUseCase = get()
        )
    }

    viewModel<TaskActivityViewModel> {
        TaskActivityViewModel(
            updateTaskUseCase = get(),
            deleteTaskUseCase = get(),
            getAllTaskGroupsUseCase = get(),
            getTaskGroupByIdUseCase = get()
        )
    }

    viewModel<TaskGroupActivityViewModel> {
        TaskGroupActivityViewModel(
            updateTaskGroupUseCase = get(),
            deleteTaskGroupUseCase = get(),
            getAllTasksUseCase = get(),
            updateTaskUseCase = get(),
            deleteTaskUseCase = get()
        )
    }
}