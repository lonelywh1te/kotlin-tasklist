package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentTaskListBinding
import ru.lonelywh1te.kotlin_tasklist.data.Task
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.refreshTasks()

        val adapter = TaskAdapter()

        recycler = binding.rvTaskList
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.taskList.observe(viewLifecycleOwner) {
            adapter.updateTaskList(viewModel.getTaskList())
        }

        Log.println(Log.DEBUG, "fragment", "CREATED")
        return binding.root
    }

    override fun onPause() {
        Log.println(Log.DEBUG, "fragment", "PAUSED")
        super.onPause()
    }
    override fun onResume() {
        Log.println(Log.DEBUG, "fragment", "RESUMED")
        viewModel.refreshTasks()
        super.onResume()
    }
}