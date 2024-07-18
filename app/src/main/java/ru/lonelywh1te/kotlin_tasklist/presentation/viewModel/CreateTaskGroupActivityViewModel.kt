package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.CreateTaskGroupUseCase

class CreateTaskGroupActivityViewModel(
    private val createTaskGroupUseCase: CreateTaskGroupUseCase
): ViewModel() {
    private val dispatcher = Dispatchers.IO

    fun createTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch (dispatcher) {
            createTaskGroupUseCase.execute(taskGroup)
        }
    }
}