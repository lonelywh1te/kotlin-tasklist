package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateEditTaskBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskViewModel

class CreateEditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEditTaskBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var task: Task
    private var editMode = false

    private var completionDate: Long? = null
    private var deletedCompletionDate: Long? = null

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditTaskBinding.inflate(layoutInflater)

        taskViewModel = getViewModel()
        notificationViewModel = NotificationViewModel(this)

        val taskGroupId = intent.extras?.getInt("taskGroupId")

        editMode = intent.extras?.getBoolean("editMode") ?: false

        if (editMode) {
            task = intent.extras?.getSerializable("task") as Task
            completionDate = task.completionDateInMillis
        }

        setActivityMode()
        setContentView(binding.root)

        binding.btnAddTask.setOnClickListener {
            val title = binding.inputTaskTitle.text.toString()
            val description = binding.inputTaskDescription.text.toString()
            val isFavourite = binding.cbIsFavourive.isChecked

            handleTaskOperation(title, description, isFavourite, taskGroupId)
        }

        binding.btnSaveTaskChanges.setOnClickListener {
            val title = binding.inputTaskTitle.text.toString()
            val description = binding.inputTaskDescription.text.toString()
            val isFavourite = binding.cbIsFavourive.isChecked

            handleTaskOperation(title, description, isFavourite, taskGroupId)
        }

        binding.btnSetTaskCompletionDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val timePicker = TimePickerDialog(this, { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                completionDate = calendar.timeInMillis

                binding.btnResetTaskDeadline.visibility = View.VISIBLE
                binding.btnSetTaskCompletionDate.text = DateUtils.normalDateFormat(completionDate!!)

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

            val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)

                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.btnResetTaskDeadline.setOnClickListener {
            deletedCompletionDate = completionDate
            completionDate = null
            binding.btnResetTaskDeadline.visibility = View.GONE
            binding.btnSetTaskCompletionDate.text = getString(R.string.set)
        }

        binding.btnRestoreTaskChanges.setOnClickListener {
            finish()
        }
    }

    private fun setActivityMode() {
        binding.tvCreateActivityTitle.text = if (editMode) getString(R.string.editTask) else getString(R.string.newTask)
        binding.btnAddTask.visibility = if (editMode) View.GONE else View.VISIBLE
        binding.btnRestoreTaskChanges.visibility = if (!editMode) View.GONE else View.VISIBLE
        binding.btnSaveTaskChanges.visibility = if (!editMode) View.GONE else View.VISIBLE

        if (editMode) {
            binding.inputTaskTitle.setText(task.title)
            binding.inputTaskDescription.setText(task.description)
            binding.cbIsFavourive.isChecked = task.isFavourite

            if (task.completionDateInMillis != null) {
                binding.btnResetTaskDeadline.visibility = View.VISIBLE
                binding.btnSetTaskCompletionDate.text = DateUtils.normalDateFormat(task.completionDateInMillis!!)
            }
        }
    }

    private fun setTaskNotification() {
        notificationViewModel.setTaskNotification(task, completionDate!!)
    }

    private fun cancelTaskNotification() {
        notificationViewModel.cancelTaskNotification(task, deletedCompletionDate!!)
    }

    private fun createTask(title: String, description: String, isFavourite: Boolean, completionDate: Long?, taskGroupId: Int?) {
        task = Task(title, description, isFavourite, completionDateInMillis = completionDate, taskGroupId = taskGroupId)
        taskViewModel.addTask(task)
        finish()
    }

    private fun updateTask(title: String, description: String, isFavourite: Boolean, completionDate: Long?) {
        task = Task(title, description, isFavourite, task.isCompleted, completionDate, task.taskGroupId, task.id)
        taskViewModel.updateTask(task)
    }

    private fun handleTaskOperation(title: String, description: String, isFavourite: Boolean, taskGroupId: Int?) {
        if (title.isBlank()) {
            binding.inputTaskTitle.error = getString(R.string.enterTitle)
            return
        }

        if (!editMode) {
            createTask(title, description, isFavourite, completionDate, taskGroupId)
        } else {
            updateTask(title, description, isFavourite, completionDate)
            finish()
        }

        if (completionDate != null) {
            setTaskNotification()
        } else if (deletedCompletionDate != null){
            cancelTaskNotification()
        }
    }
}