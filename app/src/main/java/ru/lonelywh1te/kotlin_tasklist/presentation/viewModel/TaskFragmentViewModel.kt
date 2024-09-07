package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetAllTaskGroupsUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetTaskByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.MoveTaskToTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class TaskFragmentViewModel(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskGroupByIdUseCase: GetTaskGroupByIdUseCase,
    private val getAllTaskGroupsUseCase: GetAllTaskGroupsUseCase,
    private val moveTaskToTaskGroupUseCase: MoveTaskToTaskGroupUseCase,
) : ViewModel() {
    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> get() = _task

    private val _taskGroups = MutableLiveData<List<TaskGroup>>()
    val taskGroups: LiveData<List<TaskGroup>> get() = _taskGroups

    private val dispatcher = Dispatchers.IO

    fun getTask(id: Int) {
        viewModelScope.launch {
            _task.postValue(getTaskByIdUseCase.execute(id))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            Log.d("TaskRepository", task.order.toString())
            updateTaskUseCase.execute(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(dispatcher) {
            deleteTaskUseCase.execute(task)
        }
    }

    fun moveTaskToTaskGroup(task: Task, taskGroupId: Int?) {
        viewModelScope.launch (dispatcher) {
            moveTaskToTaskGroupUseCase.execute(task, taskGroupId)
            getTask(task.id)
        }
    }

    suspend fun getTaskGroupById(id: Int) = viewModelScope.async(dispatcher) {
        getTaskGroupByIdUseCase.execute(id)
    }

    private fun getAllTaskGroups() {
        viewModelScope.launch(dispatcher) {
            _taskGroups.postValue(getAllTaskGroupsUseCase.execute())
        }
    }

    init {
        getAllTaskGroups()
    }
}