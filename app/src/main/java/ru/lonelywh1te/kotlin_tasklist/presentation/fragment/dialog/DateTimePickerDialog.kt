package ru.lonelywh1te.kotlin_tasklist.presentation.fragment.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DateTimePickerDialog(private val completionDate: Long? = null): DialogFragment() {
    private var onDateSelected: ((timeInMillis: Long) -> Unit)? = null
    private var onDateDeleted: (() -> Unit)? = null

    private val calendar = Calendar.getInstance()

    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private val month = calendar.get(Calendar.MONTH)
    private val year = calendar.get(Calendar.YEAR)
    private val hour = calendar.get(Calendar.HOUR_OF_DAY)
    private val minute = calendar.get(Calendar.MINUTE)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = DatePickerDialog(requireContext(), { _, y, m, d ->
            calendar.set(y, m, d)
            showTimePicker()
        }, year, month, day)

        if (completionDate != null) {
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Удалить") {_, _ ->
                onDateDeleted?.invoke()
            }
        }

        return dialog
    }

    private fun showTimePicker() {
        TimePickerDialog(requireContext(), { _, h, m ->
            calendar.set(Calendar.HOUR_OF_DAY, h)
            calendar.set(Calendar.MINUTE, m)

            onDateSelected?.invoke(calendar.timeInMillis)
        }, hour, minute, true).show()
    }

    fun onDateSelected(callback: (timeInMillis: Long) -> Unit) {
        onDateSelected = callback
    }

    fun onDateDeleted(callback: () -> Unit) {
        onDateDeleted = callback
    }
}