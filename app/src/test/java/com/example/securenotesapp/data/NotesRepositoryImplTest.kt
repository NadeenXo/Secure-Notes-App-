package com.example.securenotesapp.data


import com.example.securenotesapp.data.local.NoteDao
import com.example.securenotesapp.data.local.NoteEntity
import com.example.securenotesapp.data.security.CryptoManager
import com.example.securenotesapp.data.security.EncryptedPayload
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesRepositoryImplTest {

    private val dao: NoteDao = mockk()
    private val crypto: CryptoManager = mockk()

    private lateinit var repo: NotesRepositoryImpl

    @Before
    fun setup() {
        repo = NotesRepositoryImpl(dao = dao, cryptoManager = crypto)
    }

    @Test
    fun `create encrypts content then inserts entity`() = runTest {
        // Arrange
        val payload = EncryptedPayload(cipherText = byteArrayOf(1, 2, 3), iv = byteArrayOf(9, 9))
        every { crypto.encrypt("hello") } returns payload
        coEvery { dao.insert(any()) } returns 10L

        // Act
        repo.createNote(title = "t", content = "hello")

        // Assert
        val entitySlot = slot<NoteEntity>()
        coVerify { dao.insert(capture(entitySlot)) }
        assertEquals("t", entitySlot.captured.title)
        assertArrayEquals(payload.cipherText, entitySlot.captured.encryptedContent)
        assertArrayEquals(payload.iv, entitySlot.captured.iv)

        verify(exactly = 1) { crypto.encrypt("hello") }
    }

    @Test
    fun `getById decrypts entity content`() = runTest {
        // Arrange
        val entity = NoteEntity(
            id = 7L,
            title = "t",
            encryptedContent = byteArrayOf(4, 5),
            iv = byteArrayOf(1, 1, 1),
            createdAt = 123L
        )
        coEvery { dao.getById(7L) } returns entity
        every { crypto.decrypt(entity.encryptedContent, entity.iv) } returns "PLAINTEXT"

        // Act
        val note = repo.getNote(7L)

        // Assert
        assertNotNull(note)
        assertEquals(7L, note!!.id)
        assertEquals("t", note.title)
        assertEquals("PLAINTEXT", note.content)

        verify { crypto.decrypt(entity.encryptedContent, entity.iv) }
    }

    @Test
    fun `delete calls dao delete`() = runTest {
        coEvery { dao.delete(5L) } just Runs

        repo.deleteNote(5L)

        coVerify { dao.delete(5L) }
    }
}