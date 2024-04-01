package ru.lonelywh1te.kotlin_tasklist.presentation.view.taskGroupView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.databinding.ActivityCreateEditTaskGroupBinding
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskGroupViewModel

class CreateEditTaskGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEditTaskGroupBinding
    private lateinit var taskGroupViewModel: TaskGroupViewModel
    private lateinit var taskGroup: TaskGroup
    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditTaskGroupBinding.inflate(layoutInflater)
        taskGroupViewModel = ViewModelProvider(this)[TaskGroupViewModel::class.java]

        editMode = intent.extras?.getBoolean("editMode") ?: false
        if (editMode) taskGroup = intent.extras?.getSerializable("taskGroup") as TaskGroup

        setActivityMode()
        setContentView(binding.root)

        binding.btnAddTaskGroup.setOnClickListener {
            val name = binding.inputTaskGroupName.text.toString()
            val description = binding.inputTaskGroupDescription.text.toString()

            if (name.isBlank()) {
                binding.inputTaskGroupName.error = getString(R.string.enterName)
            }
            else {
                createTaskGroup(name, description)
            }
        }

        binding.btnRestoreTaskGroupChanges.setOnClickListener {
            finish()
        }

        binding.btnSaveTaskGroupChanges.setOnClickListener {
            val newTaskGroupName = binding.inputTaskGroupName.text.toString()
            val newTaskGroupDescription = binding.inputTaskGroupDescription.text.toString()

            if (newTaskGroupName.isBlank()) {
                binding.inputTaskGroupName.error = getString(R.string.enterName)
            }
            else {
                updateTaskGroup(newTaskGroupName, newTaskGroupDescription)
                finish()
            }
        }
    }

    private fun createTaskGroup(name: String, description: String) {
        taskGroupViewModel.addTaskGroup(TaskGroup(name, description))
        finish()
    }

    private fun updateTaskGroup(name: String, description: String) {
        taskGroup = TaskGroup(name, description, id = taskGroup.id)
        taskGroupViewModel.updateTaskGroup(taskGroup)
    }

    private fun setActivityMode() {
        binding.tvCreateActivityTitle.text = if (editMode) getString(R.string.editGroup) else getString(R.string.newGroup)
        binding.btnAddTaskGroup.visibility = if (editMode) View.GONE else View.VISIBLE
        binding.btnRestoreTaskGroupChanges.visibility = if (!editMode) View.GONE else View.VISIBLE
        binding.btnSaveTaskGroupChanges.visibility = if (!editMode) View.GONE else View.VISIBLE

        if (editMode) {
            binding.inputTaskGroupName.setText(taskGroup.name)
            binding.inputTaskGroupDescription.setText(taskGroup.description)
        }
    }
}