package com.em.mediaplayer.app.utils

import android.app.Application
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream
import javax.inject.Inject


class IOUtils @Inject constructor(private val application: Application) {
    fun cacheDirPath(fileName: String): String {
        return "${application.cacheDir.absoluteFile}/$fileName"
    }
}


fun InputStream.toFile(path: String) {
    use { input ->
        File(path).outputStream().use { input.copyTo(it) }
    }
}

fun Context.toFileDescriptor(uri: Uri): FileDescriptor? {
    return contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
}


