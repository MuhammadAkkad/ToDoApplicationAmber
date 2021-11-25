package com.task.noteapp.ui.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.task.noteapp.R
import com.task.noteapp.data.model.NoteModel
import com.task.noteapp.ui.note.NoteListFragmentDirections

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */
class Binding {

    companion object {

        @BindingAdapter("android:isEdited")
        @JvmStatic
        fun getEdited(view: TextView, isEdited: Boolean) {
            when (isEdited) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.GONE
            }
        }

        @BindingAdapter("android:emptyList")
        @JvmStatic
        fun emptyList(view: View, list: LiveData<List<NoteModel>>?) {
            when (list?.value?.isEmpty()) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.GONE
            }
        }

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: MaterialButton, navigate: Boolean) {
            view.setOnClickListener {
                view.findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
            }
        }

        @BindingAdapter("android:navigateToUpdateFragment")
        @JvmStatic
        fun navigateToUpdateFragment(view: ConstraintLayout, note: NoteModel) {
            view.setOnClickListener {
                view.findNavController()
                    .navigate(
                        NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(
                            note
                        )
                    )
            }
        }

        @BindingAdapter("android:source")
        @JvmStatic
        fun setImageViewResource(imageView: ImageView, resource: String) {
            try {
                if (resource.isNotEmpty()) {
                    imageView.visibility = View.VISIBLE
                    Glide.with(imageView.rootView.context).load(resource)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate()
                        .skipMemoryCache(true).into(imageView)
                } else {
                    imageView.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}