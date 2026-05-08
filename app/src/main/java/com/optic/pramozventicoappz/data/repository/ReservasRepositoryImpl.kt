package com.optic.pramozventicoappz.data.repository



import android.util.Log
import com.optic.pramozventicoappz.data.dataSource.local.dao.FixtureDao
import com.optic.pramozventicoappz.data.dataSource.local.dao.FixturePredictionDao
import com.optic.pramozventicoappz.data.dataSource.local.dao.LeagueDao
import com.optic.pramozventicoappz.data.dataSource.local.dao.PlayerDao
import com.optic.pramozventicoappz.data.dataSource.local.dao.TeamDao
import com.optic.pramozventicoappz.data.dataSource.local.mapper.toDomain
import com.optic.pramozventicoappz.data.dataSource.local.mapper.toEntity
import com.optic.pramozventicoappz.data.dataSource.remote.ReservasRemoteDataSource
import com.optic.pramozventicoappz.domain.model.clients.ClientCreateRequest
import com.optic.pramozventicoappz.domain.model.clients.ClientResponse
import com.optic.pramozventicoappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramozventicoappz.domain.model.product.MiniProductResponse
import com.optic.pramozventicoappz.domain.model.product.ProductCreateRequest
import com.optic.pramozventicoappz.domain.model.product.ProductResponse
import com.optic.pramozventicoappz.domain.model.product.ProductUpdateRequest
import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.model.reservations.ReservationResponse
import com.optic.pramozventicoappz.domain.model.reservations.ReservationUpdateRequest
import com.optic.pramozventicoappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import com.optic.pramozventicoappz.domain.model.services.ServiceCreateRequest
import com.optic.pramozventicoappz.domain.model.services.ServiceResponse
import com.optic.pramozventicoappz.domain.model.services.ServiceUpdateRequest
import com.optic.pramozventicoappz.domain.model.staff.StaffResponse
import com.optic.pramozventicoappz.domain.model.response.DefaultResponse
import com.optic.pramozventicoappz.domain.model.saleitem.SaleItemResponse
import com.optic.pramozventicoappz.domain.model.saleitem.SaleItemUpdateRequest
import com.optic.pramozventicoappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleCreateRequest
import com.optic.pramozventicoappz.domain.model.saleitem.SaleItemCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleResponse
import com.optic.pramozventicoappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramozventicoappz.domain.model.sales.SalesStatsResponse

