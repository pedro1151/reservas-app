package com.optic.pramosreservasappz.domain.repository


import com.optic.pramosreservasappz.domain.model.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationResponse
import com.optic.pramosreservasappz.domain.model.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import com.optic.pramosreservasappz.domain.model.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.model.staff.StaffResponse
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse

import com.optic.pramosreservasappz.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

interface ReservasRepository {

    // reservas

    suspend fun createReservation(
        request: ReservationCreateRequest
    ): Flow<Resource<ReservationResponse>>

    suspend fun updateReservation(
        reservationId: Int,
        request: ReservationUpdateRequest
    ): Flow<Resource<ReservationResponse>>

    suspend fun getReservationById(
        reservationId: Int,
    ): Flow<Resource<ReservationResponse>>

    suspend fun getReservations(
    ): Flow<Resource<List<ReservationResponse>>>

    suspend fun getReservationsByProvider(
        providerId: Int
    ): Flow<Resource<List<ReservationResponseComplete>>>

    // clients
    suspend fun getClientsByProvider(
        providerId: Int,
        fullName:String,
        email:String
    ): Flow<Resource<List<ClientResponse>>>

    suspend fun createClient(
        request: ClientCreateRequest
    ): Flow<Resource<ClientResponse>>

    suspend fun updateClient(
        clientId: Int,
        request: ClientUpdateRequest
    ): Flow<Resource<ClientResponse>>

    suspend fun deleteClient(
        clientId:Int,
    ): Resource<DefaultResponse>


    suspend fun getClientById(
        clientId: Int,
    ): Flow<Resource<ClientResponse>>


    //services
    suspend fun createService(
        request: ServiceCreateRequest
    ): Flow<Resource<ServiceResponse>>

    suspend fun updateService(
        serviceId: Int,
        request: ServiceUpdateRequest
    ): Flow<Resource<ServiceResponse>>

    suspend fun getServiceById(
        serviceId: Int,
    ): Flow<Resource<ServiceResponse>>

    suspend fun getServicesByProvider(providerId: Int, name:String): Flow<Resource<List<ServiceResponse>>>
    suspend fun getStaffTotales(): Flow<Resource<List<StaffResponse>>>


}