package com.optic.ecommerceappmvvm.data.dataSource.remote

import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
import retrofit2.Response

interface TeamRemoteDataSource {
  /* tambien en este archivo de datasource, se responde con Response de retrofit,
   esta aclarcion es util, ya que en las implementaciones se utiliza FLow
   */
    // Teams
    suspend fun getAll(): Response<List<Team>>
    suspend fun getTeamById(teamId: Int): Response<TeamResponse>

    //Players
    suspend fun getPlayers(): Response<List<Player>>
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

    // FIXTURES
  //Recuperar Fixture por Id
    suspend fun getFixtureById(id: Int): Response<FixtureResponse>
   //fixtures de teans seguidos
    suspend fun getFixtureFollowedTeams(season: Int, date: String): Response<List<FixtureResponse>>
    //Teams
    suspend fun getFixtureTeam(teamId: Int): Response<List<FixtureResponse>>
    suspend fun getNextFixtureTeam(teamId: Int): Response<FixtureResponse>
    suspend fun getTopFiveFixtureTeam(teamId: Int): Response<List<FixtureResponse>>

    // Leagues
    suspend fun getLeagueFixture(leagueId: Int): Response<List<FixtureResponse>>
    // versus
    suspend fun getFixtureVersus(
        teamOneId: Int,
        teamTwoId: Int,
        leagueId: Int,
        season: Int
        ): Response<List<FixtureResponse>>

  // standings
    suspend fun getLeagueStandings(leagueId: Int, season: Int): Response<List<StandingResponse>>
}