package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateTaskBinding
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TASK_NAME_EXTRA
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var notificationViewModel: NotificationViewModel

    private var task = Task("", "")
    private var taskGroupId: Int? = null
    private var completionDate: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)

        taskViewModel = getViewModel()
        notificationViewModel = NotificationViewModel(this)

        taskGroupId = intent.extras?.getInt("taskGroupId")

        binding.btnAddTask.setOnClickListener {
            createTask()

            if (task.title.isBlank()) {
                binding.inputTaskTitle.error = getString(R.string.enterTitle)
                return@setOnClickListener
            }

            if (completionDate != null) notificationViewModel.setTaskNotification(task, completionDate!!)
            taskViewModel.addTask(task)

            finish()
        }

        binding.btnSetTaskCompletionDate.setOnClickListener {
            setTaskCompletionDate()
        }

        setContentView(binding.root)
    }

    private fun createTask() {
        val title = binding.inputTaskTitle.text.toString()
        val description = binding.inputTaskDescription.text.toString()
        val isFavourite = binding.cbIsFavourive.isChecked
        val completionDateInMillis = completionDate

        task = task.copy(title = title, description = description, isFavourite = isFavourite, completionDateInMillis = completionDateInMillis, taskGroupId = taskGroupId)
    }

    private fun openTaskActivity() {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TASK_NAME_EXTRA, task)
        startActivity(intent)
    }

    private fun setTaskCompletionDate() {
        val calendar = Calendar.getInstance()

        val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            completionDate = calendar.timeInMillis

            binding.btnSetTaskCompletionDate.text = DateUtils.normalDateFormat(calendar.timeInMillis)

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)

            timePicker.show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        if (completionDate != null) {
            datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Удалить") { _, _ ->
                completionDate = null
                binding.btnSetTaskCompletionDate.text = "Назначить"
            }
        }

        datePickerDialog.show()
    }
}