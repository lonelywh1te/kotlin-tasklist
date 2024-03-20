package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.TaskItemBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.view.MainActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.TaskActivity


class TaskAdapter(): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private var taskList: List<Task> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)

        return ViewHolder(view)
    }

    fun updateTaskList(list: List<Task>) {
        taskList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        holder.bind(task)
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = TaskItemBinding.bind(item)

        fun bind(task: Task) {
            binding.tvTaskTitle.text = task.title
            binding.cbCompleteTask.isChecked = task.isCompleted

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, TaskActivity::class.java)
                intent.putExtra("task_title", task.title)
                intent.putExtra("task_desc", task.description)
                intent.putExtra("task_isFavourite", task.isFavourite)

                binding.root.context.startActivity(intent);
            }
        }

    }
}