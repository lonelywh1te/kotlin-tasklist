package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetAllTaskGroupsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetTaskByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class TaskFragmentViewModel(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskGroupByIdUseCase: GetTaskGroupByIdUseCase,
    private val getAllTaskGroupsUseCase: GetAllTaskGroupsUseCase
) : ViewModel() {

    val task = MutableLiveData<Task>()
    val taskGroups = MutableLiveData<List<TaskGroup>>()

    private val dispatcher = Dispatchers.IO

    fun getTask(id: Int) {
        viewModelScope.launch {
            task.postValue(getTaskByIdUseCase.execute(id))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            updateTaskUseCase.execute(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            deleteTaskUseCase.execute(task)
        }
    }

    suspend fun getTaskGroupById(id: Int) = viewModelScope.async(dispatcher) {
        getTaskGroupByIdUseCase.execute(id)
    }

    private fun getAllTaskGroups() {
        viewModelScope.launch(dispatcher) {
            taskGroups.postValue(getAllTaskGroupsUseCase.execute())
        }
    }

    init {
        getAllTaskGroups()
    }
}