package ru.lonelywh1te.kotlin_tasklist.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentTaskListBinding
import ru.lonelywh1te.kotlin_tasklist.data.Task
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TaskAdapter

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)

        // TODO: delete
        val taskList = listOf(
            Task("Спать", "Описание какое то"),
            Task("Купить хлеб", "Че то еще надо сделать"),
            Task("Научиться сальто", "а мимир", true),
        )

        val adapter = TaskAdapter(taskList)

        recycler = binding.rvTaskList
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        return binding.root
    }
}