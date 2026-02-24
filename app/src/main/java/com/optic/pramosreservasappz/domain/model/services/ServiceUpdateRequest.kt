package com.optic.pramosreservasappz.domain.model.services


import com.google.gson.annotations.SerializedName

data class ServiceUpdateRequest(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("duration_minutes")
    val durationMinutes: Int? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null,

    @SerializedName("updated_by")
    val updatedBy: String? = null,
    @SerializedName("buffer_time") val bufferTime: Int? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("hidden") val hidden: Boolean? = null
)
