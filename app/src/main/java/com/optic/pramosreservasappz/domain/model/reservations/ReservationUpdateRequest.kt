package com.optic.pramosreservasappz.domain.model.reservations



import com.google.gson.annotations.SerializedName

data class ReservationUpdateRequest(

    @SerializedName("start_time")
    val startTime: String? = null,

    @SerializedName("end_time")
    val endTime: String? = null,

    @SerializedName("status")
    val status: ReservationStatus? = null,

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

    @SerializedName("updated_by")
    val updatedBy: String? = null
)