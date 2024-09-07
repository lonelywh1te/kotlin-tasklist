package ru.lonelywh1te.kotlin_tasklist.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.CreateTaskFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.CreateTaskGroupFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.FavouriteTasksFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TasksFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupFragmentViewModel

val appModule = module {
    viewModel<TasksFragmentViewModel> {
        TasksFragmentViewModel(
            getAllTaskItemsUseCase = get(),
            updateTaskUseCase = get(),
            updateTaskGroupUseCase = get(),
            completeTaskUseCase = get(),
            deleteTaskGroupUseCase = get(),
            deleteTaskUseCase = get()
        )
    }

    viewModel<FavouriteTasksFragmentViewModel> {
        FavouriteTasksFragmentViewModel(
            getFavouriteTasksUseCase = get(),
            completeTaskUseCase = get()
        )
    }

    viewModel<CreateTaskFragmentViewModel> {
        CreateTaskFragmentViewModel(
            createTaskUseCase = get(),
        )
    }

    viewModel<CreateTaskGroupFragmentViewModel> {
        CreateTaskGroupFragmentViewModel(
            createTaskGroupUseCase = get()
        )
    }

    viewModel<TaskFragmentViewModel> {
        TaskFragmentViewModel(
            updateTaskUseCase = get(),
            deleteTaskUseCase = get(),
            getAllTaskGroupsUseCase = get(),
            getTaskGroupByIdUseCase = get(),
            getTaskByIdUseCase = get(),
            moveTaskToTaskGroupUseCase = get(),
        )
    }

    viewModel<TaskGroupFragmentViewModel> {
        TaskGroupFragmentViewModel(
            updateTaskGroupUseCase = get(),
            deleteTaskGroupUseCase = get(),
            getAllTasksUseCase = get(),
            updateTaskUseCase = get(),
            deleteTaskUseCase = get(),
            getTaskGroupByIdUseCase = get(),
            moveTaskToTaskGroupUseCase = get(),
            completeTaskUseCase = get()

        )
    }

    viewModel<NotificationViewModel> {
        NotificationViewModel(
            notificationScheduler = get(),
            app = get()
        )
    }
}