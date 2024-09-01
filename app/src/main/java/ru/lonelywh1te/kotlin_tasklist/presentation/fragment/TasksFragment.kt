package ru.lonelywh1te.kotlin_tasklist.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentTasksBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.callback.ItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.fragment.task.CreateTaskBottomFragment
import ru.lonelywh1te.kotlin_tasklist.presentation.fragment.taskGroup.CreateTaskGroupBottomFragment
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TasksFragmentViewModel

private const val LOG_TAG = "TasksFragment"

class TasksFragment: Fragment(), ItemClickListener {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var recycler: RecyclerView
    private val viewModel: TasksFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, viewModel.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTasksBinding.inflate(layoutInflater, container, false)
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

        binding.btnCreateItem.setOnClickListener {
            if (binding.btnCreateTask.visibility == View.GONE) openFab() else hideFab()
        }

        binding.btnCreateTask.setOnClickListener {
            val bottomFragment = CreateTaskBottomFragment.newInstance()

            bottomFragment.onCreateTask {
                Log.d(LOG_TAG, viewModel.toString())
                viewModel.getAllTaskItems()
            }

            bottomFragment.show(parentFragmentManager, bottomFragment.tag)
            hideFab()
        }

        binding.btnCreateTaskGroup.setOnClickListener {
            val bottomFragment = CreateTaskGroupBottomFragment()

            bottomFragment.onCreateTaskGroup {
                viewModel.getAllTaskItems()
            }

            bottomFragment.show(parentFragmentManager, bottomFragment.tag)
            hideFab()
        }

        viewModel.taskItems.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }
    }

    private fun openFab() {
        binding.btnCreateTask.visibility = View.VISIBLE
        binding.btnCreateTaskGroup.visibility = View.VISIBLE

        binding.btnCreateItem.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_hide))
    }

    private fun hideFab() {
        binding.btnCreateTask.visibility = View.GONE
        binding.btnCreateTaskGroup.visibility = View.GONE

        binding.btnCreateItem.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_plus))
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTaskItems()
    }

    override fun onItemClicked(taskItem: TaskItem) {
        when(taskItem) {
            is Task -> {
                val direction = TasksFragmentDirections.toTaskFragment(taskItem.id, taskItem.title)
                findNavController().navigate(direction)
            }
            is TaskGroup -> {
                val direction = TasksFragmentDirections.toTaskGroupFragment(taskItem.id, taskItem.name)
                findNavController().navigate(direction)
            }
        }
    }

    override fun onTaskCheckboxClicked(task: Task) {
        viewModel.completeTask(task)
    }
}