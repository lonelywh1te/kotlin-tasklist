package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.TaskItemBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.view.TaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.MainViewModel

interface TaskClickListener {
    fun onTaskClicked(task: Task)
    fun onTaskCheckboxClicked(id: Int, isCompleted: Boolean)
}

class TaskAdapter(private val taskClickListener: TaskClickListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
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
        val binding = TaskItemBinding.bind(holder.itemView)
        val task = taskList[position]

        binding.root.setOnClickListener {
            taskClickListener.onTaskClicked(task)
        }

        binding.cbCompleteTask.setOnCheckedChangeListener { _, isChecked ->
            taskClickListener.onTaskCheckboxClicked(task.id, isChecked)
        }

        holder.bind(task)
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = TaskItemBinding.bind(item)

        fun bind(task: Task) {
            Log.println(Log.DEBUG, "TaskAdapter", "${task.id} | ${task.isCompleted}")
            binding.tvTaskTitle.text = task.title
            binding.cbCompleteTask.isChecked = task.isCompleted
            binding.taskCard.alpha =  if (task.isCompleted) 0.3f else 1.0f
        }
    }
}