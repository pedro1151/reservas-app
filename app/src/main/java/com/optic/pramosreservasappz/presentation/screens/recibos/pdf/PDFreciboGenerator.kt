package com.optic.pramosreservasappz.presentation.screens.recibos.pdf

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import java.io.File
import java.io.FileOutputStream
import androidx.core.view.drawToBitmap

object PDFreciboGenerator {

    fun generate(
        context: Context,
        sale: SaleWithItemsResponse,
        business: BusinessCompleteResponse?
    ): Boolean {

        return try {

            val pdf = PdfDocument()

            val pageInfo =
                PdfDocument.PageInfo.Builder(
                    595,
                    842,
                    1
                ).create()

            val page = pdf.startPage(pageInfo)

            val canvas = page.canvas
            val paint = Paint()

            var y = 40

            // Fondo blanco
            canvas.drawColor(Color.WHITE)

            // BUSINESS
            paint.textSize = 22f
            paint.isFakeBoldText = true
            canvas.drawText(
                business?.name ?: "Mi Negocio",
                40f,
                y.toFloat(),
                paint
            )

            y += 30

            paint.textSize = 11f
            paint.isFakeBoldText = false

            canvas.drawText(
                "${business?.city ?: ""} ${business?.country ?: ""}",
                40f,
                y.toFloat(),
                paint
            )

            y += 20

            canvas.drawText(
                "Tel: ${business?.phone ?: "-"}",
                40f,
                y.toFloat(),
                paint
            )

            y += 35

            // Línea
            paint.strokeWidth = 1f
            canvas.drawLine(40f, y.toFloat(), 555f, y.toFloat(), paint)

            y += 30

            paint.textSize = 18f
            paint.isFakeBoldText = true

            canvas.drawText(
                "RECIBO #${sale.sale.id}",
                40f,
                y.toFloat(),
                paint
            )

            y += 25

            paint.textSize = 11f
            paint.isFakeBoldText = false

            canvas.drawText(
                sale.sale.created ?: "",
                40f,
                y.toFloat(),
                paint
            )

            y += 35

            // ITEMS
            paint.textSize = 12f
            paint.isFakeBoldText = true

            canvas.drawText("Producto", 40f, y.toFloat(), paint)
            canvas.drawText("Cant", 320f, y.toFloat(), paint)
            canvas.drawText("Total", 450f, y.toFloat(), paint)

            y += 20

            paint.isFakeBoldText = false

            sale.items.forEach {

                val total = it.quantity * it.price

                canvas.drawText(
                    it.product?.name ?: "Producto",
                    40f,
                    y.toFloat(),
                    paint
                )

                canvas.drawText(
                    it.quantity.toString(),
                    330f,
                    y.toFloat(),
                    paint
                )

                canvas.drawText(
                    "%.2f".format(total),
                    450f,
                    y.toFloat(),
                    paint
                )

                y += 22
            }

            y += 20

            canvas.drawLine(40f, y.toFloat(), 555f, y.toFloat(), paint)

            y += 30

            paint.textSize = 20f
            paint.isFakeBoldText = true

            canvas.drawText(
                "TOTAL: Bs ${"%.2f".format(sale.sale.amount)}",
                40f,
                y.toFloat(),
                paint
            )

            y += 50

            paint.textSize = 11f
            paint.isFakeBoldText = false

            canvas.drawText(
                "Gracias por su compra",
                40f,
                y.toFloat(),
                paint
            )

            pdf.finishPage(page)

            val fileName = "recibo_${sale.sale.id}.pdf"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val resolver = context.contentResolver

                val values = ContentValues().apply {
                    put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        fileName
                    )
                    put(
                        MediaStore.MediaColumns.MIME_TYPE,
                        "application/pdf"
                    )
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS
                    )
                }

                val uri = resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
                ) ?: return false

                resolver.openOutputStream(uri)?.use {
                    pdf.writeTo(it)
                }

            } else {

                val file = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    ),
                    fileName
                )

                FileOutputStream(file).use {
                    pdf.writeTo(it)
                }
            }

            pdf.close()

            true

        } catch (e: Exception) {
            false
        }
    }
}