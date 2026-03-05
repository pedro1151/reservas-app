package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.data.dataSource.remote.service.ReservasService
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
import retrofit2.Response

class ReservasRemoteDataSourceImpl (private val reservasService: ReservasService): ReservasRemoteDataSource {



    //clientes
    override suspend fun getClientsByProvider(
        providerId: Int,
        fullName: String,
        email: String
    ): Response<List<ClientResponse>> = reservasService.getClientsByProvider(providerId, fullName, email)

    override suspend fun getClientById(
        clientId: Int
    ): Response<ClientResponse> = reservasService.getClientById(clientId)


    override suspend fun createClient(
        request: ClientCreateRequest
    ): Response<ClientResponse> = reservasService.createClient(request)



    override suspend fun updateClient(
        clientId: Int,
        request: ClientUpdateRequest
    ): Response<ClientResponse> = reservasService.updateClient(clientId = clientId, request=request)

    override suspend fun deleteClient(
        clientId: Int
    ): Response<DefaultResponse> = reservasService.deleteClient(clientId)


    // servicios

    override suspend fun getServicesByProvider(
        providerId: Int,
        name: String
    ): Response<List<ServiceResponse>> = reservasService.getServicesByProvider(providerId, name)

    override suspend fun getStaffTotales(
    ): Response<List<StaffResponse>> = reservasService.getStaffsTotales()

     // services
    override suspend fun createService(
         request: ServiceCreateRequest
    ): Response<ServiceResponse> = reservasService.createService(request)

    override suspend fun updateService(
        serviceId: Int,
        request: ServiceUpdateRequest
    ): Response<ServiceResponse> = reservasService.updateService(serviceId, request)

    override suspend fun getServiceById(
        serviceId: Int
    ): Response<ServiceResponse> = reservasService.getServiceById(serviceId)


    // reservas

    override suspend fun getReservations(
    ): Response<List<ReservationResponse>> = reservasService.getReservations()

    override suspend fun getReservationsByProvider(
        providerId: Int
    ): Response<List<ReservationResponseComplete>> = reservasService.getReservationsByProvider(providerId)

    override suspend fun getReservationById(
        reservationId: Int
    ): Response<ReservationResponse> = reservasService.getReservationById(reservationId)

    override suspend fun createReservation(
        request: ReservationCreateRequest
    ): Response<ReservationResponse> = reservasService.createReservation(request)

    override suspend fun updateReservation(
        reservationId: Int,
        request: ReservationUpdateRequest
    ): Response<ReservationResponse> = reservasService.updateReservation(
        reservationId = reservationId,
        request = request
    )

}