package com.optic.pramozventicoappz.domain.model.sales
import com.google.gson.annotations.SerializedName

data class WeekStats(
    @SerializedName("week")
    val week: Int,

    @SerializedName("monto")
    val monto: Double,

    @SerializedName("desde")
    val desde: String, // puedes parsear luego a LocalDate

    @SerializedName("hasta")
    val hasta: String
)

data class MonthStats(
    @SerializedName("name")
    val name: String,

    @SerializedName("month_index")
    val monthIndex: Int,

    @SerializedName("total")
    val total: Double,

    @SerializedName("weeks")
    val weeks: List<WeekStats>
)

data class LastWeekStats(
    @SerializedName("lunes")
    val lunes: Double,

    @SerializedName("martes")
    val martes: Double,

    @SerializedName("miercoles")
    val miercoles: Double,

    @SerializedName("jueves")
    val jueves: Double,

    @SerializedName("viernes")
    val viernes: Double,

    @SerializedName("sabado")
    val sabado: Double,

    @SerializedName("domingo")
    val domingo: Double
)

data class TopProductStats(
    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("total_vendido")
    val totalVendido: Double
)

data class SalesStatsResponse(
    @SerializedName("year")
    val year: Int,

    @SerializedName("year_total")
    val yearTotal: Double,

    @SerializedName("months")
    val months: List<MonthStats> = emptyList(),

    @SerializedName("today")
    val today: Double? = null,

    @SerializedName("last_week")
    val lastWeek: LastWeekStats? = null,

    @SerializedName("productos_top")
    val productosTop: List<TopProductStats> = emptyList()
)