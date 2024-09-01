package ru.lonelywh1te.kotlin_tasklist.presentation.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import ru.lonelywh1te.kotlin_tasklist.domain.models.TaskGroup

class ChooseTaskGroupDialog(
    private val currentTaskGroupId: Int?,
    taskGroups: Array<TaskGroup>
): DialogFragment() {
    private var onTaskGroupChanged: ((taskGroupId: Int?) -> Unit)? = null

    private val taskGroupNames = taskGroups.map { it.name }.toTypedArray()
    private val taskGroupIds = taskGroups.map { it.id }
    private val currentTaskGroupName = taskGroups.find { it.id == currentTaskGroupId }?.name
    private val selectedItem = taskGroupNames.indexOf(currentTaskGroupName)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Выбрать группу")
            .setSingleChoiceItems(taskGroupNames, selectedItem, null)
            .setPositiveButton("Переместить") { dialog, _ ->
                val selectedIndex = (dialog as AlertDialog).listView.checkedItemPosition

                if (selectedIndex != ListView.INVALID_POSITION) {
                    onTaskGroupChanged?.invoke(taskGroupIds[selectedIndex])
                } else {
                    this.dismiss()
                }
            }
            .setNegativeButton(if (currentTaskGroupId == null) "Отмена" else "Удалить") { _, _ ->
                if (currentTaskGroupId != null) {
                    onTaskGroupChanged?.invoke(null)
                }

                this.dismiss()
            }
            .create()

        return dialog
    }

    fun onTaskGroupChanged(callback: (taskGroupId: Int?) -> Unit) {
        onTaskGroupChanged = callback
    }
}