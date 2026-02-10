package com.example.securenotesapp.data.local

import android.util.Base64
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromByteArray(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    @TypeConverter
    fun toByteArray(value: String): ByteArray {
        return Base64.decode(value, Base64.DEFAULT)
    }
}


//🔐 Secure Notes App (Android – Kotlin)
//
//A simple Android app that allows users to create, view, edit, and delete notes securely.
//All notes are stored locally only and never saved in plaintext.     How to Run the App
//
//Clone the repository
//
//
//Open the project in Android Studio
//
//Let Gradle sync
//
//Run the app
//
//Android Version & Tools Used
//
//Language: Kotlin
//
//UI: Jetpack Compose
//
//Architecture: MVVM
//
//Database: Room
//
//Dependency Injection: Koin
//
//Encryption: AES-256 (GCM) using Android Keystore
//
//Min SDK: 26+
//
//Target SDK: Latest stable
//
//What Influenced My Design Decisions
//
//Security first:
//Note content is encrypted before being saved to disk. Encryption keys are generated and stored securely using Android Keystore, never hardcoded or logged.
//
//No backend by design:
//The app works fully offline to reduce the attack surface and comply with the requirements.
//
//Simple but maintainable architecture:
//MVVM with Repository pattern keeps UI, business logic, and data layers clearly separated.
//
//Modern Android stack
//
//Bonus
//Threat Model (What This App Protects Against)
//
//Plaintext note exposure from:
//
//File system access
//
//Rooted device data inspection
//
//Backup extraction
//
//Accidental logging of sensitive data
//
//Known Limitations
//
//Notes are decrypted in memory when displayed
//
//No biometric / user authentication layer
//
//No cloud backup or sync
//
//Device-level compromise (malware with runtime access) is out of scope