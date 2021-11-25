package com.task.noteapp.ui.util

import androidx.recyclerview.widget.DiffUtil
import com.task.noteapp.data.model.NoteModel

/**
 * Created by Muhammad AKKAD on 11/2/21.
 */
class ListDiffUtil : DiffUtil.ItemCallback<NoteModel>() {

    override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
        return oldItem == newItem
    }

}