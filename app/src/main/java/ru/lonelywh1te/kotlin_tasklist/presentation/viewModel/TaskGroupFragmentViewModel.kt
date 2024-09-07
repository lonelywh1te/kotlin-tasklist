package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.DeleteTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.GetTaskGroupByIdUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskGroupUseCases.UpdateTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.CompleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.DeleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetAllTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.MoveTaskToTaskGroupUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class TaskGroupFragmentViewModel(
    private val getTaskGroupByIdUseCase: GetTaskGroupByIdUseCase,
    private val updateTaskGroupUseCase: UpdateTaskGroupUseCase,
    private val deleteTaskGroupUseCase: DeleteTaskGroupUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val moveTaskToTaskGroupUseCase: MoveTaskToTaskGroupUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
): ViewModel(), TaskCompleter {
    var taskGroupId: Int = -1
        set(id) {
            if (field == id) return
            field = id

            getTaskGroup()
            getAllTasks()
        }

    private val _taskGroup = MutableLiveData<TaskGroup>()
    val taskGroup: LiveData<TaskGroup> get() = _taskGroup

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val dispatcher = Dispatchers.IO

    private fun getTaskGroup() {
        viewModelScope.launch (dispatcher) {
            _taskGroup.postValue(getTaskGroupByIdUseCase.execute(taskGroupId))
        }
    }

    fun updateTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch (dispatcher) {
            updateTaskGroupUseCase.execute(taskGroup)
        }
    }

    fun deleteTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch (dispatcher) {
            deleteTaskGroupUseCase.execute(taskGroup)
        }
    }

    fun updateTaskOrder(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) return

        viewModelScope.launch(dispatcher) {
            val currentList = _tasks.value?.toMutableList() ?: mutableListOf()

            val movedTask = currentList.removeAt(fromPosition)
            currentList.add(toPosition, movedTask)

            currentList.forEachIndexed { index, task ->
                updateTaskUseCase.execute(task.copy(order = index))
            }

            getAllTasks()
        }
    }

    fun getAllTasks() {
        viewModelScope.launch (dispatcher) {
            _tasks.postValue(getAllTasksUseCase.execute(taskGroupId))
        }
    }

    fun deleteTasks() {
        viewModelScope.launch (dispatcher) {
            tasks.value?.forEach { deleteTaskUseCase.execute(it) }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch (dispatcher) {
            deleteTaskUseCase.execute(task)
            getAllTasks()
        }
    }

    override fun completeTask(id: Int) {
        viewModelScope.launch (dispatcher){
            completeTaskUseCase.execute(id)
            getAllTasks()
        }
    }
}