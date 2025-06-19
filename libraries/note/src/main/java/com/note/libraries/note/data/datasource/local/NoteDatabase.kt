package com.note.libraries.note.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.note.libraries.note.data.datasource.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
internal abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
