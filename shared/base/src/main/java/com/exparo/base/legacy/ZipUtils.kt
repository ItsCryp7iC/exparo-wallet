package com.exparo.base.legacy

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

private const val BUFFER_SIZE = 8192

// 'internal' keyword removed to make it public and accessible from other modules
fun zip(context: Context, zipFileUri: Uri, files: List<File>) {
    context.contentResolver.openOutputStream(zipFileUri, "w")?.use {
        zip(it, files)
    }
}

// 'internal' keyword removed
fun zip(outputStream: OutputStream, files: List<File>) {
    ZipOutputStream(outputStream).use { zos ->
        val buffer = ByteArray(BUFFER_SIZE)
        for (file in files) {
            if (!file.exists() || !file.isFile) continue
            val entry = ZipEntry(file.name)
            zos.putNextEntry(entry)
            FileInputStream(file).use { fis ->
                var len: Int
                while (fis.read(buffer).also { len = it } > 0) {
                    zos.write(buffer, 0, len)
                }
            }
        }
    }
}

// 'internal' keyword removed
fun unzip(context: Context, zipFileUri: Uri, destinationDir: File) {
    if (!destinationDir.exists()) {
        destinationDir.mkdirs()
    }

    context.contentResolver.openInputStream(zipFileUri)?.use {
        unzip(it, destinationDir)
    }
}

// 'internal' keyword removed
fun unzip(inputStream: InputStream, destinationDir: File) {
    ZipInputStream(inputStream).use { zis ->
        var entry: ZipEntry? = zis.nextEntry
        while (entry != null) {
            val file = File(destinationDir, entry.name)
            if (entry.isDirectory) {
                file.mkdirs()
            } else {
                FileOutputStream(file).use { fos ->
                    val buffer = ByteArray(BUFFER_SIZE)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                }
            }
            entry = zis.nextEntry
        }
    }
}