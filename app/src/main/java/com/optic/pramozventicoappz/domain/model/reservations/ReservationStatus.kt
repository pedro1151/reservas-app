package com.optic.pramozventicoappz.domain.model.reservations


import com.google.gson.annotations.SerializedName

enum class ReservationStatus {

    @SerializedName("pending")
    PENDING,

    @SerializedName("confirmed")
    CONFIRMED,

    @SerializedName("cancelled")
    CANCELLED,

    @SerializedName("completed")
    COMPLETED
}
