package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.GetAllTaskItemsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

private const val LOG_TAG = "TasksFragmentViewModel"

class TasksFragmentViewModel(
    private val getAllTaskItemsUseCase: GetAllTaskItemsUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
): ViewModel(), TaskCompleter {
    val taskItems = MutableLiveData<List<TaskItem>>()
    private val dispatcher = Dispatchers.IO

    fun getAllTaskItems() {
        viewModelScope.launch (dispatcher) {
            Log.d(LOG_TAG, "${getAllTaskItemsUseCase.execute()}")
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