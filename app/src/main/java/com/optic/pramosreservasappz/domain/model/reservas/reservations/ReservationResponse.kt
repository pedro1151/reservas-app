package com.optic.pramosreservasappz.domain.model.reservas.reservations

import com.google.gson.annotations.SerializedName

data class ReservationResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: ReservationType,

    @SerializedName("provider_id")
    val providerId: Int,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("status")
    val status: ReservationStatus,

    @SerializedName("staff_id")
    val staffId: Int?,

    @SerializedName("client_id")
    val clientId: Int?,

    @SerializedName("service_id")
    val serviceId: Int?,

    @SerializedName("classmate_id")
    val classmateId: Int?,

    @SerializedName("event_id")
    val eventId: Int?,

    @SerializedName("meeting_id")
    val meetingId: Int?,

    @SerializedName("internal_note")
    val internalNote: String?,

    @SerializedName("created_by")
    val createdBy: String?,

    @SerializedName("created")
    val created: String,

    @SerializedName("updated_by")
    val updatedBy: String?,

    @SerializedName("updated")
    val updated: String
)
