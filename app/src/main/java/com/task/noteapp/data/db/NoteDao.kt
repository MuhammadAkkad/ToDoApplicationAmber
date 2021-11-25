package com.task.noteapp.data.db

import androidx.room.*
import com.task.noteapp.data.Constant
import com.task.noteapp.data.model.NoteModel

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */
@Dao
interface NoteDao {

    @Query("SELECT * FROM ${Constant.NOTE_TABLE} ORDER BY id DESC")
    suspend fun getAllNotes(): List<NoteModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: NoteModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: NoteModel)

    @Delete
    suspend fun deleteNote(note: NoteModel)

}