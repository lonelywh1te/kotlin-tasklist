package ru.lonelywh1te.kotlin_tasklist.presentation.fragment.taskGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lonelywh1te.kotlin_tasklist.R
import ru.lonelywh1te.kotlin_tasklist.databinding.FragmentCreateTaskGroupBottomBinding
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup
import ru.lonelywh1te.kotlin_tasklist.presentation.viewModel.CreateTaskGroupFragmentViewModel

class CreateTaskGroupBottomFragment: BottomSheetDialogFragment() {
    private var onCreateTaskGroup: (() -> Unit)? = null // TODO: уничтожается при пересоздании

    private lateinit var binding: FragmentCreateTaskGroupBottomBinding
    private val viewModel by viewModel<CreateTaskGroupFragmentViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateTaskGroupBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateTaskGroup.setOnClickListener {
            if (binding.inputTaskGroupTitle.text.isBlank()) {
                binding.inputTaskGroupTitle.error = getString(R.string.enterName)
                return@setOnClickListener
            }

            createTaskGroup()
        }
    }

    private fun createTaskGroup() {
        val name = binding.inputTaskGroupTitle.text.toString()
        val description = binding.inputTaskGroupDesc.text.toString()

        // TODO: вынести во viewModel
        val taskGroup = TaskGroup(name, description)

        lifecycleScope.launch {
            viewModel.createTaskGroup(taskGroup).join()
            onCreateTaskGroup?.invoke()
        }

        this.dismiss()
    }

    fun onCreateTaskGroup(callback: () -> Unit) {
        onCreateTaskGroup = callback
    }
}