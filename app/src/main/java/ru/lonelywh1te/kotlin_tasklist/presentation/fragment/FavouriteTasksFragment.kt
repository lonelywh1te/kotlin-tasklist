package ru.lonelywh1te.kotlin_tasklist.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentFavouriteTasksBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter
import ru.lonelywh1te.kotlin_tasklist.presentation.callback.ItemClickListener
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.FavouriteTasksFragmentViewModel

class FavouriteTasksFragment : Fragment(), ItemClickListener {
    private lateinit var binding: FragmentFavouriteTasksBinding
    private lateinit var recycler: RecyclerView
    private val viewModel by viewModel<FavouriteTasksFragmentViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavouriteTasksBinding.inflate(layoutInflater, container, false)
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

        viewModel.favouriteTasks.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteTasks()
    }

    override fun onItemClicked(taskItem: TaskItem) {
        val task = taskItem as Task

        val direction = TasksFragmentDirections.toTaskFragment(task.id, task.title)
        findNavController().navigate(direction)
    }

    override fun onTaskCheckboxClicked(taskId: Int) {
        viewModel.completeTask(taskId)
    }
}