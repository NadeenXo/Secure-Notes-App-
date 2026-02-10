package com.example.securenotesapp.domain

interface NotesRepository {

    suspend fun getNotes(): List<Note>

    suspend fun getNote(id: Long): Note?

    suspend fun createNote(
        title: String,
        content: String
    )

    suspend fun updateNote(
        id: Long,
        title: String,
        content: String
    )

    suspend fun deleteNote(id: Long)
}
