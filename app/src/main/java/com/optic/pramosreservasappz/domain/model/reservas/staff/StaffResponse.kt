package com.optic.pramosreservasappz.domain.model.reservas.staff

import com.google.gson.annotations.SerializedName
import java.util.Date

data class StaffResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("provider_id") val providerId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("created_by") val createdBy: String? = null,
    @SerializedName("created") val created: Date,
    @SerializedName("updated_by") val updatedBy: String? = null,
    @SerializedName("updated") val updated: Date
)
