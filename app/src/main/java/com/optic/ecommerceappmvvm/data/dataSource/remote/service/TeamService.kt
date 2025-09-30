package com.optic.ecommerceappmvvm.data.dataSource.remote.service

import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
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
    ): Response<List<Team>>

    @GET("football/teams/{team_id}")
    suspend fun getTeamById(
        @Path("team_id") teamId: Int
    ): Response<TeamResponse>

    // players

    @GET("football/getPlayers")
    suspend fun getPlayers(
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
    ): Response<List<FixtureResponse>>

    // FIXTURE , RECUPERAR POR ID
    @GET("football/fixtures/{id}")
    suspend fun getFixtureById(
        @Path("id") id: Int
    ): Response<FixtureResponse>

    // MATCHES ( FIXTURES ) POR TEAMS SEGUUIDOS

    @GET("football/getFixtureFollowedTeams")
    suspend fun getFixtureFollowedTeams(
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
    @GET("football/fixtures/league/{league_id}")
    suspend fun getLeagueFixture(
        @Path("league_id") leagueId: Int
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


}
