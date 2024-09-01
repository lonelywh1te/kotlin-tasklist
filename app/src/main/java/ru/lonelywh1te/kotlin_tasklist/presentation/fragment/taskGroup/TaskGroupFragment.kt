package ru.lonelywh1te.kotlin_tasklist.presentation.fragment.taskGroup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.callback.ItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.fragment.task.CreateTaskBottomFragment
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupFragmentViewModel

private const val LOG_TAG = "TaskGroupFragment"

class TaskGroupFragment : Fragment(), ItemClickListener {
    private lateinit var binding: FragmentTaskGroupBinding
    private lateinit var recycler: RecyclerView
    private val viewModel by viewModel<TaskGroupFragmentViewModel>()
    private val args: TaskGroupFragmentArgs by navArgs()

    private var taskGroup = TaskGroup("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskGroup = taskGroup.copy(id = args.id)
        viewModel.getTaskGroupById(taskGroup.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter(this)

        recycler = binding.rvTaskGroupTasks
        recycler.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.taskGroup.observe(viewLifecycleOwner) {
            taskGroup = it
            updateUI()
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        binding.btnAddTaskToGroup.setOnClickListener {
            val createTaskFragment = CreateTaskBottomFragment.newInstance(taskGroup.id)

            createTaskFragment.onCreateTask {
                viewModel.getAllTasksById(taskGroup.id)
            }

            createTaskFragment.show(parentFragmentManager, createTaskFragment.tag)
        }

        binding.btnDeleteTaskGroup.setOnClickListener {
            deleteTaskGroup()
        }
    }

    override fun onPause() {
        super.onPause()
        updateTaskGroup(taskGroup)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTasksById(taskGroup.id)
    }

    private fun updateTaskGroup(taskGroup: TaskGroup) {
        val desc = binding.tvTaskGroupDesc.text.toString()
        viewModel.updateTaskGroup(taskGroup.copy(description = desc))
        updateUI()
    }

    private fun updateUI() {
        binding.tvTaskGroupDesc.setText(taskGroup.description)
    }


    private fun deleteTaskGroup() {
        viewModel.deleteTaskGroup(taskGroup)
        findNavController().popBackStack()
    }

    override fun onItemClicked(taskItem: TaskItem) {
        val task = taskItem as Task
        val direction = TaskGroupFragmentDirections.toTaskFragment(task.id, task.title)

        findNavController().navigate(direction)
    }

    override fun onTaskCheckboxClicked(task: Task) {
        viewModel.completeTask(task)
    }
}