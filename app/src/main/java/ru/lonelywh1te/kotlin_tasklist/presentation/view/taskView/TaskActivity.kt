package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TASK_NAME_EXTRA
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var task: Task

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskGroupViewModel: TaskGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)

        taskViewModel = getViewModel()
        taskGroupViewModel = getViewModel()
        notificationViewModel = NotificationViewModel(this)

        task = intent.extras?.getSerializable(TASK_NAME_EXTRA) as Task

        binding.btnDeleteTask.setOnClickListener {
            cancelTaskNotification()
            deleteTask()
        }

        binding.tvTaskCompletionDate.setOnClickListener {
            clearEditTextFocus()
            setTaskCompletionDate()
        }


        binding.ivIsFavourite.setOnClickListener {
            clearEditTextFocus()
            updateTask(task.copy(isFavourite = !task.isFavourite))
        }

        setTaskData()
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        clearEditTextFocus()
        updateTask(task)
    }

    private fun setTaskData() {
        // task data
        binding.tvTaskTitle.setText(task.title)
        binding.tvTaskDescription.setText(task.description)

        // task completion date
        if (task.completionDateInMillis != null) {
            binding.tvTaskCompletionDate.text = DateUtils.normalDateFormat(task.completionDateInMillis!!)
            binding.tvTaskCompletionDate.alpha = 1.0F
        } else {
            binding.tvTaskCompletionDate.alpha = 0.3F
            binding.tvTaskCompletionDate.text = "Добавить дату / время"
        }

        // favourite status
         if (task.isFavourite) {
             binding.ivIsFavourite.setImageDrawable(resources.getDrawable(R.drawable.ic_favourite_fill, applicationContext.theme))
        } else {
             binding.ivIsFavourite.setImageDrawable(resources.getDrawable(R.drawable.ic_favourite, applicationContext.theme))
        }
    }

    private fun updateTask(newTask: Task) {
        val title = binding.tvTaskTitle.text.toString()
        val desc = binding.tvTaskDescription.text.toString()

        task = if (newTask.title != title || newTask.description != desc){
            newTask.copy(title = title, description = desc)
        } else {
            newTask
        }

        taskViewModel.updateTask(task)
        setTaskData()
    }

    private fun deleteTask() {
        taskViewModel.deleteTask(task)
        finish()
    }

    private fun setTaskCompletionDate() {
        val calendar = Calendar.getInstance()

        val timePicker = TimePickerDialog(this, { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            setTaskNotification(calendar.timeInMillis)
            updateTask(task.copy(completionDateInMillis = calendar.timeInMillis))

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

        val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)

            timePicker.show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        if (task.completionDateInMillis != null) {
            datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Удалить", DialogInterface.OnClickListener { dialog, which ->
                cancelTaskNotification()
                updateTask(task.copy(completionDateInMillis = null))
            })
        }

        datePickerDialog.show()
    }

    private fun setTaskNotification(completionDate: Long) {
        notificationViewModel.setTaskNotification(task, completionDate)
    }

    private fun cancelTaskNotification() {
        if (task.completionDateInMillis != null) {
            notificationViewModel.cancelTaskNotification(task, task.completionDateInMillis!! - 86400000)
        }
    }

    private fun clearEditTextFocus() {
        binding.tvTaskTitle.clearFocus()
        binding.tvTaskDescription.clearFocus()
    }
}