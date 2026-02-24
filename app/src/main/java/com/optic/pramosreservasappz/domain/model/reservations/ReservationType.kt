package com.optic.pramosreservasappz.domain.model.reservations


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