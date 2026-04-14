package com.optic.pramosreservasappz.domain.repository


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

import com.optic.pramosreservasappz.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

interface ReservasRepository {

    //sales

    suspend fun createSale(
        request: SaleCreateRequest
    ): Resource<SaleResponse>

    suspend fun createSaleWithItems(
        request: CreateSaleWithItemsRequest
    ): Resource<SaleResponse>

    fun getSalesByOwner(
        ownerId: Int,
        limit: Int
    ): Flow<Resource<List<SaleResponse>>>

    suspend fun getSaleById(
        saleId:Int
    ): Resource<SaleWithItemsResponse>

    suspend fun updateSale(
        saleId: Int,
        request: SaleUpdateRequest
    ): Resource<SaleResponse>

    suspend fun deleteSaleSoft(
        saleId:Int
    ): Resource<DefaultResponse>

    suspend fun deleteSaleHard(
        saleId:Int
    ): Resource<DefaultResponse>





    // SALE STATS
    fun getSaleStats(
        ownerId: Int,
        year: Int
    ): Flow<Resource<SalesStatsResponse>>



    // sale items:

    suspend fun createSaleItem(
        request: SaleItemCreateRequest
    ): Resource<SaleItemResponse>

    suspend fun createSaleItemBulk(
        request: List<SaleItemCreateRequest>
    ): Resource<List<SaleItemResponse>>

    suspend fun getItemsBySale(
        saleId: Int,
    ): Flow<Resource<List<SaleItemResponse>>>


    suspend fun getSaleItemById(
        itemId: Int,
    ): Resource<SaleItemResponse>

    suspend fun updateSaleItem(
        itemId: Int,
        request: SaleItemUpdateRequest
    ): Resource<SaleItemResponse>


    suspend fun deleteSaleItemHard(
        itemId: Int
    ): Resource<DefaultResponse>

    suspend fun deleteSaleItemSoft(
        itemId: Int
    ): Resource<DefaultResponse>


    // products

    suspend fun createProduct(
        request: ProductCreateRequest
    ): Resource<ProductResponse>

    suspend fun createProductSafe(
        request: ProductCreateRequest
    ): Resource<ProductResponse>

    suspend fun getProductByUser(
        ownerId: Int,
        name: String
    ): Flow<Resource<List<ProductResponse>>>


    suspend fun getProductById(
        productId: Int,
    ): Resource<ProductResponse>

    suspend fun updateProduct(
        productId: Int,
        request: ProductUpdateRequest
    ): Resource<ProductResponse>

    suspend fun deleteProductSoft(
        productId: Int,
    ): Resource<DefaultResponse>

    suspend fun deleteProductHard(
        productId: Int,
    ): Resource<DefaultResponse>





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
    suspend fun getClientsByOwner(
        ownerId: Int,
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