package com.task.noteapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.noteapp.data.Constant
import java.io.Serializable

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */
@Entity(tableName = Constant.NOTE_TABLE)
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var note: String,
    var description: String,
    var createdDate: String,
    var imgUrl: String,
    var isEdited: Boolean
) : Serializable