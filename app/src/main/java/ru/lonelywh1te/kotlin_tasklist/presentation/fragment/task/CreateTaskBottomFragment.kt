package ru.lonelywh1te.kotlin_tasklist.presentation.fragment.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentCreateTaskBottomBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.fragment.dialog.DateTimePickerDialog
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.CreateTaskFragmentViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel

private const val TASK_GROUP_ID_ARG = "task_group_id"

class CreateTaskBottomFragment: BottomSheetDialogFragment() {
    private var onCreateTask: (() -> Unit)? = null // TODO: уничтожается при пересоздании

    private lateinit var binding: FragmentCreateTaskBottomBinding
    private val viewModel by viewModel<CreateTaskFragmentViewModel>()
    private val notificationViewModel by viewModel<NotificationViewModel>()

    private var completionDate: Long? = null
    private var taskGroupId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskGroupId = arguments?.getInt(TASK_GROUP_ID_ARG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateTaskBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSetTaskCompletionDate.setOnClickListener {
            setTaskCompletionDate()
        }

        binding.btnCreateTask.setOnClickListener {
            if (binding.inputTaskTitle.text.isBlank()) {
                binding.inputTaskTitle.error = getString(R.string.enterTitle)
                return@setOnClickListener
            }

            createTask()
        }
    }

    private fun createTask() {
        val title = binding.inputTaskTitle.text.toString()
        val description = binding.inputTaskDescription.text.toString()
        val isFavourite = binding.cbIsFavourive.isChecked
        val completionDateInMillis = completionDate

        // TODO: вынести во viewModel
        val task = Task(
            title = title,
            description = description,
            isFavourite = isFavourite,
            completionDateInMillis = completionDateInMillis,
            taskGroupId = taskGroupId
        )

        task.completionDateInMillis?.let { date ->
            notificationViewModel.setTaskNotification(task, date)
        }

        lifecycleScope.launch {
            viewModel.createTask(task).join()
            onCreateTask?.invoke()
        }

        this.dismiss()
    }

    private fun setTaskCompletionDate() {
        val dialog = DateTimePickerDialog(completionDate)

        dialog.apply {
            onDateSelected { timeInMillis ->
                completionDate = timeInMillis
                binding.btnSetTaskCompletionDate.text = DateUtils.normalDateFormat(timeInMillis)
            }

            onDateDeleted {
                completionDate = null
                binding.btnSetTaskCompletionDate.text = "Назначить"
            }
        }

        dialog.show(parentFragmentManager, dialog.tag)
    }

    fun onCreateTask(callback: () -> Unit) {
        onCreateTask = callback
    }

    companion object {
        fun newInstance(): CreateTaskBottomFragment = CreateTaskBottomFragment()

        fun newInstance(taskGroupId: Int): CreateTaskBottomFragment {
            val args = Bundle().apply {
                putInt(TASK_GROUP_ID_ARG, taskGroupId)
            }

            return CreateTaskBottomFragment().apply { arguments = args }
        }
    }
}