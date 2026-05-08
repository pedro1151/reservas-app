package com.optic.pramozventicoappz.domain.model.business.completebusiness


import com.google.gson.annotations.SerializedName
import java.util.Date



data class BusinessCompleteResponse(

    @SerializedName("id") val id: Int,
    // identidad
    @SerializedName("name") val name: String?,
    @SerializedName("logo_url") val logoUrl: String?,
    // contacto
    @SerializedName("email") val email: String?,

    @SerializedName("phone") val phone: String?,

    @SerializedName("whatsapp_number") val whatsappNumber: String?,

    // ubicación
    @SerializedName("city") val city: String?,

    @SerializedName("country") val country: String?,

    // estado
    @SerializedName("is_active") val isActive: Boolean,

    // owner
    @SerializedName("owner_id") val ownerId: Int,

    // auditoría
    @SerializedName("created_by") val createdBy: String?,

    @SerializedName("created") val created: String,

    @SerializedName("updated_by") val updatedBy: String?,

    @SerializedName("updated") val updated: String,

    // plan
    @SerializedName("plan") val plan: PlanCompleteResponse
)

data class PlanCompleteResponse(

    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("explanation")
    val explanation: String,

    @SerializedName("max_collab")
    val maxCollab: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("created")
    val created: String
)