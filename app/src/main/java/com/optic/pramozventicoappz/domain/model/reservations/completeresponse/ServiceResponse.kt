package com.optic.pramozventicoappz.domain.model.reservations.completeresponse

import com.google.gson.annotations.SerializedName



data class ServiceResponse(

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("price") val price: Double?,
    @SerializedName("color") val color: String?,
    @SerializedName("buffer_time") val bufferTime: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("is_active") val isActive: Boolean?,
    @SerializedName("hidden") val hidden: Boolean?,
    @SerializedName("created") val created: String

)
