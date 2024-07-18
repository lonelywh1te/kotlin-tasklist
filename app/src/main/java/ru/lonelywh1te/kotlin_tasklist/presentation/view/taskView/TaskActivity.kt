package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskView

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityTaskBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.adapter.TASK_NAME_EXTRA
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskActivityViewModel

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var task: Task

    private lateinit var taskActivityViewModel: TaskActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)

        taskActivityViewModel = getViewModel()
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

        binding.tvTaskGroupInfo.setOnClickListener {
            setTaskGroup()
        }

        binding.ivIsFavourite.setOnClickListener {
            clearEditTextFocus()
            updateTask(task.copy(isFavourite = !task.isFavourite))
        }

        updateTaskUI()
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        clearEditTextFocus()
        updateTask(task)
    }

    private fun updateTaskUI() {
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

        // task group
        if (task.taskGroupId != null) lifecycleScope.launch {
            val taskGroup = taskActivityViewModel.getTaskGroupById(task.taskGroupId!!).await()

            withContext(Dispatchers.Main) {
                binding.tvTaskGroupInfo.alpha = 1.0F
                binding.tvTaskGroupInfo.text = taskGroup.name
            }
        } else {
            binding.tvTaskGroupInfo.alpha = 0.3F
            binding.tvTaskGroupInfo.text = "Добавить группу"
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

        taskActivityViewModel.updateTask(task)
        updateTaskUI()
    }

    private fun deleteTask() {
        taskActivityViewModel.deleteTask(task)
        finish()
    }

    // TODO: Calendar -> LocalDate
    private fun setTaskCompletionDate() {
        val calendar = Calendar.getInstance()

        val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
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
            datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Удалить") { _, _ ->
                cancelTaskNotification()
                updateTask(task.copy(completionDateInMillis = null))
            }
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

    private fun setTaskGroup() {
        val taskGroupNames = taskActivityViewModel.getTaskGroupsNames()
        val taskGroupIds = taskActivityViewModel.getTaskGroupIds()

        if (taskGroupNames.isEmpty() || taskGroupIds.isEmpty()) {
            Toast.makeText(this, "Нет доступных групп", Toast.LENGTH_SHORT).show()
            return
        }

        val currentItemIndex = taskGroupIds.indexOf(task.taskGroupId)
        val currentItem = if (currentItemIndex != -1) taskGroupNames.indexOf(taskGroupNames[currentItemIndex]) else -1

        val dialog = AlertDialog.Builder(this)
            .setTitle("Выбрать группу")
            .setSingleChoiceItems(taskGroupNames, currentItem, null)
            .setPositiveButton("Переместить") { dialog, _ ->
                val checkedIndex = (dialog as AlertDialog).listView.checkedItemPosition
                if (checkedIndex != ListView.INVALID_POSITION) {
                    task = task.copy(taskGroupId = taskGroupIds[checkedIndex])
                    updateTask(task)
                } else {
                    dialog.dismiss()
                }
            }
            .create()

        val negativeButtonTitle = if (task.taskGroupId == null) "Отмена" else "Удалить"
        dialog.setButton(Dialog.BUTTON_NEGATIVE, negativeButtonTitle) { _, _ ->
            if (task.taskGroupId != null) {
                task = task.copy(taskGroupId = null)
                updateTask(task)
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}