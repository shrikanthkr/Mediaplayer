package com.em.mediaplayer.app.utils

import android.app.Application
import java.io.File
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

