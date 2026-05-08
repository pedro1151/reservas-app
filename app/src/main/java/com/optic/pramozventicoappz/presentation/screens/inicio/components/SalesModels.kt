package com.optic.pramozventicoappz.presentation.sales.Components

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalTime

val SBlack   = Color(0xFF0D0D0D)
val SGray100 = Color(0xFFF2F2F2)
val SGray200 = Color(0xFFE8E8E8)
val SGray400 = Color(0xFFAAAAAA)
val SGray600 = Color(0xFF666666)
val SAccent  = Color(0xFF1DB954)
val SRed     = Color(0xFFEF5350)
val SAmber   = Color(0xFFD97706)

val Violet  = Color(0xFF7C3AED)

data class SaleItem(
    val id:         Int,
    val clientName: String,
    val items:      List<String>,
    val total:      Double,
    val time:       LocalTime,
    val date:       LocalDate,
    val status:     SaleStatus
)

enum class SaleStatus { COMPLETED, PENDING, CANCELLED }

data class ProductItem(
    val id:    Int,
    val name:  String,
    val price: Double,
    val stock: Int,
    val color: Color
)

data class CartItem(
    val product:  ProductItem,
    val quantity: Int = 1
)

data class ReportEntry(
    val label: String,
    val value: Double,
    val isToday: Boolean = false
)

val fakeSales = mutableListOf(
    SaleItem(1, "Camila Yahuita",    listOf("Tintura cabello"), 25.0,  LocalTime.of(9,30),  LocalDate.now(), SaleStatus.COMPLETED),
    SaleItem(2, "Edgar Ramírez",     listOf("Masaje corporal"), 70.0,  LocalTime.of(10,0),  LocalDate.now(), SaleStatus.COMPLETED),
    SaleItem(3, "Enzo Pérez",        listOf("Corte premium", "Tinte"), 115.0, LocalTime.of(11,15), LocalDate.now(), SaleStatus.PENDING),
    SaleItem(4, "Bianca Flores",     listOf("Manicure"), 35.0,         LocalTime.of(14,0),  LocalDate.now(), SaleStatus.COMPLETED),
    SaleItem(5, "Marcelo Tinelli",   listOf("Servicio 2"), 100.0,      LocalTime.of(15,30), LocalDate.now().minusDays(1), SaleStatus.COMPLETED),
    SaleItem(6, "Jonathan Emmanuel", listOf("Masaje corporal"), 70.0,  LocalTime.of(9,0),   LocalDate.now().minusDays(1), SaleStatus.CANCELLED),
    SaleItem(7, "Tito Calderon",     listOf("Nuevo 3"), 123.0,         LocalTime.of(16,0),  LocalDate.now().minusDays(2), SaleStatus.COMPLETED),
    SaleItem(8, "Ana López",         listOf("Tintura cabello", "Manicure"), 60.0, LocalTime.of(11,0), LocalDate.now().minusDays(3), SaleStatus.COMPLETED),
    SaleItem(9, "Carlos Mendoza",    listOf("Corte premium"), 15.0,    LocalTime.of(12,30), LocalDate.now().minusDays(4), SaleStatus.COMPLETED),
    SaleItem(10,"Sofía García",      listOf("Masaje corporal"), 70.0,  LocalTime.of(10,30), LocalDate.now().minusDays(5), SaleStatus.COMPLETED),
)

val fakeProducts = mutableListOf(
    ProductItem(1, "Tintura cabello para mujeres", 25.0,  8,  Color(0xFF5C6BC0)),
    ProductItem(2, "Masaje corporal",               70.0,  5,  Color(0xFF26A69A)),
    ProductItem(3, "Servicio 2",                   100.0, 12, Color(0xFF7E57C2)),
    ProductItem(4, "Corte premium",                 15.0,  3,  Color(0xFFEF5350)),
    ProductItem(5, "Nuevo 3",                      123.0, 6,  Color(0xFFFF7043)),
    ProductItem(6, "Manicure",                      35.0, 10, Color(0xFFEC407A)),
    ProductItem(7, "Tinte",                         45.0,  2,  Color(0xFF42A5F5)),
)

val fakeClients = listOf(
    "Camila Yahuita", "Edgar Ramírez", "Enzo Pérez", "Bianca Flores",
    "Marcelo Tinelli", "Jonathan Emmanuel", "Tito Calderon",
    "Ana López", "Carlos Mendoza", "Sofía García", "Valentina Ruiz",
    "Diego Morales", "Luciana Peña", "Mateo Fernández"
)

val fakeWeekReport = listOf(
    ReportEntry("L",  85.0),
    ReportEntry("Ma", 130.0),
    ReportEntry("Mi", 45.0),
    ReportEntry("J",  220.0),
    ReportEntry("V",  175.0),
    ReportEntry("S",  310.0),
    ReportEntry("D",  fakeSales.filter { it.date == LocalDate.now() && it.status == SaleStatus.COMPLETED }.sumOf { it.total }, isToday = true),
)

val fakeMonthReport = (1..LocalDate.now().dayOfMonth).map { day ->
    ReportEntry(
        label = day.toString(),
        value = when {
            day % 7 == 0 -> 0.0
            else -> (50..350).random().toDouble()
        },
        isToday = day == LocalDate.now().dayOfMonth
    )
}