package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.data.dataSource.remote.service.ReservasService
import com.optic.pramosreservasappz.domain.model.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
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

class ReservasRemoteDataSourceImpl (private val reservasService: ReservasService): ReservasRemoteDataSource {



    //clientes
    override suspend fun getClientsByBusiness(
        businessId: Int,
        fullName:String,
        email:String
    ): Response<List<ClientResponse>> = reservasService.getClientsByBusiness(
        businessId = businessId,
        fullName = fullName,
        email = email
    )

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

    //ventas

    override suspend fun createSale(
        request: SaleCreateRequest
    ): Response<SaleResponse> = reservasService.createSale(request)

    override suspend fun createSaleWithItems(
        request: CreateSaleWithItemsRequest
    ): Response<SaleResponse> = reservasService.createSaleWithItems(request)

    override suspend fun getSalesByBusiness(
        businessId: Int,
        limit: Int
    ): Response<List<SaleResponse>> = reservasService.getSalesByBusiness(
        businessId= businessId,
        limit = limit
    )

    override suspend fun getSaleById(
        saleId: Int
    ): Response<SaleWithItemsResponse> = reservasService.getSaleById(saleId)

    override suspend fun updateSale(
        saleId: Int,
        request: SaleUpdateRequest
    ): Response<SaleResponse>  = reservasService.updateSale(request = request, saleId = saleId)

    override suspend fun deleteSaleSoft(
        saleId: Int
    ): Response<DefaultResponse> = reservasService.deleteSaleSoft(saleId)

    override suspend fun deleteSaleHard(
        saleId: Int
    ): Response<DefaultResponse> = reservasService.deleteSaleHard(saleId)


    // SALE STATS

    override suspend fun getSaleStats(
        businessId: Int,
        year: Int
    ): Response<SalesStatsResponse> = reservasService.getSalesStats(
        businessId = businessId,
        year = year
    )


    // SALE ITEMS
    override suspend fun createSaleItem(
        request: SaleItemCreateRequest
    ): Response<SaleItemResponse> = reservasService.createSaleItem(request)

    override suspend fun createSaleItemBulk(
        request: List<SaleItemCreateRequest>
    ): Response<List<SaleItemResponse>> = reservasService.createSaleItemBulk(request)

    override suspend fun getItemsBySale(
        saleId: Int
    ): Response<List<SaleItemResponse>> = reservasService.getItemsBySale(saleId)

    override suspend fun getSaleItemById(
        itemId: Int
    ): Response<SaleItemResponse> = reservasService.getSaleItemById(itemId)

    override suspend fun updateSaleItem(
        itemId: Int,
        request: SaleItemUpdateRequest
    ): Response<SaleItemResponse> = reservasService.updateSaleItem(itemId = itemId, request = request)

    override suspend fun deleteSaleItemHard(
        itemId: Int
    ): Response<DefaultResponse> = reservasService.deleteSaleItemHard(itemId)

    override suspend fun deleteSaleItemSoft(
        itemId: Int
    ): Response<DefaultResponse> = reservasService.deleteSaleItemSoft(itemId)

    // PRODUCTS

    override suspend fun createProduct(
        request: ProductCreateRequest
    ): Response<MiniProductResponse> = reservasService.createProduct(request)

    override suspend fun createProductSafe(
        request: ProductCreateRequest
    ): Response<ProductResponse> = reservasService.createProductSafe(request)

    override suspend fun getProductByBusiness(
        businessId: Int,
        name: String
    ): Response<List<MiniProductResponse>> = reservasService.getProductsByBusiness(
        businessId= businessId,
        name = name
    )

    override suspend fun getProductById(
        productId: Int
    ): Response<ProductResponse> = reservasService.getProductById(productId)

    override suspend fun updateProduct(
        productId: Int,
        request: ProductUpdateRequest
    ): Response<ProductResponse> = reservasService.updateProduct(productId = productId, request=request)

    override suspend fun deleteProductSoft(productId: Int): Response<DefaultResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductHard(
        productId: Int
    ): Response<DefaultResponse> = reservasService.deleteProductHard(productId)


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