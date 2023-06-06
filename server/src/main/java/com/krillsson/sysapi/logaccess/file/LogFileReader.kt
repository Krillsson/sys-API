package com.krillsson.sysapi.logaccess.file

import org.apache.commons.io.input.ReversedLinesFileReader
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

// log reader that naively reads all lines. May exhaust resources
class LogFileReader(val file: File) {

    fun count() = countLines(file)

    fun name(): String = file.name

    fun path(): String = file.absolutePath

    fun sizeBytes() = Files.size(Paths.get(file.path))
    fun lines(): List<String> {
        val reader = ReversedLinesFileReader(file, Charset.defaultCharset())
        val lines = mutableListOf<String>()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            line?.let { lines.add(it) }
        }
        return lines
    }

    private fun countLines(file: File): Int {
        var lines = 0
        val fis = FileInputStream(file)
        val buffer = ByteArray(
            8 * 1024
        )
        var read: Int
        while (fis.read(buffer).also { read = it } != -1) {
            for (i in 0 until read) {
                if (buffer[i] == '\n'.code.toByte()) lines++
            }
        }
        fis.close()
        return lines
    }
}