package com.optic.ecommerceappmvvm.data.repository



import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixtureDao
import com.optic.ecommerceappmvvm.data.dataSource.local.mapper.toDomain
import com.optic.ecommerceappmvvm.data.dataSource.local.mapper.toEntity
import com.optic.ecommerceappmvvm.data.dataSource.remote.TeamRemoteDataSource
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
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamStatsResponse
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.domain.util.ResponseToRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.ZoneId

class TeamRepositoryImpl(
    private val teamRemoteDataSource: TeamRemoteDataSource,
    private val fixtureDao: FixtureDao
): TeamRepository
{
    override suspend fun getAll(): Flow<Resource<List<Team>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getAll()
            )
        )
    }

    override suspend fun getSuggestedTeams(
        limit: Int
    ): Flow<Resource<List<Team>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getSuggestedTeams(limit)
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

    override suspend fun getLeagueById(leagueId: Int): Flow<Resource<LeagueCompleteResponse>>  = flow{
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

    override suspend fun getTeamStats(
        season: Int,
        teamId: Int,
        date: String?
    ): Flow<Resource<TeamStatsResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getTeamStats(season, teamId, date)
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

    override suspend fun getCountryFixtures(season: Int, date: String): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getCountryFixtures(season, date)
            )
        )
    }
    override suspend fun getFixtureById(id: Int): Flow<Resource<FixtureResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixtureById(id)
            )
        )
    }

    override suspend fun getFixtureLineups(id: Int): Flow<Resource<FixtureLineupsResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixtureLineups(id)
            )
        )
    }

    override suspend fun getFixtureStats(id: Int): Flow<Resource<FixtureStatsResponse>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixtureStats(id)
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

    override suspend fun getNoFollowFixtures(
        season: Int,
        date: String
    ): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getNoFollowFixtures(season, date)
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

    override suspend fun getLeagueFixture(
        leagueId: Int,
        season:Int,
        teamId: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getLeagueFixture(leagueId, season, teamId)
            )
        )
    }

    override suspend fun getFixturesByDate(
        date: String,
        limit: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow {

        emit(Resource.Loading)
        Log.d("getFixturesByDate", "1️⃣ Loading emitido")

        val localDate = LocalDate.parse(date)
        val zone = ZoneId.systemDefault()

        val startTs = localDate.atStartOfDay(zone).toEpochSecond()
        val endTs = localDate.plusDays(1).atStartOfDay(zone).toEpochSecond()

        Log.d("getFixturesByDate", "2️⃣ Consultando Room con range: $startTs - $endTs")

        val cached = runCatching { fixtureDao.getFixturesByDateRange(startTs, endTs) }
            .onFailure { Log.e("getFixturesByDate", "Error leyendo Room", it) }
            .getOrDefault(emptyList())

        Log.d("getFixturesByDate", "3️⃣ Cache size = ${cached.size}")

        if (cached.isNotEmpty()) {

            Log.d("getFixturesByDate", "4️⃣ Emitiendo cache inmediatamente (${cached.size})")
            emit(Resource.Success(cached.map { it.toDomain() }))
            Log.d("getFixturesByDate", "🟩 DESPUÉS DE emit() cache")

            // ⛔ NO LLAMAR API SI HAY CACHE
            return@flow

        } else {
            Log.d("getFixturesByDate", "4️⃣ No hay cache → mostrando Loading")
            emit(Resource.Loading)

            // 🔥 AQUI SÍ SE LLAMA API
            try {
                Log.d("getFixturesByDate", "5️⃣ Llamando backend…")

                val result = teamRemoteDataSource.getFixturesByDate(date, limit)
                val response = ResponseToRequest.send(result)

                if (response is Resource.Success) {
                    val fixtures = response.data!!

                    fixtureDao.insertFixtures(fixtures.map { it.toEntity() })
                    Log.d("getFixturesByDate", "6️⃣ Guardado en Room (API)")

                    emit(Resource.Success(fixtures))
                } else {
                    Log.d("getFixturesByDate", "6️⃣ API devolvió error")
                    emit(response)
                }

            } catch (e: Exception) {
                Log.e("getFixturesByDate", "❌ Excepción backend", e)
                emit(Resource.Failure("Error: ${e.localizedMessage ?: e.message}"))
            }
        }

    }.flowOn(kotlinx.coroutines.Dispatchers.IO)


    override suspend fun getFixturesByRange(
        dateStart: String,
        dateEnd: String
    ): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixturesByRange(dateStart, dateEnd)
            )
        )
    }


    // NUEVA FUNCIÓN de precache
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun precacheFixturesAroundToday() {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()

        val start = today.minusDays(30).toString() // yyyy-MM-dd
        val end = today.plusDays(30).toString()

        try {
            Log.d("getFixturesByDate", "⏳ Consultando fixtures desde $start hasta $end...")

            val response = ResponseToRequest.send(
                teamRemoteDataSource.getFixturesByRange(start, end)
            )

            if (response is Resource.Success) {

                val fixtures = response.data!!
                Log.d("getFixturesByDate", "📥 API devolvió: ${fixtures.size} fixtures")

                // Guardar TODO en Room
                fixtureDao.insertFixtures(fixtures.map { it.toEntity() })

                // Obtener cuántos fixtures totales quedaron guardados
                val startTs = LocalDate.parse(start).atStartOfDay(zone).toEpochSecond()
                val endTs = LocalDate.parse(end).plusDays(1).atStartOfDay(zone).toEpochSecond()

                val cachedCount = fixtureDao.getFixturesByDateRange(startTs, endTs).size

                Log.d(
                    "getFixturesByDate",
                    "✔ Precarga completada. Guardados en cache: $cachedCount fixtures"
                )

            } else {
                Log.d("getFixturesByDate", "❌ Error al obtener fixtures por rango: $response")
            }

        } catch (e: Exception) {
            Log.e("getFixturesByDate", "❌ Excepción precache por rango", e)
        }
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