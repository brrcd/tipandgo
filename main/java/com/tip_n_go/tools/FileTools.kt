package com.tip_n_go.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object FileTools {

    private val buffer = ByteArray(1024)

    fun createFileFromUri(context: Context, uri: Uri): File {
        val tempFile = createImageFile(context)
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        var length = inputStream?.read(buffer)
        while (length != null && length > 0) {
            outputStream.write(buffer, 0, length)
            length = inputStream?.read(buffer)
        }
        outputStream.close()
        inputStream?.close()
        return tempFile
    }

    fun createImageFile(context: Context): File {
        val cacheDir = context.cacheDir
        return File.createTempFile(TEMP_FILE_NAME, ".png", cacheDir).apply {
            deleteOnExit()
        }
    }

    fun resizeImageFile(context: Context, file: File) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val inputStream = context.contentResolver.openInputStream(file.toUri())
        val exif: ExifInterface? = inputStream?.let { ExifInterface(it) }
        val rotatedImage = when (exif?.getAttributeInt(TAG_ORIENTATION, ORIENTATION_UNDEFINED)) {
            ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
        val bos = ByteArrayOutputStream()
        rotatedImage.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        val byteArray = bos.toByteArray()
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.flush()
        fos.close()
        bos.close()
    }
}
