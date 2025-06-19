package com.note.libraries.note.data

import android.content.Context
import androidx.room.Room
import com.note.libraries.note.data.datasource.local.NoteDao
import com.note.libraries.note.data.datasource.local.NoteDatabase
import com.note.libraries.note.data.repository.NoteRepositoryImpl
import com.note.libraries.note.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NoteModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(context, NoteDatabase::class.java, "note_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase) = database.noteDao()

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository = NoteRepositoryImpl(noteDao)
}
