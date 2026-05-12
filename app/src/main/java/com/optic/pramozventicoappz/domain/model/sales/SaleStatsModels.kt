package com.optic.pramozventicoappz.domain.model.sales

import com.google.gson.annotations.SerializedName

data class WeekStats(
    @SerializedName("week")
    val week: Int,

    @SerializedName("monto")
    val monto: Double,

    @SerializedName("desde")
    val desde: String,

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
    val weeks: List<WeekStats> = emptyList(),

    @SerializedName("growth_percentage")
    val growthPercentage: Double? = null
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
    val totalVendido: Double,

    @SerializedName("cantidad_total")
    val cantidadTotal: Int
)

data class BestMonthStats(
    @SerializedName("name")
    val name: String,

    @SerializedName("month_index")
    val monthIndex: Int,

    @SerializedName("total")
    val total: Double
)

data class CurrentMonthStats(
    @SerializedName("name")
    val name: String,

    @SerializedName("month_index")
    val monthIndex: Int,

    @SerializedName("total")
    val total: Double,

    @SerializedName("cantidad_ventas")
    val cantidadVentas: Int,

    @SerializedName("growth_percentage")
    val growthPercentage: Double? = null
)

data class PaymentMethodStats(
    @SerializedName("payment_method")
    val paymentMethod: String,

    @SerializedName("cantidad_ventas")
    val cantidadVentas: Int,

    @SerializedName("total_vendido")
    val totalVendido: Double
)

data class SalesmanRankStats(
    @SerializedName("rank")
    val rank: Int,

    @SerializedName("total_vendido")
    val totalVendido: Double,

    @SerializedName("cantidad_ventas")
    val cantidadVentas: Int,

    @SerializedName("cantidad_items")
    val cantidadItems: Int,

    @SerializedName("ticket_promedio")
    val ticketPromedio: Double,

    @SerializedName("salesman")
    val salesman: SalesmanResponse? = null
)

data class SalesStatsResponse(
    @SerializedName("year")
    val year: Int,

    @SerializedName("year_total")
    val yearTotal: Double,

    @SerializedName("cantidad_ventas_year")
    val cantidadVentasYear: Int = 0,

    @SerializedName("cantidad_ventas_historico")
    val cantidadVentasHistorico: Int = 0,

    @SerializedName("cantidad_productos_total")
    val cantidadProductosTotal: Int = 0,

    @SerializedName("cantidad_clientes_total")
    val cantidadClientesTotal: Int = 0,

    @SerializedName("ticket_promedio_year")
    val ticketPromedioYear: Double = 0.0,

    @SerializedName("promedio_diario_year")
    val promedioDiarioYear: Double = 0.0,

    @SerializedName("cantidad_items_year")
    val cantidadItemsYear: Int = 0,

    @SerializedName("current_month")
    val currentMonth: CurrentMonthStats? = null,

    @SerializedName("best_product")
    val bestProduct: TopProductStats? = null,

    @SerializedName("main_payment_method")
    val mainPaymentMethod: PaymentMethodStats? = null,

    @SerializedName("yesterday")
    val yesterday: Double? = null,

    @SerializedName("today_vs_yesterday_percentage")
    val todayVsYesterdayPercentage: Double? = null,

    @SerializedName("best_month")
    val bestMonth: BestMonthStats? = null,

    @SerializedName("payment_methods")
    val paymentMethods: List<PaymentMethodStats> = emptyList(),

    @SerializedName("months")
    val months: List<MonthStats> = emptyList(),

    @SerializedName("today")
    val today: Double? = null,

    @SerializedName("last_week")
    val lastWeek: LastWeekStats? = null,

    @SerializedName("productos_top")
    val productosTop: List<TopProductStats> = emptyList(),

    @SerializedName("salesman_rank")
    val salesmanRank: List<SalesmanRankStats> = emptyList()
)