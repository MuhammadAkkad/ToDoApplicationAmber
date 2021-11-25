package com.task.noteapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.noteapp.data.model.NoteModel

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */
@Database(entities = [NoteModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}