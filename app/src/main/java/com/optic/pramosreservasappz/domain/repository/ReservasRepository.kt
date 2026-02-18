package com.optic.pramosreservasappz.domain.repository

import com.optic.pramosreservasappz.domain.model.League.League
import com.optic.pramosreservasappz.domain.model.League.LeagueCompleteResponse
import com.optic.pramosreservasappz.domain.model.player.Player
import com.optic.pramosreservasappz.domain.model.Team
import com.optic.pramosreservasappz.domain.model.fixture.FixtureResponse
import com.optic.pramosreservasappz.domain.model.fixture.lineups.FixtureLineupsResponse
import com.optic.pramosreservasappz.domain.model.fixture.stats.FixtureStatsResponse
import com.optic.pramosreservasappz.domain.model.followed.FollowedLeagueResponse
import com.optic.pramosreservasappz.domain.model.followed.FollowedPlayerResponse
import com.optic.pramosreservasappz.domain.model.followed.FollowedTeamResponse
import com.optic.pramosreservasappz.domain.model.player.PlayerComplete
import com.optic.pramosreservasappz.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.pramosreservasappz.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.pramosreservasappz.domain.model.player.stats.PlayerWithStats
import com.optic.pramosreservasappz.domain.model.prode.FixturePredictionRequest
import com.optic.pramosreservasappz.domain.model.prode.FixturePredictionResponse
import com.optic.pramosreservasappz.domain.model.prode.UserPredictionRanking
import com.optic.pramosreservasappz.domain.model.prode.UserPredictionSummaryEnriched
import com.optic.pramosreservasappz.domain.model.prode.UserPredictionSummaryResponse
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationResponse
import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.staff.StaffResponse
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.model.standing.StandingResponse
import com.optic.pramosreservasappz.domain.model.team.TeamResponse
import com.optic.pramosreservasappz.domain.model.team.TeamStatsResponse
import com.optic.pramosreservasappz.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

interface ReservasRepository {

    // reservas

    suspend fun createReservation(
        request: ReservationCreateRequest
    ): Flow<Resource<ReservationResponse>>

    suspend fun updateReservation(
        reservationId: Int,
        request: ReservationUpdateRequest
    ): Flow<Resource<ReservationResponse>>

    suspend fun getReservationById(
        reservationId: Int,
    ): Flow<Resource<ReservationResponse>>

    suspend fun getReservations(
    ): Flow<Resource<List<ReservationResponse>>>

    // clients
    suspend fun getClientsByProvider(
        providerId: Int,
        fullName:String,
        email:String
    ): Flow<Resource<List<ClientResponse>>>

    suspend fun createClient(
        request: ClientCreateRequest
    ): Flow<Resource<ClientResponse>>

    suspend fun updateClient(
        clientId: Int,
        request: ClientUpdateRequest
    ): Flow<Resource<ClientResponse>>

    suspend fun deleteClient(
        clientId:Int,
    ): Resource<DefaultResponse>


    suspend fun getClientById(
        clientId: Int,
    ): Flow<Resource<ClientResponse>>


    //services
    suspend fun createService(
        request: ServiceCreateRequest
    ): Flow<Resource<ServiceResponse>>

    suspend fun updateService(
        serviceId: Int,
        request: ServiceUpdateRequest
    ): Flow<Resource<ServiceResponse>>

    suspend fun getServiceById(
        serviceId: Int,
    ): Flow<Resource<ServiceResponse>>

    suspend fun getServicesByProvider(providerId: Int, name:String): Flow<Resource<List<ServiceResponse>>>
    suspend fun getStaffTotales(): Flow<Resource<List<StaffResponse>>>


    /* suspend fun getSuggestedTeams(limit: Int): Flow<Resource<List<Team>>>
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

 */

}