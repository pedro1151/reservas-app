package com.optic.pramosreservasappz.presentation.screens.recibos.pdf

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ceil
import kotlin.math.min

object PDFBbitmapGenerator {

    private const val PAGE_WIDTH = 1080
    private const val PAGE_HEIGHT = 1920
    private const val MARGIN = 24

    @RequiresApi(Build.VERSION_CODES.Q)
    fun generate(
        context: Context,
        bitmap: Bitmap,
        fileName: String
    ): Boolean {

        return try {

            val pdf = PdfDocument()

            /* ---------------------------------------
               Escala proporcional al ancho A4-like
            ----------------------------------------*/
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

                // fondo blanco
                canvas.drawColor(Color.WHITE)

                val sliceHeight =
                    min(
                        usablePageHeight,
                        scaledHeight - currentTop
                    )

                val srcTop =
                    (currentTop / scale).toInt()

                val srcBottom =
                    ((currentTop + sliceHeight) / scale).toInt()
                        .coerceAtMost(bitmap.height)

                val partBitmap =
                    Bitmap.createBitmap(
                        bitmap,
                        0,
                        srcTop,
                        bitmap.width,
                        srcBottom - srcTop
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

            savePdfQ(
                context = context,
                pdf = pdf,
                fileName = fileName
            )

            pdf.close()
            true

        } catch (e: Exception) {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun savePdfQ(
        context: Context,
        pdf: PdfDocument,
        fileName: String
    ) {

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
            ) ?: return

        resolver.openOutputStream(uri)?.use {
            pdf.writeTo(it)
        }

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
    }
}