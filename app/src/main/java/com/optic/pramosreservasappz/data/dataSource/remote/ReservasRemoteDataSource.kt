package com.optic.pramosreservasappz.data.dataSource.remote


import com.optic.pramosreservasappz.domain.model.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationResponse
import com.optic.pramosreservasappz.domain.model.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import com.optic.pramosreservasappz.domain.model.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.model.staff.StaffResponse
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemResponse
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemUpdateRequest
import com.optic.pramosreservasappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramosreservasappz.domain.model.sales.SalesStatsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReservasRemoteDataSource {
  /* tambien en este archivo de datasource, se responde con Response de retrofit,
   esta aclarcion es util, ya que en las implementaciones se utiliza FLow
   */

     // ventas

  suspend fun createSale(
    request: SaleCreateRequest
  ): Response<SaleResponse>

  suspend fun createSaleWithItems(
    request: CreateSaleWithItemsRequest
  ): Response<SaleResponse>

  suspend fun getSalesByBusiness(
    businessId: Int,
    limit: Int
  ): Response<List<SaleResponse>>

  suspend fun getSaleById(
    saleId:Int
  ): Response<SaleWithItemsResponse>

  suspend fun updateSale(
    saleId: Int,
    request: SaleUpdateRequest
  ): Response<SaleResponse>

  suspend fun deleteSaleSoft(
    saleId:Int
  ): Response<DefaultResponse>

  suspend fun deleteSaleHard(
    saleId:Int
  ): Response<DefaultResponse>


  // SALE STATS
  suspend fun getSaleStats(
    businessId: Int,
    year:Int
  ): Response<SalesStatsResponse>


  // sale items:

  suspend fun createSaleItem(
    request: SaleItemCreateRequest
  ): Response<SaleItemResponse>

  suspend fun createSaleItemBulk(
    request: List<SaleItemCreateRequest>
  ): Response<List<SaleItemResponse>>

  suspend fun getItemsBySale(
    saleId: Int,
  ): Response<List<SaleItemResponse>>


  suspend fun getSaleItemById(
    itemId: Int,
  ): Response<SaleItemResponse>

  suspend fun updateSaleItem(
    itemId: Int,
    request: SaleItemUpdateRequest
  ): Response<SaleItemResponse>


  suspend fun deleteSaleItemHard(
    itemId: Int
  ): Response<DefaultResponse>

  suspend fun deleteSaleItemSoft(
    itemId: Int
  ): Response<DefaultResponse>


  // products

  suspend fun createProduct(
    request: ProductCreateRequest
  ): Response<ProductResponse>

  suspend fun createProductSafe(
    request: ProductCreateRequest
  ): Response<ProductResponse>

  suspend fun getProductByBusiness(
    businessId: Int,
    name: String
  ): Response<List<ProductResponse>>


  suspend fun getProductById(
    productId: Int,
  ): Response<ProductResponse>

  suspend fun updateProduct(
    productId: Int,
    request: ProductUpdateRequest
  ): Response<ProductResponse>

  suspend fun deleteProductSoft(
    productId: Int,
  ): Response<DefaultResponse>

  suspend fun deleteProductHard(
    productId: Int,
  ): Response<DefaultResponse>











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
    suspend fun getClientsByBusiness(
    businessId: Int,
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