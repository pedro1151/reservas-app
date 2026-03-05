package com.optic.pramosreservasappz.data.dataSource.remote


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

interface ReservasRemoteDataSource {
  /* tambien en este archivo de datasource, se responde con Response de retrofit,
   esta aclarcion es util, ya que en las implementaciones se utiliza FLow
   */

  // reservas

  suspend fun getReservations(
  ): Response<List<ReservationResponse>>

  suspend fun getReservationsByProvider(
    providerId: Int
  ): Response<List<ReservationResponseComplete>>


  suspend fun getReservationById(
    reservationId: Int
  ): Response<ReservationResponse>

  suspend fun createReservation(
    request: ReservationCreateRequest
  ):Response<ReservationResponse>

  suspend fun updateReservation(
    reservationId: Int,
    request: ReservationUpdateRequest
  ): Response<ReservationResponse>




    // CLIENTS
    suspend fun getClientsByProvider(
    providerId: Int,
    fullName:String,
    email:String
    ): Response<List<ClientResponse>>

    suspend fun getClientById(
      clientId: Int
    ): Response<ClientResponse>

    suspend fun createClient(
      request: ClientCreateRequest
    ): Response<ClientResponse>

    suspend fun updateClient(
      clientId: Int,
      request: ClientUpdateRequest
    ): Response<ClientResponse>


  suspend fun deleteClient(
    clientId: Int,
  ): Response<DefaultResponse>


  //services
    suspend fun createService(request: ServiceCreateRequest): Response<ServiceResponse>
    suspend fun updateService(serviceId: Int, request: ServiceUpdateRequest): Response<ServiceResponse>
    suspend fun getServiceById(serviceId: Int): Response<ServiceResponse>
    suspend fun getServicesByProvider(providerId: Int, name:String): Response<List<ServiceResponse>>

    suspend fun getStaffTotales(): Response<List<StaffResponse>>




}