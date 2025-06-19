package com.note.libraries.note.data.datasource.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.note.libraries.note.data.datasource.local.entity.NoteEntity

@Dao
internal interface NoteDao {
    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query(
        """
        SELECT * FROM note 
        WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN :isAsc = 1 THEN timestamp END ASC,
            CASE WHEN :isAsc = 0 THEN timestamp END DESC
        """
    )
    fun getNoteWithFilter(query: String, isAsc: Boolean): PagingSource<Int, NoteEntity>
}
