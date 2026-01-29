package com.optic.pramosreservasappz.domain.model.reservas.services

import com.google.gson.annotations.SerializedName

data class ServiceCreateRequest(
    @SerializedName("provider_id")
    val providerId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("duration_minutes")
    val durationMinutes: Int,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = true,

    @SerializedName("created_by")
    val createdBy: String? = null,

    @SerializedName("buffer_time") val bufferTime: Double? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("hidden") val hidden: Boolean? = null
)

