package com.optic.ecommerceappmvvm.data.dataSource.remote

import com.optic.ecommerceappmvvm.data.dataSource.remote.service.TeamService
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
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamStatsResponse
import retrofit2.Response

class TeamRemoteDataSourceImpl (private val teamService: TeamService): TeamRemoteDataSource {
    override suspend fun getAll(): Response<List<Team>> = teamService.getTeams()
    override suspend fun getTeamById(teamId: Int): Response<TeamResponse> = teamService.getTeamById(teamId)



    // PLAYERS
    override suspend fun getPlayers(): Response<List<Player>> = teamService.getPlayers()
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
    override suspend fun getCountryFixtures(
        season: Int,
        date: String
    ): Response<List<FixtureResponse>> = teamService.getContryFixtures(season, date)

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


    override suspend fun getLeagueFixture(
        leagueId: Int,
        season:Int
    ): Response<List<FixtureResponse>> = teamService.getLeagueFixture(leagueId, season)

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

}