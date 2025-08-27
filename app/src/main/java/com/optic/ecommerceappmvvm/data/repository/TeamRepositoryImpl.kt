package com.optic.ecommerceappmvvm.data.repository



import com.optic.ecommerceappmvvm.data.dataSource.remote.TeamRemoteDataSource
import com.optic.ecommerceappmvvm.domain.model.League.League
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
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.domain.util.ResponseToRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TeamRepositoryImpl(
    private val teamRemoteDataSource: TeamRemoteDataSource
): TeamRepository{
    override suspend fun getAll(): Flow<Resource<List<Team>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getAll()
            )
        )
    }

    override suspend fun getTeamById(teamId: Int): Flow<Resource<TeamResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getTeamById(teamId)
            )
        )
    }


    //PLAYERS
    override suspend fun getPlayers(): Flow<Resource<List<Player>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getPlayers()
            )
        )
    }

    override suspend fun getPlayerStats(playerId: Int): Flow<Resource<PlayerWithStats>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getPlayerStats(playerId)
            )
        )
    }

    override suspend fun getPlayerTeams(playerId: Int): Flow<Resource<PlayerTeamsResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getPlayerTeams(playerId)
            )
        )
    }

    override suspend fun getPlayerLastTeam(playerId: Int): Flow<Resource<PlayerLastTeamResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getPlayerLastTeam(playerId)
            )
        )
    }



    //LIGAS
    override suspend fun getLeagues(
        name: String,
        type: String,
        countryName: String
    ): Flow<Resource<List<League>>>  = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getLeagues(name, type, countryName)
            )
        )
    }

    override suspend fun getLeagueById(leagueId: Int): Flow<Resource<League>>  = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getLeagueById(leagueId)
            )
        )
    }

    override suspend fun getFollowedPlayers(): Flow<Resource<List<Player>>>  = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFollowedPlayers()
            )
        )
    }

    override suspend fun createFollowedPlayer(playerId: Int):  Flow<Resource<FollowedPlayerResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.createFollowedPlayer(playerId)
            )
        )
    }

    override suspend fun deleteFollowedPlayer(playerId: Int): Flow<Resource<DefaultResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.deleteFollowedPlayer(playerId)
            )
        )
    }


    // FOllowed Teams
    override suspend fun getFollowedTeams(): Flow<Resource<List<Team>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFollowedTeams()
            )
        )
    }

    override suspend fun createFollowedTeam(teamId: Int): Flow<Resource<FollowedTeamResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.createFollowedTeam(teamId)
            )
        )
    }

    override suspend fun deleteFollowedTeam(teamId: Int): Flow<Resource<DefaultResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.deleteFollowedTeam(teamId)
            )
        )
    }

    // ligas seguidas
    override suspend fun getFollowedLeagues(): Flow<Resource<List<League>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFollowedLeagues()
            )
        )
    }
    override suspend fun createFollowedLeague(leagueId: Int): Flow<Resource<FollowedLeagueResponse>>  = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.createFollowedLeague(leagueId)
            )
        )
    }

    override suspend fun deleteFollowedLeague(leagueId: Int): Flow<Resource<DefaultResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.deleteFollowedLeague(leagueId)
            )
        )
    }

    //FIXTURES
    override suspend fun getFixtureById(id: Int): Flow<Resource<FixtureResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixtureById(id)
            )
        )
    }

    // FIXTURES

    override suspend fun getFixtureFollowedTeams(
        season: Int,
        date: String
    ): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixtureFollowedTeams(season, date)
            )
        )
    }

    // Versus FIxture
    override suspend fun getFixtureVersus(
        teamOneId: Int,
        teamTwoId: Int,
        leagueId: Int,
        season: Int
    ): Flow<Resource<List<FixtureResponse>>>  = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixtureVersus(teamOneId, teamTwoId, leagueId, season)
            )
        )
    }

    override suspend fun getFixtureTeam(teamId: Int): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixtureTeam(teamId)
            )
        )
    }

    override suspend fun getNextFixtureTeam(teamId: Int): Flow<Resource<FixtureResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getNextFixtureTeam(teamId)
            )
        )
    }


    override suspend fun getTopFiveFixtureTeam(teamId: Int): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getTopFiveFixtureTeam(teamId)
            )
        )
    }

    override suspend fun getLeagueStandings(
        leagueId: Int,
        season: Int
    ): Flow<Resource<List<StandingResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getLeagueStandings(leagueId, season)
            )
        )
    }
}