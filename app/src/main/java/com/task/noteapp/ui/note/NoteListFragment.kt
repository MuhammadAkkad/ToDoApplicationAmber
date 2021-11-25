package com.task.noteapp.ui.note

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.task.noteapp.MainActivity
import com.task.noteapp.R
import com.task.noteapp.data.model.NoteModel
import com.task.noteapp.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlin.properties.Delegates

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: NoteListAdapter

    private val viewModel: NoteViewModel by viewModels()

    private var _isFirstTime by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        _isFirstTime = isFirstTime()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setUpNoteListRv()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    // region OnBoarding
    private fun showOnBoarding() {
        if (_isFirstTime) {
            showSnackBar(getString(R.string.swipe_to_delete), Snackbar.LENGTH_LONG)
            setSharedKey()
        }
    }

    private fun setSharedKey() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.is_first_time), false)
            apply()
        }
    }

    private fun isFirstTime(): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref?.getBoolean(getString(R.string.is_first_time), true)!!
    }
    // endregion

    private fun setupObservers() {
        viewModel.noteList.observe(viewLifecycleOwner, {
            adapter.setData(it as ArrayList<NoteModel>)
            if (it.isNotEmpty()) {
                showOnBoarding()
            }
        })
        binding.noteListRv.smoothScrollToPosition(0)
    }

    private fun setUpNoteListRv() {
        val listRv = binding.noteListRv
        adapter = NoteListAdapter()
        val scaleInAnimationAdapter = SlideInBottomAnimationAdapter(adapter)
        scaleInAnimationAdapter.setDuration(300)
        scaleInAnimationAdapter.setInterpolator(OvershootInterpolator())
        scaleInAnimationAdapter.setFirstOnly(false)
        listRv.adapter = scaleInAnimationAdapter
        listRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        listRv.itemAnimator = LandingAnimator().apply {
            addDuration = 100
        }
        swipeToDelete(listRv)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeItem(viewHolder, adapter.list[viewHolder.adapterPosition])
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun removeItem(viewHolder: RecyclerView.ViewHolder, noteModel: NoteModel) {
        adapter.list.removeAt(viewHolder.adapterPosition)
        adapter.notifyItemRemoved(viewHolder.adapterPosition) // previously i was calling getAllNotes to refresh UI but it was causing flicking animation,
        viewModel.deleteNote(noteModel)                      // this logic solved the problem
        if (adapter.list.isEmpty()) viewModel.getAllNotes() // this line important to update UI with empty list layout.
    }

    private fun showSnackBar(message: String, length: Int) {
        activity?.let {
            MainActivity().showSnackBar(binding.root, message, length)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // avoid memory leak. recommended according to google.
        _binding = null
    }

    private fun getData() {
        viewModel.getAllNotes()
    }
}