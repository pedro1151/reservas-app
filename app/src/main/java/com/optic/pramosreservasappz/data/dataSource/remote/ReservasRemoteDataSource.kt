package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.domain.model.Team
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import retrofit2.Response

interface ReservasRemoteDataSource {
  /* tambien en este archivo de datasource, se responde con Response de retrofit,
   esta aclarcion es util, ya que en las implementaciones se utiliza FLow
   */
    // Teams
    suspend fun getClientsByProvider(providerId: Int): Response<List<ClientResponse>>
    suspend fun getServicesByProvider(providerId: Int, name:String): Response<List<ServiceResponse>>
    /*
    suspend fun getSuggestedTeams(limit: Int): Response<List<Team>>
    suspend fun getTeamById(teamId: Int): Response<TeamResponse>

    //Players
    suspend fun getPlayers(name: String?, page:Int, size:Int): Response<List<Player>>

    suspend fun getallPlayers(): Response<List<Player>>
    suspend fun getPlayerPorId(playerId: Int): Response<PlayerComplete>
    suspend fun getPlayerStats(playerId: Int): Response<PlayerWithStats>
    suspend fun getPlayerTeams(playerId: Int): Response<PlayerTeamsResponse>
    suspend fun getPlayerLastTeam(playerId: Int): Response<PlayerLastTeamResponse>

    //PLAYER SEGUIDOS
    suspend fun createFollowedPlayer(playerId: Int): Response<FollowedPlayerResponse>
    suspend fun getFollowedPlayers(): Response<List<Player>>
    suspend fun deleteFollowedPlayer(playerId: Int): Response<DefaultResponse>


  //TEAM SEGUIDOS
    suspend fun createFollowedTeam(teamId: Int): Response<FollowedTeamResponse>
    suspend fun getFollowedTeams(): Response<List<Team>>
    suspend fun deleteFollowedTeam(teamId: Int): Response<DefaultResponse>


  //LIGAS
    suspend fun createFollowedLeague(leagueId: Int): Response<FollowedLeagueResponse>
    suspend fun getFollowedLeagues(): Response<List<League>>
    suspend fun deleteFollowedLeague(leagueId: Int): Response<DefaultResponse>
    suspend fun getLeagues(name: String, type: String, countryName: String): Response<List<League>>
    suspend fun getLeagueById(leagueId: Int): Response<LeagueCompleteResponse>
    suspend fun getTopLeagues(): Response<List<League>>
    suspend fun getProdeParticipateLeagues(userId:Int): Response<List<League>>

    // FIXTURES
  //Recuperar Fixture por Id
    suspend fun getCountryFixtures(season: Int, date: String): Response<List<FixtureResponse>>
    suspend fun getFixturesByRound(leagueId: Int, season: Int, round: String): Response<List<FixtureResponse>>
    suspend fun getFixtureById(id: Int): Response<FixtureResponse>
    suspend fun getFixtureLineups(id: Int): Response<FixtureLineupsResponse>


    // Stats
    suspend fun getFixtureStats(id: Int): Response<FixtureStatsResponse>
    suspend fun getTeamStats(season: Int, teamId: Int, date: String?=null): Response<TeamStatsResponse>


   //fixtures de teans seguidos
    suspend fun getFixtureFollowedTeams(season: Int, date: String): Response<List<FixtureResponse>>
    suspend fun getNoFollowFixtures(season: Int, date: String): Response<List<FixtureResponse>>

    //Teams
    suspend fun getFixtureTeam(teamId: Int): Response<List<FixtureResponse>>
    suspend fun getNextFixtureTeam(teamId: Int): Response<FixtureResponse>
    suspend fun getTopFiveFixtureTeam(teamId: Int): Response<List<FixtureResponse>>


    // por date
    suspend fun getFixturesByDate(date: String, limit: Int): Response<List<FixtureResponse>>
    suspend fun getFixturesByRange(dateStart: String, dateEnd: String): Response<List<FixtureResponse>>

    // Leagues
    suspend fun getLeagueFixture(
        leagueId: Int,
        season: Int,
        teamId: Int
    ): Response<List<FixtureResponse>>

    // versus
    suspend fun getFixtureVersus(
        teamOneId: Int,
        teamTwoId: Int,
        leagueId: Int,
        season: Int
        ): Response<List<FixtureResponse>>

  // standings
    suspend fun getLeagueStandings(leagueId: Int, season: Int): Response<List<StandingResponse>>


    //prodes
    suspend fun createFixturePrediction(request: FixturePredictionRequest): Response<FixturePredictionResponse>
    suspend fun getUserFixturePredictions(leagueId:Int, season:Int): Response<List<FixturePredictionResponse>>
    suspend fun getPredictionRanking(top:Int): Response<List<UserPredictionRanking>>
    suspend fun getUserPredictionSummary(
        userId: Int,
        season:Int
    ): Response<UserPredictionSummaryResponse>

     */





}