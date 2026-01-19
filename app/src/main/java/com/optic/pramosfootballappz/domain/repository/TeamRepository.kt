package com.optic.pramosfootballappz.domain.repository

import com.optic.pramosfootballappz.domain.model.League.League
import com.optic.pramosfootballappz.domain.model.League.LeagueCompleteResponse
import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.domain.model.Team
import com.optic.pramosfootballappz.domain.model.fixture.FixtureResponse
import com.optic.pramosfootballappz.domain.model.fixture.lineups.FixtureLineupsResponse
import com.optic.pramosfootballappz.domain.model.fixture.stats.FixtureStatsResponse
import com.optic.pramosfootballappz.domain.model.followed.FollowedLeagueResponse
import com.optic.pramosfootballappz.domain.model.followed.FollowedPlayerResponse
import com.optic.pramosfootballappz.domain.model.followed.FollowedTeamResponse
import com.optic.pramosfootballappz.domain.model.player.PlayerComplete
import com.optic.pramosfootballappz.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.pramosfootballappz.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.pramosfootballappz.domain.model.player.stats.PlayerWithStats
import com.optic.pramosfootballappz.domain.model.prode.FixturePredictionRequest
import com.optic.pramosfootballappz.domain.model.prode.FixturePredictionResponse
import com.optic.pramosfootballappz.domain.model.prode.UserPredictionRanking
import com.optic.pramosfootballappz.domain.model.prode.UserPredictionSummaryEnriched
import com.optic.pramosfootballappz.domain.model.prode.UserPredictionSummaryResponse
import com.optic.pramosfootballappz.domain.model.response.DefaultResponse
import com.optic.pramosfootballappz.domain.model.standing.StandingResponse
import com.optic.pramosfootballappz.domain.model.team.TeamResponse
import com.optic.pramosfootballappz.domain.model.team.TeamStatsResponse
import com.optic.pramosfootballappz.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TeamRepository {

    // teams
    suspend fun getAll(name: String, country:String, page: Int, size: Int): Flow<Resource<List<Team>>>
    suspend fun getSuggestedTeams(limit: Int): Flow<Resource<List<Team>>>
    suspend fun getTeamById(teamId: Int): Flow<Resource<TeamResponse>>

    //PLayers
    suspend fun getPlayers(name: String?, page: Int, size:Int): Flow<Resource<List<Player>>>
    suspend fun getallPlayers(): Flow<Resource<List<Player>>>
    suspend fun getPlayerPorId(playerId: Int): Flow<Resource<PlayerComplete>>

    suspend fun getPlayerStats(playerId: Int): Flow<Resource<PlayerWithStats>>
    suspend fun getPlayerTeams(playerId: Int): Flow<Resource<PlayerTeamsResponse>>
    suspend fun getPlayerLastTeam(playerId: Int): Flow<Resource<PlayerLastTeamResponse>>

    //PLAYERS SEGUIDOS
    fun getFollowedPlayers(): Flow<Resource<List<Player>>>
    suspend fun createFollowedPlayer(playerId: Int, isAuthenticated: Boolean):Flow<Resource<FollowedPlayerResponse>>
    suspend fun deleteFollowedPlayer(playerId: Int, isAuthenticated: Boolean):Flow<Resource<DefaultResponse>>


    //TEAMS SEGUIDOS
    fun getFollowedTeams(): Flow<Resource<List<Team>>>
    suspend fun createFollowedTeam(teamId: Int):Flow<Resource<FollowedTeamResponse>>
    suspend fun deleteFollowedTeam(teamId: Int):Flow<Resource<DefaultResponse>>
    suspend fun getTeamStats(season: Int, teamId: Int, date: String? =  null):Flow<Resource<TeamStatsResponse>>


    //ligas
    suspend fun getFollowedLeagues(): Flow<Resource<List<League>>>
    suspend fun createFollowedLeague(
    leagueId: Int,
    isAuthenticated: Boolean):Flow<Resource<FollowedLeagueResponse>>
    //cache leagues
    suspend fun getProdeParticipateLeagues(userId:Int): Flow<Resource<List<League>>>


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

    // sincronizar cache
    suspend fun syncCached() //llama a todos
    suspend fun syncCachedPredictions()
    suspend fun syncCachedPlayers()
    suspend fun syncCachedTeams()
    suspend fun syncCachedLeagues()
    suspend fun updateFixturesByDate(
        date: String,
        limit: Int
    ): Flow<Resource<List<FixtureResponse>>>

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

    suspend fun getUserFixturePredictions(
        leagueId: Int,
        season: Int
    )
    :Flow<Resource<List<FixturePredictionResponse>>>

    suspend fun getPredictionRanking(top: Int):Flow<Resource<List<UserPredictionRanking>>>
    suspend fun getUserPredictionSummary(userId: Int, season: Int): Resource<UserPredictionSummaryResponse>
    suspend fun getUserPredictionSummaryEnriched(
        userId: Int,
        season: Int
    ): Flow<Resource<UserPredictionSummaryEnriched>>



}