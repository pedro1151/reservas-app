package com.optic.pramosreservasappz.domain.model.reservations

import com.google.gson.annotations.SerializedName

data class ReservationCreateRequest(

    @SerializedName("type")
    val type: ReservationType,

    @SerializedName("provider_id")
    val providerId: Int,

    @SerializedName("start_time")
    val startTime: String,   // ISO String

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("status")
    val status: ReservationStatus = ReservationStatus.PENDING,

    @SerializedName("staff_id")
    val staffId: Int? = null,

    @SerializedName("client_id")
    val clientId: Int? = null,

    @SerializedName("service_id")
    val serviceId: Int? = null,

    @SerializedName("classmate_id")
    val classmateId: Int? = null,

    @SerializedName("event_id")
    val eventId: Int? = null,

    @SerializedName("meeting_id")
    val meetingId: Int? = null,

    @SerializedName("internal_note")
    val internalNote: String? = null,

    @SerializedName("created_by")
    val createdBy: String? = null
)
