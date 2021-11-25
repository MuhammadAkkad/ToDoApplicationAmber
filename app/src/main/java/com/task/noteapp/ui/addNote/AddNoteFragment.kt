package com.task.noteapp.ui.addNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.task.noteapp.MainActivity
import com.task.noteapp.R
import com.task.noteapp.data.Constant
import com.task.noteapp.data.model.NoteModel
import com.task.noteapp.databinding.FragmentAddNoteBinding
import com.task.noteapp.ui.note.NoteViewModel
import com.task.noteapp.ui.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null

    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by viewModels()

    private val args by navArgs<AddNoteFragmentArgs>()

    private val emptyArgs = "java.lang.reflect.InvocationTargetException"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)

        setupUpdateLayout()
        setupFabClick()
        setupTextWatchers()

        return binding.root
    }

    private fun setupUpdateLayout() {
        try {
            binding.args = args
            (activity as MainActivity).supportActionBar?.title = getString(R.string.update_note)

            binding.noteImageIv.visibility = View.VISIBLE

            Glide.with(this).load(args.note.imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                .into(binding.noteImageIv)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addNote() {
        if (!validateInputs()) {
            insertOrUpdateNote()
        } else {
            showError()
        }
    }

    private fun insertOrUpdateNote() {
        activity?.let { MainActivity().hideKeyboard(it) }
        try {
            // if args null insert else update
            val id = args.note.id
            val noteTitle = binding.titleEt.text.toString()
            val noteImageUrl = binding.imageUrlEt.text.toString()
            val noteDescription = binding.descriptionEt.text.toString()
            val isEdited =
                noteTitle != args.note.note || noteImageUrl != args.note.imgUrl || noteDescription != args.note.description
            val note =
                NoteModel(
                    id,
                    noteTitle,
                    noteDescription,
                    args.note.createdDate,
                    noteImageUrl,
                    isEdited
                )
            if (isEdited) // if user click update without changing anything do nothing.
                viewModel.updateNote(note)
        } catch (e: java.lang.Exception) {
            if (e.toString() == emptyArgs) {
                val noteTitle = binding.titleEt.text.toString()
                val noteImageUrl = binding.imageUrlEt.text.toString()
                val noteDescription = binding.descriptionEt.text.toString()
                val note =
                    NoteModel(0, noteTitle, noteDescription, getCreatedDate(), noteImageUrl, false)
                viewModel.insertNote(note)
            }
        }
        navigateBack()
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun getCreatedDate(): String {
        return SimpleDateFormat(
            Constant.DATE_FORMAT,
            Locale.US
        ).format(Calendar.getInstance().timeInMillis)
    }

    private fun showError() {
        if (binding.descriptionEt.text.isNullOrBlank()) {
            binding.descriptionEt.error = getString(R.string.required_field)
            binding.descriptionEt.requestFocus()
        }

        if (!binding.imageUrlEt.text.isNullOrEmpty() && !Utils.isValidUrl(binding.imageUrlEt.text.toString())) {
            binding.imageUrlEt.error = getString(R.string.check_entered_url)
            binding.imageUrlEt.requestFocus()
        }

        if (binding.titleEt.text.isNullOrBlank()) {
            binding.titleEt.error = getString(R.string.required_field)
            binding.titleEt.requestFocus()
        }
    }

    private fun setupFabClick() {
        binding.saveNoteMb.setOnClickListener {
            addNote()
        }
    }

    private fun validateInputs(): Boolean {
        return binding.titleEt.text.isNullOrBlank() || binding.descriptionEt.text.isNullOrBlank() || (!binding.imageUrlEt.text
            .isNullOrEmpty() && !Utils.isValidUrl(binding.imageUrlEt.text.toString()))
    }

    private fun setupTextWatchers() {
        binding.titleEt.doAfterTextChanged { binding.titleEt.error = null }
        binding.descriptionEt.doAfterTextChanged { binding.descriptionEt.error = null }
        binding.imageUrlEt.doAfterTextChanged { binding.imageUrlEt.error = null }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // avoid memory leak. recommended according to google.
        _binding = null
    }
}