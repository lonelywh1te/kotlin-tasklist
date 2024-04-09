package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var task: Task

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.println(Log.DEBUG, "kotlin-tasklist", "CREATED")
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        taskViewModel = getViewModel()

        task = intent.extras?.getSerializable("task") as Task
        notificationViewModel = NotificationViewModel(this)

        setContentView(binding.root)

        binding.btnDeleteTask.setOnClickListener {
            cancelTaskNotification()
            deleteTask()
        }

        binding.btnEditTask.setOnClickListener {
            val intent = Intent(this, CreateEditTaskActivity::class.java)
            intent.putExtra("editMode", true)
            intent.putExtra("task", task)
            startActivity(intent)
        }

        taskViewModel.task.observe(this) {
            task = it
            setTaskData()
        }
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.getTaskById(task.id)
    }

    private fun setTaskData() {
        binding.tvTaskTitle.text = task.title
        binding.tvTaskDescription.text = task.description

        if (task.completionDateInMillis != null) {
            binding.tvTaskCompletionDate.text = DateUtils.normalDateFormat(task.completionDateInMillis!!)
            binding.tvTaskCompletionDate.visibility = View.VISIBLE
        } else {
            binding.tvTaskCompletionDate.visibility = View.GONE
        }

        binding.tvIsFavouriteStatus.visibility = if (task.isFavourite) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun deleteTask() {
        taskViewModel.deleteTask(task)
        finish()
    }

    private fun cancelTaskNotification() {
        if (task.completionDateInMillis != null) {
            notificationViewModel.cancelTaskNotification(task, task.completionDateInMillis!! - 86400000)
        }
    }
}