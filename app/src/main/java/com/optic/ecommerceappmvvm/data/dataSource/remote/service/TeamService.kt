package com.optic.ecommerceappmvvm.data.dataSource.remote.service

import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.lineups.FixtureLineupsResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.stats.FixtureStatsResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.PlayerComplete
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionResponse
import com.optic.ecommerceappmvvm.domain.model.prode.UserPredictionRanking
import com.optic.ecommerceappmvvm.domain.model.prode.UserPredictionSummaryResponse
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamStatsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TeamService {
/* Se utiliza Response de retrofit, en los archivos de servicios */
    @GET("football/teams")
    suspend fun getTeams(
        @Query("name") name: String,
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("page_size") size: Int
    ): Response<List<Team>>

    @GET("football/teams/{team_id}")
    suspend fun getTeamById(
        @Path("team_id") teamId: Int
    ): Response<TeamResponse>

    // equipos sugeridos

    @GET("football/teams/suggested/{limit}")
    suspend fun getSuggestedTeams(
        @Path("limit") limit: Int
    ): Response<List<Team>>

    // players

    @GET("football/get/player/{player_id}")
    suspend fun getPlayerPorId(
        @Path("player_id") playerId: Int,
    ): Response<PlayerComplete>

    @GET("football/players/name/{name}/page/{page}/size/{size}")
    suspend fun getPlayers(
        @Path("name") name: String?,
        @Path("page") page: Int,
        @Path("size") size: Int
    ): Response<List<Player>>

    @GET("football/players/all")
    suspend fun getallPlayers(
    ): Response<List<Player>>

    /*  Recupera la informacion de un Player junto con todas sus estatisticas
    * ligas, equipos, goles etc..   */
    @GET("football/players/stats/{player_id}")
    suspend fun getPlayerStats(
        @Path("player_id") playerId: Int
    ): Response<PlayerWithStats>

     // Trayectoria de un jugador last team
    @GET("football/players/team/{player_id}")
    suspend fun getPlayerLastTeam(
        @Path("player_id") playerId: Int
    ): Response<PlayerLastTeamResponse>

    // Trayectoria de un jugador,
    @GET("football/players/teams/{player_id}")
    suspend fun getPlayerTeams(
        @Path("player_id") playerId: Int
    ): Response<PlayerTeamsResponse>


   // get ligas
   @GET("football/leagues")
   suspend fun getLeagues(
       @Query("name") name: String,
       @Query("type_") type: String,
       @Query("country_name") countryName: String
   ): Response<List<League>>

    // get ligas
    @GET("football/leagues/participate/user/{user_id}")
    suspend fun getProdeParticipateLeagues(
        @Path("user_id") userId: Int
    ): Response<List<League>>


    @GET("football/leagues/top")
    suspend fun getTopLeagues(
    ): Response<List<League>>

    @GET("football/leagues/{league_id}")
    suspend fun getLeagueById(
        @Path("league_id") leagueId: Int
    ): Response<LeagueCompleteResponse>



// SEGUIDORES
    @POST("football/createFollowedPlayer")
    suspend fun createFollowedPlayer(
    @Body request: FollowedPlayerRequest
    ): Response<FollowedPlayerResponse>

    @GET("football/getFollowedPlayers")
    suspend fun getFollowedPlayers(
    ): Response<List<Player>>

    @DELETE("football/deleteFollowed/{player_id}")
    suspend fun deleteFollowedPlayer(
        @Path("player_id") playerId: Int
    ): Response<DefaultResponse>


    // TEAMS SEGUIDOS

    @POST("football/createFollowedTeam")
    suspend fun createFollowedTeam(
        @Body request: FollowedTeamRequest
    ): Response<FollowedTeamResponse>

    @GET("football/getFollowedTeams")
    suspend fun getFollowedTeams(
    ): Response<List<Team>>

    @DELETE("football/deleteFollowedTeam/{team_id}")
    suspend fun deleteFollowedTeam(
        @Path("team_id") teamId: Int
    ): Response<DefaultResponse>

    // VERSUS FIXTURE

    @GET("football/versusTeamFixture")
    suspend fun getFixtureVersus(
        @Query("team_one_id") teamOneId: Int,
        @Query("team_two_id") teamTwoId: Int,
        @Query("league_id") leagueId: Int,
        @Query("season") season: Int
    ): Response<List<FixtureResponse>>

    // FIXTURE POR PAIS ( MEDIANTE LA IP DE DONDE SE CONECTAN)
    @GET("football/fixtures/country")
    suspend fun getContryFixtures(
        @Query("season") season: Int,
        @Query("date") date: String
    ): Response<List<FixtureResponse>>

    // FIXTURE , RECUPERAR POR ID
    @GET("football/fixtures/{id}")
    suspend fun getFixtureById(
        @Path("id") id: Int
    ): Response<FixtureResponse>

    // FIXTURE LINEUPS POR ID
    @GET("football/fixtures/{id}/lineups")
    suspend fun getFixtureLineups(
        @Path("id") id: Int
    ): Response<FixtureLineupsResponse>

    // FIXTURE stats POR ID
    @GET("football/fixtures/{id}/stats")
    suspend fun getFixtureStats(
        @Path("id") id: Int
    ): Response<FixtureStatsResponse>

    // MATCHES ( FIXTURES ) POR TEAMS SEGUUIDOS

    @GET("football/getFixtureFollowedTeams")
    suspend fun getFixtureFollowedTeams(
        @Query("season") season: Int,
        @Query("date") date: String
    ): Response<List<FixtureResponse>>


    // MATCHES ( FIXTURES ) de equipos no seguidos


    @GET("football/fixtures/nofollow")
    suspend fun getNoFollowFixtures(
        @Query("season") season: Int,
        @Query("date") date: String
    ): Response<List<FixtureResponse>>
    //Fixture de un equipo en general
    @GET("football/fixtures/team/{team_id}")
    suspend fun getFixtureTeam(
        @Path("team_id") teamId: Int
    ): Response<List<FixtureResponse>>

    //Fixture de proximo partido de un EQuipo
    @GET("football/fixtures/team/{team_id}/next")
    suspend fun getNextFixtureTeam(
        @Path("team_id") teamId: Int
    ): Response<FixtureResponse>

    //Fixture top 5 ultimos partidos finalizados
    @GET("football/fixtures/team/{team_id}/top5")
    suspend fun getTopFiveFixtureTeam(
        @Path("team_id") teamId: Int
    ): Response<List<FixtureResponse>>

    //Fixture de una Liga
    @GET("football/fixtures/league/{league_id}/season/{season}/team/{team_id}")
    suspend fun getLeagueFixture(
        @Path("league_id") leagueId: Int,
        @Path("season") season: Int,
        @Path("team_id") teamId: Int
    ): Response<List<FixtureResponse>>

    // fixture por fecha
    @GET("football/fixtures/date/{date}/limit/{limit}")
    suspend fun getFixturesByDate(
        @Path("date") date: String,
        @Path("limit") limit: Int
    ): Response<List<FixtureResponse>>


    // fixture por rango de fechas
    @GET("football/fixtures/start/{date_start}/end/{date_end}")
    suspend fun getFixturesByRange(
        @Path("date_start") dateStart: String,
        @Path("date_end") dateEnd: String,
    ): Response<List<FixtureResponse>>

    // fixrure por round de liga
    @GET("football/fixtures/round")
    suspend fun getFixturesByRound(
        @Query("league_id") leagueId: Int,
        @Query("season") season: Int,
        @Query("round") round: String
    ): Response<List<FixtureResponse>>




    // PARA SEGUIR LIGAS

    @POST("football/createFollowedLeague")
    suspend fun createFollowedLeague(
        @Body request: FollowedLeagueRequest
    ): Response<FollowedLeagueResponse>

    @GET("football/getFollowedLeagues")
    suspend fun getFollowedLeagues(
    ): Response<List<League>>

    @DELETE("football/deleteFollowedLeague/{league_id}")
    suspend fun deleteFollowedLeague(
        @Path("league_id") leagueId: Int
    ): Response<DefaultResponse>


    // STANDINGS ( CLASIFICACIONES) POR LEAGUE Y SEASON
    @GET("football/leagues/{leagueId}/standings/{season}")
    suspend fun getLeagueStandings(
        @Path("leagueId") leagueId: Int,
        @Path("season") season: Int
    ): Response<List<StandingResponse>>


    //teams stats

    @GET("football/team/stats")
    suspend fun getTeamStats(
        @Query("season") season: Int,
        @Query("team_id") teamId: Int,
        @Query("date") date: String? =null
    ): Response<TeamStatsResponse>

    // prodes
    @POST("football/prode/create")
    suspend fun createFixturePrediction(
        @Body request: FixturePredictionRequest
    ): Response<FixturePredictionResponse>


    @GET("football/prode/fixturepredict/list")
    suspend fun getUserFixturePredictions(
        @Query("league_id") leagueId: Int,
        @Query("season") season: Int
    ): Response<List<FixturePredictionResponse>>


    // ranking de predicciones
    @GET("football/prode/ranking/{top}")
    suspend fun getPredictionRanking(
        @Path("top") top: Int
    ): Response<List<UserPredictionRanking>>

    // user predictions summary
    @GET("football/prode/summary/user/{user_id}/season/{season}")
    suspend fun getUserPredictionSummary(
        @Path("user_id") userId: Int,
        @Path("season") season: Int
    ): Response<UserPredictionSummaryResponse>



}
