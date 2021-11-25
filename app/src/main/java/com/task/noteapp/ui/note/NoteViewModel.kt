package com.task.noteapp.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.data.db.NoteRepository
import com.task.noteapp.data.model.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    private val _noteList = MutableLiveData<List<NoteModel>>()
    val noteList: LiveData<List<NoteModel>> get() = _noteList


    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _noteList.postValue(repository.getAllNotes())
        }
    }

    fun insertNote(noteModel: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(noteModel)
        }
    }

    fun deleteNote(noteModel: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(noteModel)
        }
    }

    fun updateNote(note: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

}