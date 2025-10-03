package com.optic.ecommerceappmvvm.di

import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.repository.ExternalRepository
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository
import com.optic.ecommerceappmvvm.domain.repository.TriviasRepository
import com.optic.ecommerceappmvvm.domain.useCase.auth.*
import com.optic.ecommerceappmvvm.domain.useCase.external.ExternalUseCase
import com.optic.ecommerceappmvvm.domain.useCase.external.LoginGoogleUseCase
import com.optic.ecommerceappmvvm.domain.useCase.team.CreateFollowedPlayerUC
import com.optic.ecommerceappmvvm.domain.useCase.team.CreateFollowedTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.DeleteFollowedPlayerUC
import com.optic.ecommerceappmvvm.domain.useCase.team.DeleteFollowedTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.GetFollowedPlayersUC
import com.optic.ecommerceappmvvm.domain.useCase.team.GetFollowedTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.GetLeaguesUseCase
import com.optic.ecommerceappmvvm.domain.useCase.team.GetPlayerStatsUseCase
import com.optic.ecommerceappmvvm.domain.useCase.team.GetPlayersUseCase
import com.optic.ecommerceappmvvm.domain.useCase.team.GetallTeamUseCase
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import com.optic.ecommerceappmvvm.domain.useCase.team.equipos.GetTeamByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetCountryFixturesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureFollowedTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetNextFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetNoFollowFixturesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetTopFiveFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetVersusFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.CreateFollowedLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.DeleteFollowedLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.GetFollowedLeaguesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.leagues.GetLeagueByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.players.GetPlayerLastTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.players.GetPlayerTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.standings.GetLeagueStandingsUC
import com.optic.ecommerceappmvvm.domain.useCase.trivias.GetSimilarPlayers
import com.optic.ecommerceappmvvm.domain.useCase.trivias.TriviasUseCase
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
        logout = LogoutUseCase(authRepository)
    )

    @Provides
    fun provideTeamUseCase(teamRepository: TeamRepository) = TeamUseCase(
        getallTeamUseCase = GetallTeamUseCase(teamRepository),
        getPlayersUseCase = GetPlayersUseCase(teamRepository),
        getPlayerStatsUseCase = GetPlayerStatsUseCase(teamRepository),
        getLeaguesUseCase = GetLeaguesUseCase(teamRepository),
        getFollowedPlayersUC = GetFollowedPlayersUC(teamRepository),
        createFollowedPlayerUC = CreateFollowedPlayerUC(teamRepository),
        deleteFollowedPlayerUC = DeleteFollowedPlayerUC(teamRepository),

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
        //leagues
        getLeagueStandingsUC = GetLeagueStandingsUC(teamRepository),
        getVersusFixtureTeamUC = GetVersusFixtureTeamUC(teamRepository),
        getPlayerTeamsUC = GetPlayerTeamsUC(teamRepository),
        getPlayerLastTeamUC = GetPlayerLastTeamUC(teamRepository),
        getLeagueByIdUC = GetLeagueByIdUC(teamRepository),
        getNoFollowFixturesUC = GetNoFollowFixturesUC(teamRepository)


    )

    @Provides
    fun provideExternalUseCase(externalRepository: ExternalRepository) = ExternalUseCase(
        login = LoginGoogleUseCase(externalRepository)
    )


    @Provides
    fun provideTriviaslUseCase(triviasRepository: TriviasRepository) = TriviasUseCase(
        getSimilarPlayers =  GetSimilarPlayers(triviasRepository)
    )








}