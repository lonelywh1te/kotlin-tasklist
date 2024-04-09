package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentItemListBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.OnItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

const val ARG_IS_FAVOURITE_LIST = "isFavList"

class ItemListFragment: Fragment() {
    private var isFavouriteTaskList: Boolean? = null

    private lateinit var binding: FragmentItemListBinding
    private lateinit var recycler: RecyclerView
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskGroupViewModel: TaskGroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentItemListBinding.inflate(layoutInflater, container, false)
        isFavouriteTaskList = arguments?.getBoolean(ARG_IS_FAVOURITE_LIST)

        taskViewModel = getViewModel()
        taskGroupViewModel = getViewModel()

        taskViewModel.isFavouriteTaskList = (isFavouriteTaskList == true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter(OnItemClickListener(requireContext(), taskViewModel))

        recycler = binding.rvTaskList
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        val mediatorLiveData = MediatorLiveData<Pair<List<Task>, List<TaskGroup>>>()

        mediatorLiveData.addSource(taskViewModel.taskList) { taskList ->
            val taskGroupList = taskGroupViewModel.getTaskGroupList()
            mediatorLiveData.value = Pair(taskList, taskGroupList)
        }

        mediatorLiveData.addSource(taskGroupViewModel.taskGroupList) { taskGroupList ->
            val taskList = taskViewModel.getTaskList()
            mediatorLiveData.value = Pair(taskList, taskGroupList)
        }

        mediatorLiveData.observe(viewLifecycleOwner) { (taskList, taskListGroup) ->
            adapter.updateTaskList(taskList, taskListGroup)
            binding.tvIsEmptyList.visibility = if (taskViewModel.getTaskList().isEmpty() && taskGroupViewModel.getTaskGroupList().isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onResume() {
        taskViewModel.isFavouriteTaskList = (isFavouriteTaskList == true)
        if (isFavouriteTaskList == true){
            taskViewModel.getFavouriteTasks()
        } else {
            taskViewModel.getAllTasks()
            taskGroupViewModel.getAllTaskGroups()
        }

        super.onResume()
    }

    companion object {
        fun newInstance(isFavouriteTaskList: Boolean): ItemListFragment {
            val instance = ItemListFragment()
            val arguments = Bundle()
            arguments.putBoolean(ARG_IS_FAVOURITE_LIST, isFavouriteTaskList)
            instance.arguments = arguments

            return instance
        }
    }
}