package com.task.noteapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.task.noteapp.data.db.AppDatabase
import com.task.noteapp.data.db.NoteRepository
import com.task.noteapp.data.model.NoteModel
import com.task.noteapp.ui.note.NoteViewModel
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest : TestCase() {

    private lateinit var viewModel: NoteViewModel

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
                .build()
        val repository = NoteRepository(db.noteDao())
        viewModel = NoteViewModel(repository)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.task.noteapp", appContext.packageName)
    }

    @Test
    fun createNoteUITest() {
        val title = "note title text"
        val description = "note description text"
        val url =
            "https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"

        onView(withId(R.id.add_note_mb)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.title_et)).perform(typeText(title))
        onView(withId(R.id.image_url_et)).perform(typeText(url))
        onView(withId(R.id.description_et)).perform(typeText(description))
        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(description)).check(matches(isDisplayed()))
        onView(withText(url)).check(
            matches(
                isDisplayed()
            )
        )
        onView(withId(R.id.save_note_mb)).perform(click())
    }

    @Test
    fun testInsertNote() {
        val title = "title insertion"
        val description = "description insertion"
        val imageUrl =
            "https://i.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"
        val createdDate = "11/11/1111"
        val isEdited = true
        viewModel.insertNote(NoteModel(0, title, description, createdDate, imageUrl, isEdited))
        viewModel.getAllNotes()
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.noteList.observeOnce {
                assertEquals(
                    it[0].note,
                    title
                )
            }
        }
    }

    private fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }

}