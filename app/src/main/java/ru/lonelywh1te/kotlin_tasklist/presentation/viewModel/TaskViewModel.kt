package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.AddTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetFavouriteTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetTaskByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class TaskViewModel(
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getFavouriteTasksUseCase: GetFavouriteTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
): ViewModel() {
    val task = MutableLiveData<Task>()
    val taskList = MutableLiveData<List<Task>>()

    var isFavouriteTaskList = false

    fun getTaskList(): List<Task> {
        return taskList.value.orEmpty()
    }

    fun getAllTasks() {
        viewModelScope.launch {
            if (isFavouriteTaskList) {
                getFavouriteTasks()
            } else {
                taskList.postValue(getAllTasksUseCase.execute())
            }
        }
    }

    fun getAllTasks(taskGroupId: Int?) {
        viewModelScope.launch {
            taskList.postValue(getAllTasksUseCase.execute(taskGroupId))
        }
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            task.postValue(getTaskByIdUseCase.execute(id))
        }
    }

    fun getFavouriteTasks() {
        viewModelScope.launch {
            taskList.postValue(getFavouriteTasksUseCase.execute())
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase.execute(task)
            getAllTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase.execute(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase.execute(task)

            if (task.taskGroupId == null || isFavouriteTaskList) getAllTasks()
            else getAllTasks(task.taskGroupId)
        }
    }

    fun changeTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch (Dispatchers.IO) {
            updateTask(task.copy(isCompleted = isCompleted))
        }
    }
}