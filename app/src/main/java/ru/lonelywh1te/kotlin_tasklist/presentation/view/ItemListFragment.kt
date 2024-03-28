package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentItemListBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.ItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView.TaskGroupActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.TaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class ItemListFragment(private val isFavouriteTaskList: Boolean) : Fragment() {
    private lateinit var binding: FragmentItemListBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskGroupViewModel: TaskGroupViewModel
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentItemListBinding.inflate(layoutInflater, container, false)
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskGroupViewModel = ViewModelProvider(this)[TaskGroupViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter(object: ItemClickListener {

            override fun onItemClicked(taskItem: TaskItem) {
                val intent: Intent = when(taskItem) {
                    is Task -> {
                        Intent(binding.root.context, TaskActivity::class.java).apply {
                            putExtra("task", taskItem)
                        }
                    }
                    is TaskGroup -> {
                        Intent(binding.root.context, TaskGroupActivity::class.java).apply {
                            putExtra("taskGroup", taskItem)
                        }
                    }
                    else -> throw IllegalAccessException("Invalid View Type")
                }

                binding.root.context.startActivity(intent);
            }

            override fun onTaskCheckboxClicked(task: Task, isCompleted: Boolean) {
                taskViewModel.isFavouriteTaskList = isFavouriteTaskList
                taskViewModel.changeTaskCompletion(task, isCompleted)
            }
        })

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
        if (isFavouriteTaskList){
            taskViewModel.getFavouriteTasks()
        } else {
            taskViewModel.getAllTasks()
            taskGroupViewModel.getAllTaskGroups()
        }

        super.onResume()
    }
}