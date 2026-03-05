package com.optic.pramosreservasappz.di

import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.repository.ExternalRepository
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.useCase.auth.*
import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginPlessUC
import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginSendCodeUC
import com.optic.pramosreservasappz.domain.useCase.external.ExternalUseCase
import com.optic.pramosreservasappz.domain.useCase.external.LoginGoogleUseCase
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.CreateClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.DeleteClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.UpdateClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.CreateReservationUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.GetReservationByIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.GetReservationsByProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.GetReservationsUC
import com.optic.pramosreservasappz.domain.useCase.reservas.reservation.UpdateReservationUC
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
        loginSendCodeUC = LoginSendCodeUC(authRepository)

    )


    @Provides

    fun provideTeamUseCase(reservasRepository: ReservasRepository) = ReservasUC(

        //clients
        getClientPorProviderUC = GetClientPorProviderUC(reservasRepository),
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
        getReservationsByProviderUC = GetReservationsByProviderUC(reservasRepository)

    )



    @Provides
    fun provideExternalUseCase(externalRepository: ExternalRepository) = ExternalUseCase(
        login = LoginGoogleUseCase(externalRepository)
    )








}