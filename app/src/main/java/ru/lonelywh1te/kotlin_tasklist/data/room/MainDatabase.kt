package ru.lonelywh1te.kotlin_tasklist.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.lonelywh1te.kotlin_tasklist.data.room.dao.TaskDao
import ru.lonelywh1te.kotlin_tasklist.data.room.dao.TaskGroupDao
import ru.lonelywh1te.kotlin_tasklist.data.room.entity.TaskEntity
import ru.lonelywh1te.kotlin_tasklist.data.room.entity.TaskGroupEntity

@Database(entities = [TaskEntity::class, TaskGroupEntity::class], version = 1, exportSchema = false)
abstract class MainDatabase: RoomDatabase() {
    abstract fun TaskDao(): TaskDao
    abstract fun TaskGroupDao(): TaskGroupDao

    companion object {
        fun get(context: Context): MainDatabase {
            return Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, "tasks_db")
                        .fallbackToDestructiveMigration() // TODO: удалить
                        .build()
        }
    }
}