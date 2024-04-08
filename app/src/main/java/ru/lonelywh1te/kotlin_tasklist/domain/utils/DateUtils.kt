package ru.lonelywh1te.kotlin_tasklist.domain.utils

import android.icu.text.SimpleDateFormat
import java.util.Locale

private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"

object DateUtils {
    fun normalDateFormat(time: Long): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return sdf.format(time)
    }
}