package ru.lonelywh1te.kotlin_tasklist.data.entity

import android.icu.text.SimpleDateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lonelywh1te.kotlin_tasklist.data.TaskItem
import java.io.Serializable
import java.util.Locale

@Entity(tableName = "task_table")
data class Task (
    var title: String,
    var description: String,
    var isFavourite: Boolean = false,
    var isCompleted: Boolean = false,
    var completionDateInMillis: Long? = null,
    var taskGroupId: Int? = null,

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : TaskItem(), Serializable
{
    companion object {
        private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"

        fun normalDateFormat(time: Long): String {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            return sdf.format(time)
        }
    }
}
