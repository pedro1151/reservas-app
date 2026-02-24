package com.optic.pramosreservasappz.domain.model.services


import com.google.gson.annotations.SerializedName
import java.util.*

data class ServiceResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("provider_id") val providerId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("created_by") val createdBy: String? = null,
    @SerializedName("created") val created: Date,
    @SerializedName("updated_by") val updatedBy: String? = null,
    @SerializedName("updated") val updated: Date,

    @SerializedName("buffer_time") val bufferTime: Double? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("hidden") val hidden: Boolean? = null


)
