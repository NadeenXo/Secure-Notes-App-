package com.example.securenotesapp.data

import com.example.securenotesapp.data.local.NoteDao
import com.example.securenotesapp.data.local.NoteEntity
import com.example.securenotesapp.data.security.CryptoManager
import com.example.securenotesapp.domain.Note
import com.example.securenotesapp.domain.NotesRepository

class NotesRepositoryImpl(
    private val dao: NoteDao,
    private val cryptoManager: CryptoManager
) : NotesRepository {

    override suspend fun getNotes(): List<Note> =
        dao.getAll().map { it.toDomain() }

    override suspend fun getNote(id: Long): Note? =
        dao.getById(id)?.toDomain()

    override suspend fun createNote(title: String, content: String) {
        val encrypted = cryptoManager.encrypt(content)
        dao.insert(
            NoteEntity(
                title = title,
                encryptedContent = encrypted.cipherText,
                iv = encrypted.iv,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateNote(id: Long, title: String, content: String) {
        val encrypted = cryptoManager.encrypt(content)
        dao.update(
            id = id,
            title = title,
            encryptedContent = encrypted.cipherText,
            iv = encrypted.iv
        )
    }

    override suspend fun deleteNote(id: Long) {
        dao.delete(id)
    }

    private fun NoteEntity.toDomain(): Note =
        Note(
            id = id,
            title = title,
            content = cryptoManager.decrypt(encryptedContent, iv),
            createdAt = createdAt
        )
}
