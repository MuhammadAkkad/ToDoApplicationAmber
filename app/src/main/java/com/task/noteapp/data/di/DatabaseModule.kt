package com.task.noteapp.data.di

import android.content.Context
import androidx.room.Room
import com.task.noteapp.data.Constant
import com.task.noteapp.data.db.AppDatabase
import com.task.noteapp.data.db.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Created by Muhammad AKKAD on 11/12/21.
 */
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            Constant.NOTE_DATABASE
        ).build()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

}