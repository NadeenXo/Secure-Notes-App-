package com.example.securenotesapp.di

import androidx.room.Room
import com.example.securenotesapp.data.NotesRepositoryImpl
import com.example.securenotesapp.data.local.NotesDatabase
import com.example.securenotesapp.data.security.CryptoManager
import com.example.securenotesapp.domain.NotesRepository
import com.example.securenotesapp.ui.presentation.details_screen.NoteDetailsViewModel
import com.example.securenotesapp.ui.presentation.list_screen.NotesListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            NotesDatabase::class.java,
            "secure_notes.db"
        ).build()
    }

    single { get<NotesDatabase>().noteDao() }

    single { CryptoManager() }

    single<NotesRepository> {
        NotesRepositoryImpl(
            dao = get(),
            cryptoManager = get()
        )
    }


    viewModel { NotesListViewModel(repository = get()) }

    viewModel { (noteId: Long) ->
        NoteDetailsViewModel(noteId = noteId, repository = get())
    }
}