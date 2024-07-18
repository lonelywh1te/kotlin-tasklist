package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.CreateTaskUseCase

class CreateTaskActivityViewModel(
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {
    private val dispatcher = Dispatchers.IO

    fun createTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            createTaskUseCase.execute(task)
        }
    }
}