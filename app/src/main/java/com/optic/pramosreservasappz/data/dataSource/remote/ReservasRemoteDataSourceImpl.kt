package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.data.dataSource.remote.service.ReservasService
import com.optic.pramosreservasappz.domain.model.Team
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
import retrofit2.Response

class ReservasRemoteDataSourceImpl (private val reservasService: ReservasService): ReservasRemoteDataSource {

    /*
    override suspend fun getSuggestedTeams(limit: Int): Response<List<Team>> = teamService.getSuggestedTeams(limit)

    override suspend fun getTeamById(teamId: Int): Response<TeamResponse> = teamService.getTeamById(teamId)



    // PLAYERS
    override suspend fun getPlayers(name: String?, page: Int, size:Int): Response<List<Player>> = teamService.getPlayers(name, page, size)
    override suspend fun getallPlayers(): Response<List<Player>> = teamService.getallPlayers()
    override suspend fun getPlayerPorId(
        playerId: Int
    ): Response<PlayerComplete> = teamService.getPlayerPorId(playerId)

    override suspend fun getPlayerStats(playerId: Int): Response<PlayerWithStats> = teamService.getPlayerStats(playerId)
    override suspend fun getPlayerTeams(playerId: Int): Response<PlayerTeamsResponse> = teamService.getPlayerTeams(playerId)
    override suspend fun getPlayerLastTeam(playerId: Int): Response<PlayerLastTeamResponse> = teamService.getPlayerLastTeam(playerId)
    override suspend fun createFollowedPlayer(playerId: Int): Response<FollowedPlayerResponse> = teamService.createFollowedPlayer(FollowedPlayerRequest( playerId))
    override suspend fun getFollowedPlayers(): Response<List<Player>> = teamService.getFollowedPlayers()
    override suspend fun deleteFollowedPlayer(playerId: Int): Response<DefaultResponse> = teamService.deleteFollowedPlayer(playerId)

    //LIGAS
    override suspend fun getLeagues(
        name: String,
        type: String,
        countryName: String
    ): Response<List<League>> = teamService.getLeagues(name, type, countryName)

    override suspend fun getLeagueById(leagueId: Int): Response<LeagueCompleteResponse> = teamService.getLeagueById(leagueId)
    override suspend fun getTopLeagues(): Response<List<League>> = teamService.getTopLeagues()
    override suspend fun getProdeParticipateLeagues(userId: Int): Response<List<League>> = teamService.getProdeParticipateLeagues(userId)
    override suspend fun getCountryFixtures(
        season: Int,
        date: String
    ): Response<List<FixtureResponse>> = teamService.getContryFixtures(season, date)

    override suspend fun getFixturesByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Response<List<FixtureResponse>> = teamService.getFixturesByRound(leagueId, season, round)

    // FOLLOWED TEAMS
    override suspend fun createFollowedTeam(teamId: Int): Response<FollowedTeamResponse>  = teamService.createFollowedTeam(
        FollowedTeamRequest(teamId)
    )

    override suspend fun getFollowedTeams(): Response<List<Team>> = teamService.getFollowedTeams()

    override suspend fun deleteFollowedTeam(teamId: Int): Response<DefaultResponse> = teamService.deleteFollowedTeam(teamId)



    // FOLLOWED LEAGUES
    override suspend fun createFollowedLeague(leagueId: Int): Response<FollowedLeagueResponse> = teamService.createFollowedLeague(
        FollowedLeagueRequest(leagueId)
    )

    override suspend fun getFollowedLeagues(): Response<List<League>> = teamService.getFollowedLeagues()
    override suspend fun deleteFollowedLeague(leagueId: Int): Response<DefaultResponse> = teamService.deleteFollowedLeague(leagueId)



    //FIXTURES (MATCHES)
    override suspend fun getFixtureById(
        id: Int
    ): Response<FixtureResponse> = teamService.getFixtureById(id)

    override suspend fun getFixtureLineups(
        id: Int
    ): Response<FixtureLineupsResponse> = teamService.getFixtureLineups(id)

    override suspend fun getFixtureStats(
        id: Int
    ): Response<FixtureStatsResponse> = teamService.getFixtureStats(id)

    override suspend fun getTeamStats(
        season: Int,
        teamId: Int,
        date: String?
    ): Response<TeamStatsResponse> = teamService.getTeamStats(season, teamId, date)


    //FIXTURES (MATCHES)
    override suspend fun getFixtureFollowedTeams(
        season: Int,
        date: String
    ): Response<List<FixtureResponse>> = teamService.getFixtureFollowedTeams(season, date)

    override suspend fun getNoFollowFixtures(
        season: Int,
        date: String
    ): Response<List<FixtureResponse>> = teamService.getNoFollowFixtures(season, date)


    override suspend fun getFixtureTeam(
        teamId: Int
    ): Response<List<FixtureResponse>> = teamService.getFixtureTeam(teamId)



    override suspend fun getNextFixtureTeam(
        teamId: Int
    ): Response<FixtureResponse> = teamService.getNextFixtureTeam(teamId)


    override suspend fun getTopFiveFixtureTeam(
        teamId: Int
    ): Response<List<FixtureResponse>> = teamService.getTopFiveFixtureTeam(teamId)

    override suspend fun getFixturesByDate(
        date: String,
        limit: Int
    ): Response<List<FixtureResponse>> = teamService.getFixturesByDate(date, limit)

    override suspend fun getFixturesByRange(
        dateStart: String,
        dateEnd: String
    ): Response<List<FixtureResponse>> = teamService.getFixturesByRange(dateStart, dateEnd)


    override suspend fun getLeagueFixture(
        leagueId: Int,
        season:Int,
        teamId: Int
    ): Response<List<FixtureResponse>> = teamService.getLeagueFixture(leagueId, season, teamId)

    // Versus Fixture
    override suspend fun getFixtureVersus(
        teamOneId: Int,
        teamTwoId: Int,
        leagueId: Int,
        season: Int
    ): Response<List<FixtureResponse>> = teamService.getFixtureVersus(teamOneId, teamTwoId, leagueId, season)


    // standings
    override suspend fun getLeagueStandings(
        leagueId: Int,
        season: Int
    ): Response<List<StandingResponse>> = teamService.getLeagueStandings(leagueId, season)


    // prodes - prediction
    override suspend fun createFixturePrediction(request: FixturePredictionRequest): Response<FixturePredictionResponse> = teamService.createFixturePrediction(request)
    override suspend fun getUserFixturePredictions(
        leagueId: Int,
        season: Int
    ): Response<List<FixturePredictionResponse>> = teamService.getUserFixturePredictions(leagueId, season)

    override suspend fun getPredictionRanking(
        top: Int
    ): Response<List<UserPredictionRanking>> = teamService.getPredictionRanking(top)

    override suspend fun getUserPredictionSummary(
        userId: Int,
        season: Int
    ): Response<UserPredictionSummaryResponse> = teamService.getUserPredictionSummary(userId, season)

     */

