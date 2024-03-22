package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.data.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentTaskListBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class TaskListFragment(private val isFavouriteTaskList: Boolean) : Fragment(), TaskClickListener {
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
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

        viewModel.taskList.observe(viewLifecycleOwner) {
            Log.println(Log.DEBUG, "fragment", "UPDATED: r${viewModel.taskList.value}")
            val currentList = viewModel.getTaskList()
            adapter.updateTaskList(currentList)

            binding.tvIsEmptyList.visibility = if (currentList.isEmpty()) {
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
            viewModel.getAllTasks()
        }

        super.onResume()
    }

    override fun onTaskClicked(task: Task) {
        val intent = Intent(binding.root.context, TaskActivity::class.java)
        intent.putExtra("task", task)

        binding.root.context.startActivity(intent);
    }

    override fun onTaskCheckboxClicked(id: Int, isCompleted: Boolean) {
        viewModel.isFavouriteTaskList = isFavouriteTaskList
        viewModel.changeTaskCompletion(id, isCompleted)
    }
}