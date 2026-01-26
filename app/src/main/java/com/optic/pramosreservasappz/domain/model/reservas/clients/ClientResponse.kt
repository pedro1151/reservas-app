package com.optic.pramosreservasappz.domain.model.reservas.clients


import com.google.gson.annotations.SerializedName

data class ClientResponse(

    @SerializedName("id") val id: Int,
    @SerializedName("provider_id") val providerId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("enterprise_name") val enterpriseName: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("postal_code") val postalCode: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("created_by") val createdBy: String? = null,
    @SerializedName("created") val created: String,  // Puedes mapearlo a LocalDateTime luego si quieres
    @SerializedName("updated_by") val updatedBy: String? = null,
    @SerializedName("updated") val updated: String? = null
)
