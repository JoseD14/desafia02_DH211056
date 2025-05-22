// FileUtils.kt
package com.example.desafio02.utils  // Cambia según tu paquete

import java.io.File

object FileUtils {

    fun getMimeType(file: File): String {
        val extension = file.extension.lowercase()
        return when (extension) {
            "jpg", "jpeg", "png" -> "image/*"
            "mp3", "wav", "m4a" -> "audio/*"
            "mp4", "avi", "mkv" -> "video/*"
            "txt" -> "text/plain"
            "pdf" -> "application/pdf"
            else -> "*/*"
        }
    }
}
