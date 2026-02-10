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
