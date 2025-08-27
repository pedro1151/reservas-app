package com.optic.ecommerceappmvvm.domain.useCase.team

import com.optic.ecommerceappmvvm.domain.useCase.team.equipos.GetTeamByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureFollowedTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetNextFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetTopFiveFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetVersusFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.CreateFollowedLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.DeleteFollowedLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.GetFollowedLeaguesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.leagues.GetLeagueByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.players.GetPlayerLastTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.players.GetPlayerTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.standings.GetLeagueStandingsUC

data class TeamUseCase(

    // teams
    val getallTeamUseCase: GetallTeamUseCase,
    val getTeamByIdUC : GetTeamByIdUC,

    //players
    val getPlayersUseCase: GetPlayersUseCase,
    val getPlayerStatsUseCase: GetPlayerStatsUseCase,
    val getPlayerTeamsUC: GetPlayerTeamsUC,
    val getLeaguesUseCase    : GetLeaguesUseCase,
    val getPlayerLastTeamUC: GetPlayerLastTeamUC,

    // UC PLAYER SEGUIDOS
    val getFollowedPlayersUC   : GetFollowedPlayersUC,
    val createFollowedPlayerUC   : CreateFollowedPlayerUC,
    val deleteFollowedPlayerUC   : DeleteFollowedPlayerUC,

    // UC TEAM SEGUIDOS

    val getFollowedTeamsUC   : GetFollowedTeamsUC,
    val createFollowedTeamUC   : CreateFollowedTeamUC,
    val deleteFollowedTeamUC  : DeleteFollowedTeamUC,

    // matches ( FIxture)
    val getFixtureFollowedTeamsUC : GetFixtureFollowedTeamsUC,
    val getFixtureTeamUC : GetFixtureTeamUC,
    val getNextFixtureTeamUC : GetNextFixtureTeamUC,
    val getTopFiveFixtureTeamUC : GetTopFiveFixtureTeamUC,

    //FISTURE X ID
    val getFixtureByIdUC : GetFixtureByIdUC,


    // UC LEAGUES SEGUIDOS

    val getFollowedLeaguesUC   : GetFollowedLeaguesUC,
    val createFollowedLeagueUC   : CreateFollowedLeagueUC,
    val deleteFollowedLeagueUC  : DeleteFollowedLeagueUC,
    val getLeagueByIdUC: GetLeagueByIdUC,


    //standings
    val getLeagueStandingsUC : GetLeagueStandingsUC,

    // VERSUS
    val getVersusFixtureTeamUC: GetVersusFixtureTeamUC

)
