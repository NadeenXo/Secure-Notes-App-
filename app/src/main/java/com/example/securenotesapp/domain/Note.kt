package com.example.securenotesapp.domain

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long
)