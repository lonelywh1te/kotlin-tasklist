package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.GetAllTaskItemsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class MainFragmentViewModel(
    private val getAllTaskItemsUseCase: GetAllTaskItemsUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
): ViewModel(), TaskViewModel {
    val taskItems = MutableLiveData<List<TaskItem>>()
    private val dispatcher = Dispatchers.IO

    fun getAllTaskItems() {
        viewModelScope.launch (dispatcher) {
            taskItems.postValue(getAllTaskItemsUseCase.execute())
        }
    }

    override fun completeTask(task: Task) {
        viewModelScope.launch (dispatcher){
            updateTaskUseCase.execute(task.copy(isCompleted = !task.isCompleted))
            getAllTaskItems()
        }
    }
}