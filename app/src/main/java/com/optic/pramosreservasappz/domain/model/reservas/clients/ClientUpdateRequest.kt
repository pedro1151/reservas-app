package com.optic.pramosreservasappz.domain.model.reservas.clients

import com.google.gson.annotations.SerializedName

data class ClientUpdateRequest(

    @SerializedName("full_name")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("enterprise_name")
    val enterpriseName: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("postal_code")
    val postalCode: String? = null,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("updated_by")
    val updatedBy: String? = null
)
