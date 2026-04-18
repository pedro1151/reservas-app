package com.optic.pramosreservasappz.di

import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.repository.ExternalRepository
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.useCase.auth.*
import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginPlessUC
import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginSendCodeUC
import com.optic.pramosreservasappz.domain.useCase.business.CreateColaboradorUC
import com.optic.pramosreservasappz.domain.useCase.business.GetBusinessByIdUC
import com.optic.pramosreservasappz.domain.useCase.business.GetBusinessMembersUC
import com.optic.pramosreservasappz.domain.useCase.external.ExternalUseCase
import com.optic.pramosreservasappz.domain.useCase.external.LoginGoogleUseCase
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.CreateClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.DeleteClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorOwnerUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.UpdateClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.product.CreateProductSafeUC
import com.optic.pramosreservasappz.domain.useCase.reservas.product.CreateProductUC
import com.optic.pramosreservasappz.domain.useCase.reservas.product.DeleteProductHardUC
import com.optic.pramosreservasappz.domain.useCase.reservas.product.DeleteProductSoftUC
import com.optic.pramosreservasappz.domain.useCase.reservas.product.GetProductByIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.product.GetProductsByUserUC
import com.optic.pramosreservasappz.domain.useCase.reservas.product.UpdateProductUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.CreateReservationUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.GetReservationByIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.GetReservationsByProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.GetReservationsUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.UpdateReservationUC
import com.optic.pramosreservasappz.domain.useCase.reservas.saleitem.CreateSaleItemBulkUC
import com.optic.pramosreservasappz.domain.useCase.reservas.saleitem.CreateSaleItemUC
import com.optic.pramosreservasappz.domain.useCase.reservas.saleitem.DeleteSaleItemHardUC
import com.optic.pramosreservasappz.domain.useCase.reservas.saleitem.DeleteSaleItemSoftUC
import com.optic.pramosreservasappz.domain.useCase.reservas.saleitem.GetItemsBySaleUC
import com.optic.pramosreservasappz.domain.useCase.reservas.saleitem.GetSaleItemByIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.saleitem.UpdateSaleItemUC
import com.optic.pramosreservasappz.domain.useCase.reservas.sales.CreateSaleUC
import com.optic.pramosreservasappz.domain.useCase.reservas.sales.CreateSaleWithItemsUC
import com.optic.pramosreservasappz.domain.useCase.reservas.sales.DeleteSaleHardUC
import com.optic.pramosreservasappz.domain.useCase.reservas.sales.DeleteSaleSoftUC
import com.optic.pramosreservasappz.domain.useCase.reservas.sales.GetSaleByIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.sales.GetSalesByOwnerUC
import com.optic.pramosreservasappz.domain.useCase.reservas.sales.UpdateSaleUC
import com.optic.pramosreservasappz.domain.useCase.reservas.salestats.GetSaleStatsUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.CreateServiceUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.GetServicesPorIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.GetServicesPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.UpdateServiceUC
import com.optic.pramosreservasappz.domain.useCase.reservas.staff.GetStaffTotalesUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAuthUseCase(authRepository: AuthRepository) = AuthUseCase(
        login = LoginUseCase(authRepository),
        register = RegisterUseCase(authRepository),
        saveSession = SaveSessionUseCase(authRepository),
        getSessionData = GetSessionDataUseCase(authRepository),
        logout = LogoutUseCase(authRepository),
        loginPlessUC = LoginPlessUC(authRepository),
        loginSendCodeUC = LoginSendCodeUC(authRepository),

        //buesiness
        createColaboradorUC = CreateColaboradorUC(authRepository),
        getBusinessMembersUC = GetBusinessMembersUC(authRepository),
        getBusinessByIdUC = GetBusinessByIdUC(authRepository)

    )


    @Provides

    fun provideTeamUseCase(reservasRepository: ReservasRepository) = ReservasUC(

        //clients
        getClientPorOwnerUC = GetClientPorOwnerUC(reservasRepository),
        getClientPorIdUC = GetClientPorIdUC(reservasRepository),
        createClientUC = CreateClientUC(reservasRepository),
        updateClientUC = UpdateClientUC(reservasRepository),
        deleteClientUC = DeleteClientUC(reservasRepository),


        // services
        getServicesPorProviderUC = GetServicesPorProviderUC(reservasRepository),
        createServiceUC = CreateServiceUC(reservasRepository),
        updateServiceUC = UpdateServiceUC(reservasRepository),
        getStaffTotalesUC = GetStaffTotalesUC(reservasRepository),
        getServicesPorIdUC = GetServicesPorIdUC(reservasRepository),

        // reservas
        getReservationsUC = GetReservationsUC(reservasRepository),
        createReservationUC = CreateReservationUC(reservasRepository),
        updateReservationUC = UpdateReservationUC(reservasRepository),
        getReservationByIdUC = GetReservationByIdUC(reservasRepository),
        getReservationsByProviderUC = GetReservationsByProviderUC(reservasRepository),

        // ventas
        createSaleUC = CreateSaleUC(reservasRepository),
        createSaleWithItemsUC = CreateSaleWithItemsUC(reservasRepository),
        getSaleByIdUC = GetSaleByIdUC(reservasRepository),
        getSalesByOwnerUC = GetSalesByOwnerUC(reservasRepository),
        updateSaleUC = UpdateSaleUC(reservasRepository),
        deleteSaleHardUC = DeleteSaleHardUC(reservasRepository),
        deleteSaleSoftUC = DeleteSaleSoftUC(reservasRepository),

        //items
        createSaleItemUC = CreateSaleItemUC(reservasRepository),
        createSaleItemBulkUC = CreateSaleItemBulkUC(reservasRepository),
        getItemsBySaleUC = GetItemsBySaleUC(reservasRepository),
        getSaleItemByIdUC = GetSaleItemByIdUC(reservasRepository),
        updateSaleItemUC = UpdateSaleItemUC(reservasRepository),
        deleteSaleItemHardUC = DeleteSaleItemHardUC(reservasRepository),
        deleteSaleItemSoftUC = DeleteSaleItemSoftUC(reservasRepository),


        //products
        createProductUC = CreateProductUC(reservasRepository),
        createProductSafeUC = CreateProductSafeUC(reservasRepository),
        getProductsByUserUC = GetProductsByUserUC(reservasRepository),
        getProductByIdUC = GetProductByIdUC(reservasRepository),
        updateProductUC = UpdateProductUC(reservasRepository),
        deleteProductHardUC = DeleteProductHardUC(reservasRepository),
        deleteProductSoftUC = DeleteProductSoftUC(reservasRepository),


        // sale stats

        getSaleStatsUC = GetSaleStatsUC(reservasRepository)

    )



    @Provides
    fun provideExternalUseCase(externalRepository: ExternalRepository) = ExternalUseCase(
        login = LoginGoogleUseCase(externalRepository)
    )








}