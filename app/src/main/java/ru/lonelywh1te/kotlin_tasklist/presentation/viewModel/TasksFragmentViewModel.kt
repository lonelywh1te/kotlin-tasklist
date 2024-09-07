package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.GetAllTaskItemsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.UpdateTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.CompleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

private const val LOG_TAG = "TasksFragmentViewModel"

class TasksFragmentViewModel(
    private val getAllTaskItemsUseCase: GetAllTaskItemsUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val updateTaskGroupUseCase: UpdateTaskGroupUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
): ViewModel(), TaskCompleter {
    private val _taskItems = MutableLiveData<List<TaskItem>>()
    val taskItems: LiveData<List<TaskItem>> get() = _taskItems

    private val dispatcher = Dispatchers.IO

    fun getAllTaskItems() {
        viewModelScope.launch (dispatcher) {
            _taskItems.postValue(getAllTaskItemsUseCase.execute())
        }
    }

    override fun completeTask(id: Int) {
        viewModelScope.launch (dispatcher){
            completeTaskUseCase.execute(id)
            getAllTaskItems()
        }
    }

    fun updateItemOrder(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) return

        viewModelScope.launch(dispatcher) {
            val currentList = _taskItems.value?.toMutableList() ?: mutableListOf()

            val movedItem = currentList.removeAt(fromPosition)
            currentList.add(toPosition, movedItem)

            when (movedItem) {
                is Task -> {
                    currentList.filterIsInstance<Task>().forEachIndexed { index, task ->
                        updateTaskUseCase.execute(task.copy(order = index))
                    }
                }
                is TaskGroup -> {
                    currentList.filterIsInstance<TaskGroup>().forEachIndexed { index, taskGroup ->
                        updateTaskGroupUseCase.execute(taskGroup.copy(order = index))
                    }
                }
            }

            getAllTaskItems()
        }
    }

    init {
        getAllTaskItems()
    }
}