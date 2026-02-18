package com.optic.pramosreservasappz.domain.model.reservas.reservations


import com.google.gson.annotations.SerializedName

enum class ReservationType {

    @SerializedName("appointment")
    APPOINTMENT,

    @SerializedName("class")
    CLASS,

    @SerializedName("event")
    EVENT,

    @SerializedName("meeting")
    MEETING
}