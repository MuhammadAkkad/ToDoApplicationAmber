package com.task.noteapp.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.task.noteapp.data.model.NoteModel
import com.task.noteapp.databinding.NoteItemLayoutBinding
import com.task.noteapp.ui.util.ListDiffUtil

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */
class NoteListAdapter :
    ListAdapter<NoteModel, NoteListAdapter.ViewHolder>(ListDiffUtil()) {

    var list = ArrayList<NoteModel>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: NoteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteModel) {

            binding.note = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteItemLayoutBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    fun setData(list: List<NoteModel>) {
        this.list = list as ArrayList<NoteModel>
        this.submitList(list)
    }
}