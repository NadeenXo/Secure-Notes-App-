package com.example.securenotesapp

import android.content.Context
import androidx.room.Room
import com.example.securenotesapp.data.NotesRepositoryImpl
import com.example.securenotesapp.data.local.NotesDatabase
import com.example.securenotesapp.data.security.CryptoManager
import com.example.securenotesapp.domain.NotesRepository

object AppGraph {

    @Volatile private var db: NotesDatabase? = null
    @Volatile private var repo: NotesRepository? = null

    fun provideRepository(context: Context): NotesRepository {
        return repo ?: synchronized(this) {
            val database = db ?: Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "secure_notes.db"
            ).build().also { db = it }

            NotesRepositoryImpl(
                dao = database.noteDao(),
                cryptoManager = CryptoManager(),
            ).also { repo = it }
        }
    }
}