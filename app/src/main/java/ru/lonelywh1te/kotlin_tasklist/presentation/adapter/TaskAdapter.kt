package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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

    class TaskCallback(private val oldList: List<Task>, private val newList: List<Task>): DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldTask = oldList[oldItemPosition]
            val newTask = newList[newItemPosition]
            return oldTask.id == newTask.id && oldTask.title == newTask.title && oldTask.isCompleted == newTask.isCompleted
        }

    }
    fun updateTaskList(list: List<Task>) {
        val diffCallback = TaskCallback(taskList, list)
        val diffTasks = DiffUtil.calculateDiff(diffCallback)

        taskList = list
        
        diffTasks.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = taskList.size

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
                intent.putExtra("task", task)

                binding.root.context.startActivity(intent);
            }
        }

    }
}