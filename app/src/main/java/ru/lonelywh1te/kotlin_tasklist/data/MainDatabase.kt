package ru.lonelywh1te.kotlin_tasklist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.lonelywh1te.kotlin_tasklist.data.dao.TaskDao
import ru.lonelywh1te.kotlin_tasklist.data.dao.TaskGroupDao
import ru.lonelywh1te.kotlin_tasklist.data.entity.Task
import ru.lonelywh1te.kotlin_tasklist.data.entity.TaskGroup

@Database(entities = [Task::class, TaskGroup::class], version = 1)
abstract class MainDatabase: RoomDatabase() {
    abstract fun TaskDao(): TaskDao
    abstract fun TaskGroupDao(): TaskGroupDao

    companion object {
        private var db: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            if (db == null) {
                synchronized(MainDatabase::class) {
                    db = Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, "tasks_db").build()
                }
            }

            return db!!
        }
    }
}