package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskItem
import ru.lonelywh1te.kotlin_tasklist.databinding.TaskGroupItemBinding
import ru.lonelywh1te.kotlin_tasklist.databinding.TaskItemBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.callback.ItemClickListener

private const val LOG_TAG = "TaskAdapter"

class TaskDiffUtil(private val oldList: List<TaskItem>, private val newList: List<TaskItem>): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem is Task && newItem is Task) {
            return oldItem.id == newItem.id
        } else if (oldItem is TaskGroup && newItem is TaskGroup) {
            return oldItem.id == newItem.id
        }

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem is Task && newItem is Task) {
            return oldItem.title == newItem.title &&
                    oldItem.isCompleted == newItem.isCompleted &&
                    oldItem.completionDateInMillis == newItem.completionDateInMillis
        } else if (oldItem is TaskGroup && newItem is TaskGroup) {
            return oldItem.name == newItem.name
        }

        return false
    }
}

class TaskAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var taskItems: List<TaskItem> = emptyList()
        set(newList) {
            val diffCallback = TaskDiffUtil(taskItems, newList)
            val diffTasks = DiffUtil.calculateDiff(diffCallback)

            field = newList

            diffTasks.dispatchUpdatesTo(this)
        }

    fun updateList(taskItems: List<TaskItem>) {
        this.taskItems = taskItems
    }

    override fun getItemViewType(position: Int): Int {
        return when (taskItems[position]) {
            is Task -> R.layout.task_item
            is TaskGroup -> R.layout.task_group_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val taskBinding = TaskItemBinding.inflate(inflater, parent, false)
        val taskGroupBinding = TaskGroupItemBinding.inflate(inflater, parent, false)

        return when(viewType) {
            R.layout.task_item -> TaskViewHolder(taskBinding)
            R.layout.task_group_item -> TaskGroupViewHolder(taskGroupBinding)
            else -> throw IllegalAccessException("Invalid View Type")
        }
    }

    override fun getItemCount(): Int {
        return taskItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {

        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = taskItems[position]

        when(holder) {
            is TaskViewHolder -> {
                val task = item as Task
                val binding = TaskItemBinding.bind(holder.itemView)

                binding.root.setOnClickListener {
                    itemClickListener.onItemClicked(task)
                }

                binding.cbCompleteTask.setOnClickListener {
                    itemClickListener.onTaskCheckboxClicked(task.id)
                }

                holder.bind(task)
            }

            is TaskGroupViewHolder -> {
                val taskGroup = item as TaskGroup
                val binding = TaskGroupItemBinding.bind(holder.itemView)

                binding.root.setOnClickListener {
                    itemClickListener.onItemClicked(item)
                }

                holder.bind(taskGroup)
            }
        }
    }


    fun itemMoved(oldPosition: Int, newPosition: Int) {
        val currentList = taskItems.toMutableList()

        val movedTask = currentList.removeAt(oldPosition)
        currentList.add(newPosition, movedTask)

        taskItems = currentList
    }

    fun getItem(position: Int): TaskItem {
        return taskItems[position]
    }

    class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTaskTitle.text = task.title
            binding.cbCompleteTask.isChecked = task.isCompleted
            binding.taskItem.alpha =  if (task.isCompleted) 0.3f else 1.0f

            if (task.completionDateInMillis != null) {
                binding.tvTaskCompletionDate.text = DateUtils.normalDateFormat(task.completionDateInMillis)
                binding.tvTaskCompletionDate.visibility = View.VISIBLE
            } else {
                binding.tvTaskCompletionDate.visibility = View.GONE
            }
        }
    }

    class TaskGroupViewHolder(private val binding: TaskGroupItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(taskGroup: TaskGroup) {
            binding.tvTaskGroupName.text = taskGroup.name
        }
    }
}