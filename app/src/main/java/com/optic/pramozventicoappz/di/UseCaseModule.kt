package com.optic.pramozventicoappz.di

import com.optic.pramozventicoappz.domain.repository.AuthRepository
import com.optic.pramozventicoappz.domain.repository.ExternalRepository
import com.optic.pramozventicoappz.domain.repository.ReservasRepository
import com.optic.pramozventicoappz.domain.useCase.auth.*
import com.optic.pramozventicoappz.domain.useCase.auth.loginpless.LoginPlessUC
import com.optic.pramozventicoappz.domain.useCase.auth.loginpless.LoginSendCodeUC
import com.optic.pramozventicoappz.domain.useCase.business.CreateColaboradorUC
import com.optic.pramozventicoappz.domain.useCase.business.GetBusinessByIdUC
import com.optic.pramozventicoappz.domain.useCase.business.GetBusinessMembersUC
import com.optic.pramozventicoappz.domain.useCase.business.GetMemberUC
import com.optic.pramozventicoappz.domain.useCase.business.UpdateColaboradorUC
import com.optic.pramozventicoappz.domain.useCase.external.ExternalUseCase
import com.optic.pramozventicoappz.domain.useCase.external.LoginGoogleUseCase
import com.optic.pramozventicoappz.domain.useCase.external.googleplaybilling.GetMyEntitlementUC
import com.optic.pramozventicoappz.domain.useCase.external.googleplaybilling.GetUserEntitlementsUC
import com.optic.pramozventicoappz.domain.useCase.external.googleplaybilling.GetUserPurchasesUC
import com.optic.pramozventicoappz.domain.useCase.external.googleplaybilling.GooglePlayVerifyPurchasesUC
import com.optic.pramozventicoappz.domain.useCase.reservas.ReservasUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.CreateClientUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.DeleteClientUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.GetClientPorBusinessUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.GetClientPorIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.UpdateClientUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.CreateProductSafeUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.CreateProductUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.DeleteProductHardUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.DeleteProductSoftUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.GetProductByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.GetProductsByBusinessUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.UpdateProductUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.CreateReservationUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.GetReservationByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.GetReservationsByProviderUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.GetReservationsUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.UpdateReservationUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.CreateSaleItemBulkUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.CreateSaleItemUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.DeleteSaleItemHardUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.DeleteSaleItemSoftUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.GetItemsBySaleUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.GetSaleItemByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.UpdateSaleItemUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.CreateSaleUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.CreateSaleWithItemsUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.DeleteSaleHardUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.DeleteSaleSoftUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.GetSaleByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.GetSalesByBusinessUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.UpdateSaleUC
import com.optic.pramozventicoappz.domain.useCase.reservas.salestats.GetSaleStatsUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.CreateServiceUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.GetServicesPorIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.GetServicesPorProviderUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.UpdateServiceUC
import com.optic.pramozventicoappz.domain.useCase.reservas.staff.GetStaffTotalesUC
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
        getBusinessByIdUC = GetBusinessByIdUC(authRepository),
        updateColaboradorUC = UpdateColaboradorUC(authRepository),
        getMemberUC = GetMemberUC(authRepository)

    )


    @Provides

    fun provideTeamUseCase(reservasRepository: ReservasRepository) = ReservasUC(

        //clients
        getClientPorBusinessUC = GetClientPorBusinessUC(reservasRepository),
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
        getSalesByBusinessUC = GetSalesByBusinessUC(reservasRepository),
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
        getProductsByBusinessUC = GetProductsByBusinessUC(reservasRepository),
        getProductByIdUC = GetProductByIdUC(reservasRepository),
        updateProductUC = UpdateProductUC(reservasRepository),
        deleteProductHardUC = DeleteProductHardUC(reservasRepository),
        deleteProductSoftUC = DeleteProductSoftUC(reservasRepository),


        // sale stats

        getSaleStatsUC = GetSaleStatsUC(reservasRepository)

    )



    @Provides
    fun provideExternalUseCase(externalRepository: ExternalRepository) = ExternalUseCase(
        login = LoginGoogleUseCase(externalRepository),

        // google play billings

        googlePlayVerifyPurchasesUC = GooglePlayVerifyPurchasesUC(externalRepository),
        getUserPurchasesUC = GetUserPurchasesUC(externalRepository),
        getMyEntitlementUC =  GetMyEntitlementUC(externalRepository),
        getUserEntitlementsUC = GetUserEntitlementsUC(externalRepository)
    )








}