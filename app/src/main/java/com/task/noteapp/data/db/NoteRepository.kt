package com.task.noteapp.data.db

import com.task.noteapp.data.model.NoteModel
import javax.inject.Inject

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */
class NoteRepository @Inject constructor (private val noteDao: NoteDao) {

    suspend fun getAllNotes() = noteDao.getAllNotes()

    suspend fun insertNote(note: NoteModel) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteModel) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteModel) {
        noteDao.deleteNote(note)
    }

}