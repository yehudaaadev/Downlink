package com.example.yt2mp3.data.storage

import android.content.Context
import android.net.Uri
import com.example.yt2mp3.data.util.AppLog
import com.example.yt2mp3.domain.model.VideoMetadata
import com.mpatric.mp3agic.ID3v2
import com.mpatric.mp3agic.ID3v24Tag
import com.mpatric.mp3agic.Mp3File
import java.io.File
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Writes ID3 metadata after conversion (PRD §17). Never fails the download. */
object Id3Writer {
    suspend fun writeMp3Tags(mp3: File, metadata: VideoMetadata) = withContext(Dispatchers.IO) {
        try {
            val tag: ID3v2 = ID3v24Tag()
            tag.title = metadata.title
            metadata.artist?.let { tag.artist = it }
            tag.url = metadata.sourceUrl
            metadata.thumbnailUrl?.let { thumb ->
                runCatching {
                    val conn = URL(thumb).openConnection().apply {
                        connectTimeout = 10_000
                        readTimeout = 10_000
                    }
                    val bytes = conn.getInputStream().use { it.readBytes() }
                    if (bytes.size in 1..3_000_000) {
                        tag.setAlbumImage(bytes, "image/jpeg")
                    }
                }.onFailure { AppLog.w("METADATA", "cover art skipped", it) }
            }
            val tmp = File(mp3.parentFile, mp3.nameWithoutExtension + "_tagged.mp3")
            val wrapper = Mp3File(mp3.absolutePath)
            wrapper.id3v2Tag = tag
            wrapper.save(tmp.absolutePath)
            if (tmp.exists() && tmp.length() > 0) {
                mp3.delete()
                tmp.renameTo(mp3)
            } else {
                tmp.delete()
            }
        } catch (t: Throwable) {
            // PRD §17: missing metadata must not fail the download.
            AppLog.w("METADATA", "ID3 write skipped", t)
        }
    }

    /**
     * Rewrites the ID3v2 title and artist tags of an already-saved SAF-stored MP3.
     *
     * Workflow: copy URI → temp file → mp3agic rewrite → temp2 → write back via ContentResolver.
     * Returns true on success. Never throws — all errors are logged and return false.
     */
    suspend fun rewriteTags(
        context: Context,
        outputUri: String,
        title: String,
        artist: String?,
    ): Boolean = withContext(Dispatchers.IO) {
        val cacheDir = context.cacheDir
        val tmpIn = File(cacheDir, "tag_in_${System.currentTimeMillis()}.mp3")
        val tmpOut = File(cacheDir, "tag_out_${System.currentTimeMillis()}.mp3")
        try {
            // 1. Copy SAF document to a local temp file so mp3agic can read it.
            val uri = Uri.parse(outputUri)
            context.contentResolver.openInputStream(uri)?.use { input ->
                tmpIn.outputStream().use { input.copyTo(it) }
            } ?: return@withContext false

            // 2. Read existing tags and build a new ID3v2.4 tag with updated fields.
            val wrapper = Mp3File(tmpIn.absolutePath)
            val tag: ID3v2 = if (wrapper.hasId3v2Tag()) wrapper.id3v2Tag else ID3v24Tag()
            tag.title = title
            tag.artist = artist ?: ""

            wrapper.id3v2Tag = tag
            wrapper.save(tmpOut.absolutePath)

            if (!tmpOut.exists() || tmpOut.length() == 0L) return@withContext false

            // 3. Write the retagged file back into the SAF document.
            context.contentResolver.openOutputStream(uri, "wt")?.use { output ->
                tmpOut.inputStream().use { it.copyTo(output) }
            } ?: return@withContext false

            true
        } catch (t: Throwable) {
            AppLog.w("METADATA", "rewriteTags failed", t)
            false
        } finally {
            tmpIn.delete()
            tmpOut.delete()
        }
    }
}
