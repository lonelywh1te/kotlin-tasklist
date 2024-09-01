package ru.lonelywh1te.kotlin_tasklist.presentation.fragment.task

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentTaskBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.Task
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.domain.utils.DateUtils
import ru.lonelywh1te.kotlin_tasklist.presentation.fragment.dialog.ChooseTaskGroupDialog
import ru.lonelywh1te.kotlin_tasklist.presentation.fragment.dialog.DateTimePickerDialog
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.NotificationViewModel
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.TaskFragmentViewModel

class TaskFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentTaskBinding
    private val viewModel by viewModel<TaskFragmentViewModel>()
    private val notificationViewModel by viewModel<NotificationViewModel>()
    private val args: TaskFragmentArgs by navArgs()

    private var task = Task("", "")
    private var taskGroup: TaskGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = task.copy(id = args.taskId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner)

        binding.btnDeleteTask.setOnClickListener {
            deleteTask()
        }

        binding.tvTaskDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                task = task.copy(description = s.toString())
            }
        })

        binding.tvTaskGroupInfo.setOnClickListener {
            val taskGroups = viewModel.taskGroups.value?.toTypedArray() ?: emptyArray()
            val dialog = ChooseTaskGroupDialog(task.taskGroupId, taskGroups)

            dialog.onTaskGroupChanged { taskGroupId ->
                task = task.copy(taskGroupId = taskGroupId)
                updateTask()
            }

            dialog.show(parentFragmentManager, dialog.tag)
        }

        binding.tvTaskCompletionDate.setOnClickListener {
            setTaskCompletionDate()
        }

        viewModel.task.observe(viewLifecycleOwner) {
            task = it
            updateTaskUI()
        }
    }

    override fun onPause() {
        super.onPause()
        updateTask()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTask(task.id)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.task_fragment, menu)

        val isFavouriteItem = menu.findItem(R.id.is_favourite)

        isFavouriteItem.icon = if (task.isFavourite) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite_fill)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.is_favourite -> {
                task = task.copy(isFavourite = !task.isFavourite)
                requireActivity().invalidateOptionsMenu()
                updateTask()
            }
            R.id.rename_task -> {}
            android.R.id.home -> findNavController().popBackStack()
        }

        return true
    }

    private fun updateTask() {
        viewModel.updateTask(task)
        updateTaskUI()
    }

    private fun updateTaskUI() {
        requireActivity().actionBar?.title = task.title

        binding.tvTaskDescription.setText(task.description)

        // task completion date
        if (task.completionDateInMillis != null) {
            binding.tvTaskCompletionDate.text = DateUtils.normalDateFormat(task.completionDateInMillis!!)
            binding.tvTaskCompletionDate.alpha = 1.0F
        } else {
            binding.tvTaskCompletionDate.alpha = 0.3F
            binding.tvTaskCompletionDate.text = getString(R.string.add_date_time)
        }

        // task group
        if (task.taskGroupId != null) lifecycleScope.launch {
            taskGroup = viewModel.getTaskGroupById(task.taskGroupId!!).await()

            withContext(Dispatchers.Main) {
                binding.tvTaskGroupInfo.alpha = 1.0F
                binding.tvTaskGroupInfo.text = taskGroup!!.name
            }
        } else {
            binding.tvTaskGroupInfo.alpha = 0.3F
            binding.tvTaskGroupInfo.text = "Добавить группу"
        }

        requireActivity().invalidateOptionsMenu()
    }

    private fun setTaskCompletionDate() {
        val dialog = DateTimePickerDialog(task.completionDateInMillis)

        dialog.apply {
            onDateSelected { timeInMillis ->
                task = task.copy(completionDateInMillis = timeInMillis)
                binding.tvTaskCompletionDate.text = DateUtils.normalDateFormat(timeInMillis)

                notificationViewModel.setTaskNotification(task, timeInMillis)
            }

            onDateDeleted {
                task = task.copy(completionDateInMillis = null)
                binding.tvTaskCompletionDate.text = "Назначить"

                notificationViewModel.cancelTaskNotification(task)
            }
        }

        dialog.show(parentFragmentManager, dialog.tag)
    }

    private fun deleteTask() {
        viewModel.deleteTask(task)
        findNavController().popBackStack()
    }
}