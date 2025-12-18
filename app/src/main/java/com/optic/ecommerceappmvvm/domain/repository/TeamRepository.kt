package com.optic.ecommerceappmvvm.domain.repository

import com.optic.ecommerceappmvvm.data.dataSource.local.mapper.toRequest
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.lineups.FixtureLineupsResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.stats.FixtureStatsResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionResponse
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamStatsResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    // teams
    suspend fun getAll(name: String, country:String, page: Int, size: Int): Flow<Resource<List<Team>>>
    suspend fun getSuggestedTeams(limit: Int): Flow<Resource<List<Team>>>
    suspend fun getTeamById(teamId: Int): Flow<Resource<TeamResponse>>

    //PLayers
    suspend fun getPlayers(name: String?, page: Int, size:Int): Flow<Resource<List<Player>>>
    suspend fun getallPlayers(): Flow<Resource<List<Player>>>
    suspend fun getPlayerStats(playerId: Int): Flow<Resource<PlayerWithStats>>
    suspend fun getPlayerTeams(playerId: Int): Flow<Resource<PlayerTeamsResponse>>
    suspend fun getPlayerLastTeam(playerId: Int): Flow<Resource<PlayerLastTeamResponse>>

    //PLAYERS SEGUIDOS
    suspend fun getFollowedPlayers(): Flow<Resource<List<Player>>>
    suspend fun createFollowedPlayer(playerId: Int):Flow<Resource<FollowedPlayerResponse>>
    suspend fun deleteFollowedPlayer(playerId: Int):Flow<Resource<DefaultResponse>>


    //TEAMS SEGUIDOS
    suspend fun getFollowedTeams(): Flow<Resource<List<Team>>>
    suspend fun createFollowedTeam(teamId: Int):Flow<Resource<FollowedTeamResponse>>
    suspend fun deleteFollowedTeam(teamId: Int):Flow<Resource<DefaultResponse>>
    suspend fun getTeamStats(season: Int, teamId: Int, date: String? =  null):Flow<Resource<TeamStatsResponse>>


    //ligas
    suspend fun getFollowedLeagues(): Flow<Resource<List<League>>>
    suspend fun createFollowedLeague(
    leagueId: Int,
    isAuthenticated: Boolean):Flow<Resource<FollowedLeagueResponse>>


    suspend fun deleteFollowedLeague(leagueId: Int, isAuthenticated: Boolean):Flow<Resource<DefaultResponse>>
    suspend fun getLeagues(name: String, type: String, countryName: String): Flow<Resource<List<League>>>
    suspend fun getLeagueById(leagueId:Int): Flow<Resource<LeagueCompleteResponse>>
    suspend fun getTopLeagues(): Flow<Resource<List<League>>>


    //FIXTURE
    suspend fun getCountryFixtures(season: Int, date: String): Flow<Resource<List<FixtureResponse>>>
    suspend fun getFixturesByRound(leagueId: Int, season: Int, round: String): Flow<Resource<List<FixtureResponse>>>
    suspend fun getFixtureById(id: Int): Flow<Resource<FixtureResponse>>
    suspend fun getFixtureLineups(id: Int): Flow<Resource<FixtureLineupsResponse>>
    suspend fun getFixtureStats(id: Int): Flow<Resource<FixtureStatsResponse>>
    suspend fun getFixtureFollowedTeams(season: Int, date: String): Flow<Resource<List<FixtureResponse>>>
    suspend fun getNoFollowFixtures(season: Int, date: String): Flow<Resource<List<FixtureResponse>>>
    suspend fun getFixtureTeam(teamId: Int): Flow<Resource<List<FixtureResponse>>>
    suspend fun getNextFixtureTeam(teamId: Int): Flow<Resource<FixtureResponse>>
    suspend fun getTopFiveFixtureTeam(teamId: Int): Flow<Resource<List<FixtureResponse>>>
    suspend fun getLeagueFixture(leagueId: Int, season: Int, teamId: Int): Flow<Resource<List<FixtureResponse>>>
    suspend fun getFixturesByDate(date: String, limit: Int): Flow<Resource<List<FixtureResponse>>>
    suspend fun getFixturesByRange(dateStart: String, dateEnd:String): Flow<Resource<List<FixtureResponse>>>

    // funciones de carga de cache
    suspend fun precacheFixturesAroundToday()
    suspend fun precacheAllLeagues()
    suspend fun precacheAllPlayers()
    suspend fun precacheAllTeams(
        name:String,
        country: String,
        page: Int,
        size: Int
    )

    suspend fun syncCachedPredictions()
    //

    //Versus
    suspend fun getFixtureVersus(
        teamOneId: Int,
        teamTwoId: Int,
        leagueId: Int,
        season: Int
    ): Flow<Resource<List<FixtureResponse>>>
    //STANDINGS
    suspend fun getLeagueStandings(leagueId: Int, season: Int): Flow<Resource<List<StandingResponse>>>


    // prodes
    suspend fun createFixturePrediction(
        request: FixturePredictionRequest,
        isAuthenticated: Boolean
    ):Flow<Resource<FixturePredictionResponse>>
    suspend fun getUserFixturePredictions(leagueId: Int, season: Int):Flow<Resource<List<FixturePredictionResponse>>>
}