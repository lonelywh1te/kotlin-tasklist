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
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentItemListBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView.TaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class ItemListFragment(private val isFavouriteTaskList: Boolean) : Fragment(), TaskClickListener {
    private lateinit var binding: FragmentItemListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentItemListBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter(this)

        recycler = binding.rvTaskList
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        val mediatorLiveData = MediatorLiveData<Pair<List<Task>, List<TaskGroup>>>()

        mediatorLiveData.addSource(viewModel.taskList) { taskList ->
            val taskGroupList = viewModel.getTaskGroupList()
            mediatorLiveData.value = Pair(taskList, taskGroupList)
        }

        mediatorLiveData.addSource(viewModel.taskGroupList) { taskGroupList ->
            val taskList = viewModel.getTaskList()
            mediatorLiveData.value = Pair(taskList, taskGroupList)
        }

        mediatorLiveData.observe(viewLifecycleOwner) { (taskList, taskListGroup) ->
            adapter.updateTaskList(taskList, taskListGroup)

            binding.tvIsEmptyList.visibility = if (viewModel.getTaskList().isEmpty() && viewModel.getTaskGroupList().isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onResume() {
        if (isFavouriteTaskList){
            viewModel.getFavouriteTasks()
        } else {
            viewModel.getAllItems()
        }

        super.onResume()
    }

    override fun onTaskClicked(task: Task) {
        val intent = Intent(binding.root.context, TaskActivity::class.java)
        intent.putExtra("task", task)

        binding.root.context.startActivity(intent);
    }

    override fun onTaskCheckboxClicked(task: Task, isCompleted: Boolean) {
        viewModel.isFavouriteTaskList = isFavouriteTaskList
        viewModel.changeTaskCompletion(task, isCompleted)
    }
}