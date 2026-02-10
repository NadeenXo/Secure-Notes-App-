package com.example.securenotesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    suspend fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): NoteEntity?

    @Insert
    suspend fun insert(entity: NoteEntity): Long

    @Query("""
        UPDATE notes 
        SET title = :title, encryptedContent = :encryptedContent, iv = :iv 
        WHERE id = :id
    """)
    suspend fun update(id: Long, title: String, encryptedContent: ByteArray, iv: ByteArray)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Long)
}
