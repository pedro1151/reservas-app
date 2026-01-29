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
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorProviderUC
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
        // services
        getServicesPorProviderUC = GetServicesPorProviderUC(reservasRepository),
        createServiceUC = CreateServiceUC(reservasRepository),
        updateServiceUC = UpdateServiceUC(reservasRepository),
        getStaffTotalesUC = GetStaffTotalesUC(reservasRepository),
        getServicesPorIdUC = GetServicesPorIdUC(reservasRepository)

     /*
        //getallTeamUseCase = GetallTeamUseCase(teamRepository)

        getPlayersUseCase = GetPlayersUseCase(teamRepository),
        getPlayerStatsUseCase = GetPlayerStatsUseCase(teamRepository),
        getLeaguesUseCase = GetLeaguesUseCase(teamRepository),
        getFollowedPlayersUC = GetFollowedPlayersUC(teamRepository),
        createFollowedPlayerUC = CreateFollowedPlayerUC(teamRepository),
        deleteFollowedPlayerUC = DeleteFollowedPlayerUC(teamRepository),

        //players
        getAllPlayersUC = GetAllPlayersUC(teamRepository),
        getPlayerPorIdUC = GetPlayerPorIdUC(teamRepository),

        getFollowedTeamsUC = GetFollowedTeamsUC(teamRepository),
        createFollowedTeamUC = CreateFollowedTeamUC(teamRepository),
        deleteFollowedTeamUC =   DeleteFollowedTeamUC(teamRepository),

        getFixtureFollowedTeamsUC = GetFixtureFollowedTeamsUC(teamRepository),

        getFollowedLeaguesUC = GetFollowedLeaguesUC(teamRepository),
        createFollowedLeagueUC = CreateFollowedLeagueUC(teamRepository),
        deleteFollowedLeagueUC = DeleteFollowedLeagueUC(teamRepository),
        getTeamByIdUC = GetTeamByIdUC(teamRepository),
        //FIxtures
        getCountryFixturesUC = GetCountryFixturesUC(teamRepository),
        getFixtureTeamUC = GetFixtureTeamUC(teamRepository),
        getNextFixtureTeamUC = GetNextFixtureTeamUC(teamRepository),
        getTopFiveFixtureTeamUC = GetTopFiveFixtureTeamUC(teamRepository),
        getFixtureByIdUC = GetFixtureByIdUC(teamRepository),
        getFixtureLeagueUC =  GetFixtureLeagueUC(teamRepository),
        getFixtureByDateUC = GetFixtureByDateUC(teamRepository),
        saveFixturesCacheUC = SaveFixturesCacheUC(teamRepository),
        getFixtureByRangeUC = GetFixtureByRangeUC(teamRepository),

        //leagues
        getLeagueStandingsUC = GetLeagueStandingsUC(teamRepository),
        getVersusFixtureTeamUC = GetVersusFixtureTeamUC(teamRepository),
        getPlayerTeamsUC = GetPlayerTeamsUC(teamRepository),
        getPlayerLastTeamUC = GetPlayerLastTeamUC(teamRepository),
        getLeagueByIdUC = GetLeagueByIdUC(teamRepository),
        getNoFollowFixturesUC = GetNoFollowFixturesUC(teamRepository),
        getTopLeaguesUC = GetTopLeaguesUC(teamRepository),
        getProdeParticipateLeaguesUC = GetProdeParticipateLeaguesUC(teamRepository),

        //lineups
        getFixtureLineupsUC = GetFixtureLineupsUC(teamRepository),
        getFixtureStatsUC = GetFixtureStatsUC(teamRepository),

        //teams stats
        getTeamStatsUC = GetTeamStatsUC(teamRepository),

        //suggested teams
        getSuggestedTeamsUC = GetSuggestedTeamsUC(teamRepository),
        getFixturesByRoundUC = GetFixturesByRoundUC(teamRepository),

        // prode
        createFixturePredictionUC = CreateFixturePredictionUC(teamRepository),
        getUserFixturePredictionsUC = GetUserFixturePredictionsUC(teamRepository),
        getPredictionRankingUC = GetPredictionRankingUC(teamRepository),
        //cache prode
        syncCachePredictionsUC = SyncCachePredictionsUC(teamRepository),
        syncCacheUC = SyncCacheUC(teamRepository),
        getUserPredictionSummaryUC = GetUserPredictionSummaryUC(teamRepository)

      */


    )



    @Provides
    fun provideExternalUseCase(externalRepository: ExternalRepository) = ExternalUseCase(
        login = LoginGoogleUseCase(externalRepository)
    )








}