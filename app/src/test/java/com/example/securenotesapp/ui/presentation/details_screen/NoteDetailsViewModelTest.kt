package com.example.securenotesapp.ui.presentation.details_screen

import com.example.securenotesapp.MainDispatcherRule
import com.example.securenotesapp.domain.NotesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: NotesRepository = mockk()

    @Test
    fun `save creates new note when isNewNote`() = runTest {
        // IMPORTANT: match the real signature (3 params) + return something
        coEvery { repository.createNote("Title", "Content") } returns Unit

        val viewModel = NoteDetailsViewModel(
            noteId = -1L,
            repository = repository
        )

        viewModel.onTitleChange("Title")
        viewModel.onContentChange("Content")

        var callbackCalled = false

        viewModel.save { callbackCalled = true }

        // Let launched coroutines run
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.createNote("Title", "Content")
        }

        assertTrue(callbackCalled)
    }
}
