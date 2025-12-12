package com.optic.ecommerceappmvvm.domain.useCase.team

import com.optic.ecommerceappmvvm.domain.useCase.team.equipos.GetSuggestedTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.equipos.GetTeamByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.equipos.GetTeamStatsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetCountryFixturesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureByDateUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureByRangeUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureFollowedTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureLineupsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureStatsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetFixturesByRoundUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetNextFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetNoFollowFixturesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetTopFiveFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.GetVersusFixtureTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.fixture.cache.SaveFixturesCacheUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.CreateFollowedLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.DeleteFollowedLeagueUC
import com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues.GetFollowedLeaguesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.leagues.GetLeagueByIdUC
import com.optic.ecommerceappmvvm.domain.useCase.team.leagues.GetTopLeaguesUC
import com.optic.ecommerceappmvvm.domain.useCase.team.players.GetAllPlayersUC
import com.optic.ecommerceappmvvm.domain.useCase.team.players.GetPlayerLastTeamUC
import com.optic.ecommerceappmvvm.domain.useCase.team.players.GetPlayerTeamsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.prode.CreateFixturePredictionUC
import com.optic.ecommerceappmvvm.domain.useCase.team.prode.GetUserFixturePredictionsUC
import com.optic.ecommerceappmvvm.domain.useCase.team.standings.GetLeagueStandingsUC

data class TeamUseCase(

    // teams
    val getallTeamUseCase: GetallTeamUseCase,
    val getTeamByIdUC : GetTeamByIdUC,
    val getSuggestedTeamsUC: GetSuggestedTeamsUC,

    //players
    val getPlayersUseCase: GetPlayersUseCase,
    val getPlayerStatsUseCase: GetPlayerStatsUseCase,
    val getPlayerTeamsUC: GetPlayerTeamsUC,
    val getLeaguesUseCase    : GetLeaguesUseCase,
    val getPlayerLastTeamUC: GetPlayerLastTeamUC,
    val getAllPlayersUC: GetAllPlayersUC,

    // UC PLAYER SEGUIDOS
    val getFollowedPlayersUC   : GetFollowedPlayersUC,
    val createFollowedPlayerUC   : CreateFollowedPlayerUC,
    val deleteFollowedPlayerUC   : DeleteFollowedPlayerUC,

    // UC TEAM SEGUIDOS

    val getFollowedTeamsUC   : GetFollowedTeamsUC,
    val createFollowedTeamUC   : CreateFollowedTeamUC,
    val deleteFollowedTeamUC  : DeleteFollowedTeamUC,
    val getTeamStatsUC: GetTeamStatsUC,

    // matches ( FIxture)
    val getCountryFixturesUC: GetCountryFixturesUC,
    val getFixtureFollowedTeamsUC : GetFixtureFollowedTeamsUC,
    val getNoFollowFixturesUC: GetNoFollowFixturesUC,
    val getFixtureTeamUC : GetFixtureTeamUC,
    val getNextFixtureTeamUC : GetNextFixtureTeamUC,
    val getTopFiveFixtureTeamUC : GetTopFiveFixtureTeamUC,
    val getFixtureLeagueUC: GetFixtureLeagueUC,
    val getFixtureByDateUC: GetFixtureByDateUC,
    val getFixtureByRangeUC: GetFixtureByRangeUC,
    val getFixturesByRoundUC: GetFixturesByRoundUC,

    // cache fixtures
    val saveFixturesCacheUC: SaveFixturesCacheUC,

    //FISTURE X ID
    val getFixtureByIdUC : GetFixtureByIdUC,
    val getFixtureLineupsUC: GetFixtureLineupsUC,
    val getFixtureStatsUC: GetFixtureStatsUC,


    // UC LEAGUES SEGUIDOS

    val getFollowedLeaguesUC   : GetFollowedLeaguesUC,
    val createFollowedLeagueUC   : CreateFollowedLeagueUC,
    val deleteFollowedLeagueUC  : DeleteFollowedLeagueUC,
    val getLeagueByIdUC: GetLeagueByIdUC,
    val getTopLeaguesUC: GetTopLeaguesUC,


    //standings
    val getLeagueStandingsUC : GetLeagueStandingsUC,

    // VERSUS
    val getVersusFixtureTeamUC: GetVersusFixtureTeamUC,


    // prodes
    val createFixturePredictionUC: CreateFixturePredictionUC,
    val getUserFixturePredictionsUC: GetUserFixturePredictionsUC

)
