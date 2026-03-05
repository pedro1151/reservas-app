package com.optic.pramosreservasappz.domain.model.reservations.completeresponse


import com.google.gson.annotations.SerializedName



data class ClientResponse(

    @SerializedName("id") val id: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("enterprise_name") val enterpriseName: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("postal_code") val postalCode: String?,
    @SerializedName("created") val created: String

)