import com.optic.pramozventicoappz.domain.repository.ReservasRepository
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.domain.util.ResponseToRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ReservasRepositoryImpl(
    private val reservasRemoteDataSource: ReservasRemoteDataSource,
    private val fixtureDao: FixtureDao,
    private val leagueDao: LeagueDao,
    private val playerDao: PlayerDao,
    private val teamDao: TeamDao,
    private val fixturePredictionDao: FixturePredictionDao
): ReservasRepository
{
    override suspend fun createSale(
        request: SaleCreateRequest
    ): Resource<SaleResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.createSale(request)
        )


    override suspend fun createSaleWithItems(
        request: CreateSaleWithItemsRequest
    ): Resource<SaleResponse>  =
        ResponseToRequest.send(
            reservasRemoteDataSource.createSaleWithItems(request)
        )



    override fun getSalesByBusiness(
        businessId: Int,
        limit:Int
    ): Flow<Resource<List<SaleResponse>>> = flow {
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getSalesByBusiness(
                    businessId =  businessId,
                    limit = limit

                )
            )
        )
    }

    override suspend fun getSaleById(
        saleId: Int
    ): Resource<SaleWithItemsResponse> =
            ResponseToRequest.send(
                reservasRemoteDataSource.getSaleById(saleId)
            )


    override suspend fun updateSale(
        saleId: Int,
        request: SaleUpdateRequest
    ): Resource<SaleResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.updateSale(saleId=saleId, request=request)
        )


    override suspend fun deleteSaleSoft(
        saleId: Int
    ): Resource<DefaultResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.deleteSaleSoft(saleId)
        )


    override suspend fun deleteSaleHard(
        saleId: Int
    ): Resource<DefaultResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.deleteSaleHard(saleId)
        )




    // SALE STATS

    override fun getSaleStats(
        businessId: Int,
        year: Int
    ): Flow<Resource<SalesStatsResponse>> = flow {
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getSaleStats(
                    businessId = businessId,
                    year    = year
                )
            )
        )
    }


    // sale items
    override suspend fun createSaleItem(
        request: SaleItemCreateRequest
    ): Resource<SaleItemResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.createSaleItem(request)
    )

    override suspend fun createSaleItemBulk(
        request: List<SaleItemCreateRequest>
    ): Resource<List<SaleItemResponse>> =
        ResponseToRequest.send(
            reservasRemoteDataSource.createSaleItemBulk(request)
        )

    override suspend fun getItemsBySale(
        saleId: Int
    ): Flow<Resource<List<SaleItemResponse>>> = flow {
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getItemsBySale(saleId)
            )
        )
    }

    override suspend fun getSaleItemById(
        itemId: Int
    ): Resource<SaleItemResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.getSaleItemById(itemId)
     )

    override suspend fun updateSaleItem(
        itemId: Int,
        request: SaleItemUpdateRequest
    ): Resource<SaleItemResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.updateSaleItem(
                itemId =  itemId,
                request = request
            )
        )

    override suspend fun deleteSaleItemHard(
        itemId: Int
    ): Resource<DefaultResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.deleteSaleItemHard(itemId)
        )

    override suspend fun deleteSaleItemSoft(
        itemId: Int
    ): Resource<DefaultResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.deleteSaleItemSoft(itemId)
        )

    override suspend fun createProduct(
        request: ProductCreateRequest
    ): Resource<MiniProductResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.createProduct(request)
        )

    override suspend fun createProductSafe(
        request: ProductCreateRequest
    ): Resource<ProductResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.createProductSafe(request)
        )

    override suspend fun getProductByBusiness(
        businessId: Int,
        name: String
    ): Flow<Resource<List<MiniProductResponse>>> = flow {
    emit(
        ResponseToRequest.send(
            reservasRemoteDataSource.getProductByBusiness(
                businessId = businessId,
                name = name
            )
        )
    )
}

    override suspend fun getProductById(
        productId: Int
    ): Resource<ProductResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.getProductById(productId)
        )

    override suspend fun updateProduct(
        productId: Int,
        request: ProductUpdateRequest
    ): Resource<ProductResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.updateProduct(productId=productId, request=request)
        )

    override suspend fun deleteProductSoft(
        productId: Int
    ): Resource<DefaultResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.deleteProductSoft(productId)
        )

    override suspend fun deleteProductHard(
        productId: Int
    ): Resource<DefaultResponse> =
        ResponseToRequest.send(
            reservasRemoteDataSource.deleteProductHard(productId)
        )




    // reservas
    override suspend fun createReservation(
        request: ReservationCreateRequest
    ): Flow<Resource<ReservationResponse>> = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.createReservation(request)
            )
        )
    }

    override suspend fun updateReservation(
        reservationId: Int,
        request: ReservationUpdateRequest
    ): Flow<Resource<ReservationResponse>> = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.updateReservation(
                    reservationId=reservationId,
                    request=request
                )
            )
        )
    }

    override suspend fun getReservationById(
        reservationId: Int
    ): Flow<Resource<ReservationResponse>> = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getReservationById(reservationId)
            )
        )
    }

    override suspend fun getReservations(

    ): Flow<Resource<List<ReservationResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getReservations()
            )
        )
    }

    override suspend fun getReservationsByProvider(
        providerId: Int
    ): Flow<Resource<List<ReservationResponseComplete>>> = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getReservationsByProvider(providerId)
            )
        )
    }

    //clients
    override suspend fun getClientsByBusiness(
        businessId: Int,
        fullName: String,
        email:String
    ): Flow<Resource<List<ClientResponse>>> =flow{
    emit(
        ResponseToRequest.send(
            reservasRemoteDataSource.getClientsByBusiness(
                businessId = businessId,
                fullName = fullName,
                email = email
            )
        )
    )
}

    override suspend fun createClient(
        request: ClientCreateRequest
    ): Flow<Resource<ClientResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.createClient(request)
            )
        )
    }

    override suspend fun updateClient(
        clientId:Int,
        request: ClientUpdateRequest
    ): Flow<Resource<ClientResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.updateClient(clientId = clientId, request = request)
            )
        )
    }

    override suspend fun deleteClient(
        clientId:Int,
    ): Resource<DefaultResponse> = ResponseToRequest.send(
                reservasRemoteDataSource.deleteClient(clientId)
    )




    override suspend fun getClientById(
        clientId: Int
    ): Flow<Resource<ClientResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getClientById(clientId)
            )
        )
    }


    //services

    override suspend fun getServicesByProvider(
        providerId: Int,
        name: String
    ): Flow<Resource<List<ServiceResponse>>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getServicesByProvider(providerId, name)
            )
        )
    }

    override suspend fun getStaffTotales(

    ): Flow<Resource<List<StaffResponse>>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getStaffTotales()
            )
        )
    }

    override suspend fun createService(
        request: ServiceCreateRequest
    ): Flow<Resource<ServiceResponse>> = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.createService(request)
            )
        )
    }

    override suspend fun updateService(
        serviceId: Int,
        request: ServiceUpdateRequest
    ): Flow<Resource<ServiceResponse>>  = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.updateService(serviceId, request)
            )
        )
    }

    override suspend fun getServiceById(
        serviceId: Int
    ): Flow<Resource<ServiceResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getServiceById(serviceId)
            )
        )
    }
}