    //clientes
    override suspend fun getClientsByProvider(
        providerId: Int,
        fullName: String,
        email: String
    ): Response<List<ClientResponse>> = reservasService.getClientsByProvider(providerId, fullName, email)

    override suspend fun getClientById(
        clientId: Int
    ): Response<ClientResponse> = reservasService.getClientById(clientId)


    override suspend fun createClient(
        request: ClientCreateRequest
    ): Response<ClientResponse> = reservasService.createClient(request)



    override suspend fun updateClient(
        clientId: Int,
        request: ClientUpdateRequest
    ): Response<ClientResponse> = reservasService.updateClient(clientId = clientId, request=request)

    override suspend fun deleteClient(
        clientId: Int
    ): Response<DefaultResponse> = reservasService.deleteClient(clientId)


    // servicios

    override suspend fun getServicesByProvider(
        providerId: Int,
        name: String
    ): Response<List<ServiceResponse>> = reservasService.getServicesByProvider(providerId, name)

    override suspend fun getStaffTotales(
    ): Response<List<StaffResponse>> = reservasService.getStaffsTotales()

     // services
    override suspend fun createService(
         request: ServiceCreateRequest
    ): Response<ServiceResponse> = reservasService.createService(request)

    override suspend fun updateService(
        serviceId: Int,
        request: ServiceUpdateRequest
    ): Response<ServiceResponse> = reservasService.updateService(serviceId, request)

    override suspend fun getServiceById(
        serviceId: Int
    ): Response<ServiceResponse> = reservasService.getServiceById(serviceId)


    // reservas

    override suspend fun getReservations(
    ): Response<List<ReservationResponse>> = reservasService.getReservations()

    override suspend fun getReservationById(
        reservationId: Int
    ): Response<ReservationResponse> = reservasService.getReservationById(reservationId)

    override suspend fun createReservation(
        request: ReservationCreateRequest
    ): Response<ReservationResponse> = reservasService.createReservation(request)

    override suspend fun updateReservation(
        reservationId: Int,
        request: ReservationUpdateRequest
    ): Response<ReservationResponse> = reservasService.updateReservation(
        reservationId = reservationId,
        request = request
    )

}