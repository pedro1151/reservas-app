package com.optic.pramosreservasappz.presentation.screens.recibos.pdf

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ceil
import kotlin.math.min

object PDFBbitmapGenerator {

    private const val TAG = "PDFBbitmapGenerator"

    private const val PAGE_WIDTH = 1080
    private const val PAGE_HEIGHT = 1920
    private const val MARGIN = 24

    fun generate(
        context: Context,
        bitmap: Bitmap,
        fileName: String
    ): Boolean {

        if (bitmap.isRecycled || bitmap.width <= 0 || bitmap.height <= 0) {
            Log.e(TAG, "Bitmap inválido para generar PDF")
            return false
        }

        val pdf = PdfDocument()

        return try {
            drawBitmapToPdf(bitmap, pdf)

            val finalFileName = normalizePdfName(fileName)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                savePdfQ(context, pdf, finalFileName)
            } else {
                savePdfLegacy(context, pdf, finalFileName)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error generando PDF", e)
            false
        } finally {
            try {
                pdf.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error cerrando PDF", e)
            }
        }
    }

    fun generateForShare(
        context: Context,
        bitmap: Bitmap,
        fileName: String
    ): Uri? {

        if (bitmap.isRecycled || bitmap.width <= 0 || bitmap.height <= 0) {
            Log.e(TAG, "Bitmap inválido para compartir PDF")
            return null
        }

        val pdf = PdfDocument()

        return try {
            drawBitmapToPdf(bitmap, pdf)

            val finalFileName = normalizePdfName(fileName)

            val shareDir = File(context.cacheDir, "shared_receipts")

            if (!shareDir.exists()) {
                shareDir.mkdirs()
            }

            val file = File(shareDir, finalFileName)

            FileOutputStream(file).use { outputStream ->
                pdf.writeTo(outputStream)
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error generando PDF para compartir", e)
            null
        } finally {
            try {
                pdf.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error cerrando PDF para compartir", e)
            }
        }
    }

    private fun drawBitmapToPdf(
        bitmap: Bitmap,
        pdf: PdfDocument
    ) {

        val scale =
            (PAGE_WIDTH - (MARGIN * 2)).toFloat() /
                    bitmap.width.toFloat()

        val scaledHeight =
            (bitmap.height * scale).toInt()

        val usablePageHeight =
            PAGE_HEIGHT - (MARGIN * 2)

        val totalPages =
            ceil(
                scaledHeight.toDouble() /
                        usablePageHeight.toDouble()
            ).toInt()

        if (totalPages <= 0) {
            throw IllegalStateException("No hay páginas para generar")
        }

        var pageNumber = 1
        var currentTop = 0

        while (currentTop < scaledHeight) {

            val pageInfo =
                PdfDocument.PageInfo.Builder(
                    PAGE_WIDTH,
                    PAGE_HEIGHT,
                    pageNumber
                ).create()

            val page = pdf.startPage(pageInfo)
            val canvas = page.canvas

            canvas.drawColor(Color.WHITE)

            val sliceHeight =
                min(
                    usablePageHeight,
                    scaledHeight - currentTop
                )

            val srcTop =
                (currentTop / scale).toInt()

            val srcBottom =
                ((currentTop + sliceHeight) / scale)
                    .toInt()
                    .coerceAtMost(bitmap.height)

            val sourceHeight =
                srcBottom - srcTop

            if (sourceHeight <= 0) {
                pdf.finishPage(page)
                break
            }

            val partBitmap =
                Bitmap.createBitmap(
                    bitmap,
                    0,
                    srcTop,
                    bitmap.width,
                    sourceHeight
                )

            val scaledPart =
                Bitmap.createScaledBitmap(
                    partBitmap,
                    PAGE_WIDTH - (MARGIN * 2),
                    sliceHeight,
                    true
                )

            canvas.drawBitmap(
                scaledPart,
                MARGIN.toFloat(),
                MARGIN.toFloat(),
                null
            )

            pdf.finishPage(page)

            partBitmap.recycle()
            scaledPart.recycle()

            currentTop += sliceHeight
            pageNumber++
        }
    }

    private fun normalizePdfName(
        fileName: String
    ): String {
        return if (fileName.endsWith(".pdf", ignoreCase = true)) {
            fileName
        } else {
            "$fileName.pdf"
        }
    }

    private fun savePdfQ(
        context: Context,
        pdf: PdfDocument,
        fileName: String
    ): Boolean {

        return try {
            val resolver = context.contentResolver

            val values = ContentValues().apply {
                put(
                    MediaStore.Downloads.DISPLAY_NAME,
                    fileName
                )
                put(
                    MediaStore.Downloads.MIME_TYPE,
                    "application/pdf"
                )
                put(
                    MediaStore.Downloads.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
                put(
                    MediaStore.Downloads.IS_PENDING,
                    1
                )
            }

            val uri =
                resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
                ) ?: return false

            resolver.openOutputStream(uri)?.use { outputStream ->
                pdf.writeTo(outputStream)
            } ?: return false

            values.clear()
            values.put(
                MediaStore.Downloads.IS_PENDING,
                0
            )

            resolver.update(
                uri,
                values,
                null,
                null
            )

            true

        } catch (e: Exception) {
            Log.e(TAG, "Error guardando PDF en Downloads", e)
            false
        }
    }

    private fun savePdfLegacy(
        context: Context,
        pdf: PdfDocument,
        fileName: String
    ): Boolean {

        return try {
            val directory =
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    ?: context.filesDir

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val file = File(directory, fileName)

            FileOutputStream(file).use { outputStream ->
                pdf.writeTo(outputStream)
            }

            true

        } catch (e: Exception) {
            Log.e(TAG, "Error guardando PDF legacy", e)
            false
        }
    }
}