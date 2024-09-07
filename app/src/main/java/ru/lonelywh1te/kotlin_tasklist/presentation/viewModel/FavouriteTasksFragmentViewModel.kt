package ru.lonelywh1te.kotlin_tasklist.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.CompleteTaskUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.GetFavouriteTasksUseCase
import ru.lonelywh1te.kotlin_tasklist.domain.usecase.taskUseCases.UpdateTaskUseCase

class FavouriteTasksFragmentViewModel(
    private val getFavouriteTasksUseCase: GetFavouriteTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
) : ViewModel(), TaskCompleter {
    val favouriteTasks = MutableLiveData<List<Task>>()
    private val dispatcher = Dispatchers.IO

    fun getFavouriteTasks() {
        viewModelScope.launch(dispatcher) {
            favouriteTasks.postValue(getFavouriteTasksUseCase.execute())
        }
    }

    override fun completeTask(id: Int) {
        viewModelScope.launch(dispatcher) {
            completeTaskUseCase.execute(id)
            getFavouriteTasks()
        }
    }
}