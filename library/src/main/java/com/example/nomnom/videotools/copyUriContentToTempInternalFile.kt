package com.example.nomnom.videotools

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun copyUriContentToTempInternalFile(context: Context, uri: Uri): String? {
    return when (uri.scheme) {
        "content" -> {
            context.contentResolver.query(uri, null, null, null, null)?.use {
                if (it.moveToFirst().not()) return null
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex == -1) return null

                // Todo: sanitize filepath
                val displayName = it.getString(displayNameIndex)
                val file = File(context.cacheDir, displayName)
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                file.absolutePath

            }
        }
        "file" -> {
            uri.path
        }
        else -> {
            null
        }
    }
}