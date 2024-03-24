package ru.lonelywh1te.kotlin_tasklist.presentation.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.TaskGroupItemBinding
import ru.lonelywh1te.kotlin_tasklist.databinding.TaskItemBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.view.TaskActivity
import ru.lonelywh1te.kotlin_tasklist.presentation.view.TaskGroupActivity

interface TaskClickListener {
    fun onTaskClicked(task: Task)
    fun onTaskCheckboxClicked(task: Task, isCompleted: Boolean)
}

class TaskAdapter(private val taskClickListener: TaskClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var taskItems = listOf<TaskItem>()
    fun updateTaskList(taskList: List<Task>, taskGroup: List<TaskGroup>) {
        val newTaskItems = mutableListOf<TaskItem>()
        newTaskItems.addAll(taskGroup)
        newTaskItems.addAll(taskList)

        setTaskItems(newTaskItems)
    }

    fun updateTaskList(taskList: List<Task>) {
        val newTaskItems = mutableListOf<TaskItem>()
        newTaskItems.addAll(taskList)

        setTaskItems(newTaskItems)
    }

    private fun setTaskItems(newTaskItems: List<TaskItem>) {
        val diffCallback = TaskCallback(taskItems, newTaskItems)
        val diffTasks = DiffUtil.calculateDiff(diffCallback)

        taskItems = newTaskItems

        diffTasks.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (taskItems[position]) {
            is Task -> R.layout.task_item
            is TaskGroup -> R.layout.task_group_item
            else -> throw IllegalAccessException("Invalid View Type")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.task_item -> TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false))
            R.layout.task_group_item ->  TaskGroupViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_group_item, parent, false))
            else -> {
                throw IllegalAccessException("Invalid View Type")
            }
        }
    }

    class TaskCallback(private val oldList: List<TaskItem>, private val newList: List<TaskItem>): DiffUtil.Callback() {
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
                return oldItem == newItem
            } else if (oldItem is TaskGroup && newItem is TaskGroup) {
                return oldItem == newItem
            }

            return false
        }
    }

    override fun getItemCount(): Int {
        return taskItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = taskItems[position]
        Log.println(Log.DEBUG, "recycler", "onBindViewHolder")
        when(holder) {
            is TaskViewHolder -> {
                item as Task
                val binding = TaskItemBinding.bind(holder.itemView)

                binding.root.setOnClickListener {
                    Log.println(Log.DEBUG, "TASK_ADAPTER", "task clicked!")
                    taskClickListener.onTaskClicked(item)
                }

                binding.cbCompleteTask.setOnClickListener {
                    taskClickListener.onTaskCheckboxClicked(item, !item.isCompleted)
                }

                holder.bind(item)
            }

            is TaskGroupViewHolder -> {
                val taskGroup = item as TaskGroup
                val binding = TaskGroupItemBinding.bind(holder.itemView)

                binding.root.setOnClickListener {
                    Log.println(Log.DEBUG, "TASK_ADAPTER", "taskGroup clicked!")
                    val intent = Intent(binding.root.context, TaskGroupActivity::class.java)
                    intent.putExtra("taskGroup", taskGroup)

                    binding.root.context.startActivity(intent);
                }

                holder.bind(taskGroup)
            }
        }
    }

    class TaskViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = TaskItemBinding.bind(item)

        fun bind(task: Task) {
            Log.println(Log.DEBUG, "recycler", "${task.id} | ${task.isCompleted}")
            binding.tvTaskTitle.text = task.title
            binding.cbCompleteTask.isChecked = task.isCompleted
            binding.taskCard.alpha =  if (task.isCompleted) 0.3f else 1.0f
        }
    }

    class TaskGroupViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = TaskGroupItemBinding.bind(item)

        fun bind(taskGroup: TaskGroup) {
            binding.tvTaskGroupName.text = taskGroup.name
        }
    }
}