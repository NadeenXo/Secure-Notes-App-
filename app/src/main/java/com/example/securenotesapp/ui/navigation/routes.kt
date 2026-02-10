package com.example.securenotesapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object NotesList

@Serializable
data class NoteDetails(val noteId: Long)
