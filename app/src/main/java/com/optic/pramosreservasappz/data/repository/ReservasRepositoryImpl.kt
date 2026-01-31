package com.optic.pramosreservasappz.data.repository



import android.util.Log
import com.optic.pramosreservasappz.data.dataSource.local.dao.FixtureDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.FixturePredictionDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.LeagueDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.PlayerDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.TeamDao
import com.optic.pramosreservasappz.data.dataSource.local.mapper.toDomain
import com.optic.pramosreservasappz.data.dataSource.local.mapper.toEntity
import com.optic.pramosreservasappz.data.dataSource.remote.ReservasRemoteDataSource
import com.optic.pramosreservasappz.domain.model.Team
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.staff.StaffResponse

import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.domain.util.ResponseToRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ReservasRepositoryImpl(
    private val reservasRemoteDataSource: ReservasRemoteDataSource,
    private val fixtureDao: FixtureDao,
    private val leagueDao: LeagueDao,
    private val playerDao: PlayerDao,
    private val teamDao: TeamDao,
    private val fixturePredictionDao: FixturePredictionDao
): ReservasRepository
{
    /*
    override suspend fun getAll(): Flow<Resource<List<Team>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getAll()
            )
        )
    }
   */
    /*
    override suspend fun getAll(
            name: String,
            country:String,
            page: Int,
            size: Int
        ): Flow<Resource<List<Team>>> = flow {

        val cached = runCatching { teamDao.getTeams() }.getOrDefault(emptyList())
        val start = (page - 1) * size
        val end = start + size

    // 1️⃣ Emitir lo que tengas en cache (si existe)
        if (cached.isNotEmpty() && start < cached.size) {
            val cachedEnd = minOf(end, cached.size)
            emit(Resource.Success(cached.subList(start, cachedEnd).map { it.toDomain() }))
            Log.d("precacheTeams", "5️⃣ Llamando desde cache con start $start y end $cachedEnd")
        }

    // 2️⃣ Solo llamar API si cache no cubre toda la página
        if (cached.size < end) {
            try {
                Log.d("precacheTeams", "5️⃣ Llamando backend…")
                val result = teamRemoteDataSource.getAll(name, country,page, size)
                val response = ResponseToRequest.send(result)

                if (response is Resource.Success) {
                    val teams = response.data!!
                    teamDao.insertTeams(teams.map { it.toEntity() })
                    emit(Resource.Success(teams))
                    Log.d("precacheTeams", "6️⃣ Guardado de teams en Room (API)")
                } else {
                    emit(response)
                }
            } catch (e: Exception) {
                emit(Resource.Failure("Error al obtener los teams: ${e.localizedMessage ?: e.message}"))
            }
        }

    }.flowOn(Dispatchers.IO)



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
    override suspend fun getPlayers(
        name: String?,
        page: Int,
        size: Int
    ): Flow<Resource<List<Player>>> = flow {

        val cached = runCatching { playerDao.getPlayers() }.getOrDefault(emptyList())
        val start = (page - 1) * size
        val end = start + size

// 1️⃣ Emitir lo que tengas en cache (si existe)
        if (cached.isNotEmpty() && start < cached.size) {
            val cachedEnd = minOf(end, cached.size)
            emit(Resource.Success(cached.subList(start, cachedEnd).map { it.toDomain() }))
            Log.d("precachePlayers", "5️⃣ Llamando desde cache con start $start y end $cachedEnd")
        }

// 2️⃣ Solo llamar API si cache no cubre toda la página
        if (cached.size < end) {
            try {
                Log.d("precachePlayers", "5️⃣ Llamando backend…")
                val result = teamRemoteDataSource.getPlayers(name, page, size)
                val response = ResponseToRequest.send(result)

                if (response is Resource.Success) {
                    val players = response.data!!
                    playerDao.insertPlayers(players.map { it.toEntity() })
                    emit(Resource.Success(players))
                    Log.d("precachePlayers", "6️⃣ Guardado en Room (API)")
                } else {
                    emit(response)
                }
            } catch (e: Exception) {
                emit(Resource.Failure("Error al obtener players: ${e.localizedMessage ?: e.message}"))
            }
        }

    }.flowOn(Dispatchers.IO)




    override suspend fun getallPlayers(): Flow<Resource<List<Player>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getallPlayers()
            )
        )
    }

    override suspend fun getPlayerPorId(
        playerId: Int
    ): Flow<Resource<PlayerComplete>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getPlayerPorId(playerId)
            )
        )
    }


    override suspend fun getPlayerStats(
        playerId: Int
    ): Flow<Resource<PlayerWithStats>> = flow {

        emit(Resource.Loading)

        // 1️⃣ Cache
        val cached = runCatching {
            playerDao.getPlayerStats(playerId, 2025)
        }.getOrDefault(emptyList())

        if (cached.isNotEmpty()) {
            Log.d("precachePlayerStats", "✅ stats desde Room")
            emit(Resource.Success(cached.toDomain()))
            return@flow
        }

        // 2️⃣ API
        try {
            Log.d("precachePlayerStats", "🌐 Llamando API")

            val result = teamRemoteDataSource.getPlayerStats(playerId)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {
                val playerStat = response.data!!

                playerDao.insertPlayerStats(playerStat.toEntities())
                Log.d("precachePlayerStats", "💾 Guardado en Room")

                emit(Resource.Success(playerStat))
            } else {
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("precachePlayerStats", "❌ Error", e)
            emit(
                Resource.Failure(
                    "Error al obtener stats: ${e.localizedMessage ?: e.message}"
                )
            )
        }

    }.flowOn(Dispatchers.IO)


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
    ): Flow<Resource<List<League>>> = flow {

        //emit(Resource.Loading)

        // 1️⃣ Leer cache local
        val cached = runCatching {
            leagueDao.getLeagues(name, type, countryName)
        }.getOrDefault(emptyList())
        Log.d("getLeagues", "3️⃣ Cache size = ${cached.size}")

        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached.map { it.toDomain() }))
            return@flow //  ⛔ No llamar API
        }

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("getLeagues", "5️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getLeagues(name, type, countryName)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {

                val leagues = response.data!!
                Log.d(
                    "getLeagues",
                    "6️⃣ API devolvió ${leagues.size} leagues"
                )

                // guardo en cache
                leagueDao.insertLeagues(leagues.map { it.toEntity() })
                Log.d("getLeagues", "6️⃣ Guardado en Room (API)")
                emit(Resource.Success(leagues))

            } else {
                Log.d("getLeagues","6️⃣ API devolvió error → $response")
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("ggetLeagues", "❌ Excepción backend", e)
            emit(Resource.Failure("Error al obtener leagies: ${e.localizedMessage ?: e.message}"))
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun getLeagueById(leagueId: Int): Flow<Resource<LeagueCompleteResponse>>  = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getLeagueById(leagueId)
            )
        )
    }

    override suspend fun getTopLeagues(): Flow<Resource<List<League>>> = flow{
        //emit(Resource.Loading)

        // 1️⃣ Leer cache local
        val cached = runCatching {
            leagueDao.getTopLeagues()
        }.getOrDefault(emptyList())
        Log.d("precacheTopLigas", " topligas cache size = ${cached.size}")

        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached.map { it.toDomain() }))
            return@flow //  ⛔ No llamar API
        }

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("precache", "5️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getTopLeagues()
            val response = ResponseToRequest.send(result)
            emit(response)

        } catch (e: Exception) {
            Log.e("precache", "❌ Excepción backend", e)
            emit(Resource.Failure("Error al obtener el top ligas desde api: ${e.localizedMessage ?: e.message}"))
        }

    }.flowOn(Dispatchers.IO)


    override fun getFollowedPlayers(): Flow<Resource<List<Player>>>  = flow{
        emit(Resource.Loading)

        try {
            // Recolectamos el Flow de Room
            playerDao.getFollowedPlayers()
                .collect { cached ->
                    if (cached.isNotEmpty()) {

                        Log.d("precacheFOllowdPlayers", "Emito cache de followed players")
                        // Emitimos el cache
                        emit(Resource.Success(cached.map { it.toDomain() }))
                    } else {
                        Log.d("precacheFOllowdPlayers", "NO HAY CACHE, llamo a la api de followed players")
                        // Cache vacío → fallback API
                        val apiResult = teamRemoteDataSource.getFollowedPlayers()
                        val response = ResponseToRequest.send(apiResult)
                        emit(response)
                    }
                }
        } catch (e: Exception) {
            emit(Resource.Failure(e.localizedMessage ?: "Error al obtener PLAYERS seguidas"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createFollowedPlayer(
        playerId: Int,
        isAuthenticated: Boolean
    ):  Flow<Resource<FollowedPlayerResponse>> = flow{
            Log.d("precacheFOllowdPlayers", "Se guarda cache de la plauer seguido seguida = ${playerId}")
            playerDao.insertFollowedPlayer(FollowedPlayerEntity(playerId))

    }

    override suspend fun deleteFollowedPlayer(
        playerId: Int,
        isAuthenticated: Boolean
    ): Flow<Resource<DefaultResponse>> = flow{
        emit(Resource.Loading)

        try {

                // 🔹 Borrar solo en cache
                playerDao.deleteFollowedPlayer(playerId)
                // Emitimos éxito local
                emit(Resource.Success(DefaultResponse(true, "PLayer seguido eliminado del cache")))

        } catch (e: Exception) {
            emit(Resource.Failure(e.localizedMessage ?: "Error al eliminar el player seguido"))
        }
    }.flowOn(Dispatchers.IO)


    // FOllowed Teams
    override fun getFollowedTeams(): Flow<Resource<List<Team>>> = flow{
        emit(Resource.Loading)

        try {
            // Recolectamos el Flow de Room
            teamDao.getFollowedTeams()
                .collect { cached ->
                    if (cached.isNotEmpty()) {

                        Log.d("precacheTeamsPlayers", "Emito cache de followed teams")
                        // Emitimos el cache
                        emit(Resource.Success(cached.map { it.toDomain() }))
                    } else {
                        Log.d("precacheTeamsPlayers", "NO HAY CACHE, llamo a la api de followed teams")
                        // Cache vacío → fallback API
                        val apiResult = teamRemoteDataSource.getFollowedTeams()
                        val response = ResponseToRequest.send(apiResult)
                        emit(response)
                    }
                }
        } catch (e: Exception) {
            emit(Resource.Failure(e.localizedMessage ?: "Error al obtener TEAMS seguidas"))
        }
    }.flowOn(Dispatchers.IO)



    override suspend fun createFollowedTeam(teamId: Int): Flow<Resource<FollowedTeamResponse>> = flow{
        Log.d("precacheFollowedTeams", "Se guarda cache del team seguido seguida = ${teamId}")
        teamDao.insertFollowedTeam(FollowedTeamEntity(teamId))

    }


    override suspend fun deleteFollowedTeam(teamId: Int): Flow<Resource<DefaultResponse>> = flow{
        emit(Resource.Loading)

        try {

            // 🔹 Borrar solo en cache
            teamDao.deleteFollowedTeam(teamId)
            // Emitimos éxito local
            emit(Resource.Success(DefaultResponse(true, "Team seguido eliminado del cache")))

        } catch (e: Exception) {
            emit(Resource.Failure(e.localizedMessage ?: "Error al eliminar el team seguido"))
        }
    }.flowOn(Dispatchers.IO)

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
    suspend override fun getFollowedLeagues(
    ): Flow<Resource<List<League>>> = flow {
        emit(Resource.Loading)

        try {
            // Recolectamos el Flow de Room
            leagueDao.getFollowedLeaguesFromCache()
                .collect { cached ->
                    if (cached.isNotEmpty()) {
                        // Emitimos el cache
                        emit(Resource.Success(cached.map { it.toDomain() }))
                    } else {
                        // Cache vacío → fallback API
                        val apiResult = teamRemoteDataSource.getFollowedLeagues()
                        val response = ResponseToRequest.send(apiResult)
                        emit(response)
                    }
                }
        } catch (e: Exception) {
            emit(Resource.Failure(e.localizedMessage ?: "Error al obtener ligas seguidas"))
        }
    }.flowOn(Dispatchers.IO)



    override suspend fun createFollowedLeague(
        leagueId: Int,
        isAuthenticated: Boolean
    ): Flow<Resource<FollowedLeagueResponse>>  = flow{
            // guardo en cache
            Log.d("precacheFOllowdLegues", "Se guarda cache de la ligaId seguida = ${leagueId}")
            leagueDao.insertFollowedLeague(FollowedLeagueEntity(leagueId))

    }

    override suspend fun getProdeParticipateLeagues(
        userId: Int
    ): Flow<Resource<List<League>>> = flow {

        emit(Resource.Loading)

        // 1️⃣ Leer cache local
        val cached = runCatching {
            leagueDao.getParticipateProdeLeagues()
        }.getOrDefault(emptyList())
        Log.d("precache-etProdeParticipateLeagues", "3️⃣ Cache size = ${cached.size}")

        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached.map { it.toDomain() }))
            return@flow //  ⛔ No llamar API
        }

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("precache-etProdeParticipateLeagues","️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getProdeParticipateLeagues(userId)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {

                val leagues = response.data!!
                Log.d(
                    "precache-etProdeParticipateLeagues",
                    "6️⃣ API devolvió ${leagues.size} leagues"
                )

                // guardo en cache
                leagueDao.insertLeagues(leagues.map { it.toEntity() })
                Log.d("precache-etProdeParticipateLeagues", "6️⃣ Guardado en Room (API)")
                emit(Resource.Success(leagues))

            } else {
                Log.d("precache-etProdeParticipateLeagues","6️⃣ API devolvió error → $response")
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("precache-etProdeParticipateLeagues", "❌ Excepción backend", e)
            emit(Resource.Failure("Error al obtener leagies: ${e.localizedMessage ?: e.message}"))
        }

    }.flowOn(Dispatchers.IO)

    suspend override fun deleteFollowedLeague(
        leagueId: Int,
        isAuthenticated: Boolean
    )
    : Flow<Resource<DefaultResponse>> = flow {
        emit(Resource.Loading)

        try {

            // 🔹 Borrar solo en cache
            leagueDao.deleteFollowedLeagueFromCache(leagueId)
            // Emitimos éxito local
            emit(Resource.Success(DefaultResponse(true, "League seguido eliminado del cache")))

        } catch (e: Exception) {
            emit(Resource.Failure(e.localizedMessage ?: "Error al eliminar la liga seguido"))
        }
    }.flowOn(Dispatchers.IO)

    //FIXTURES

    override suspend fun getCountryFixtures(season: Int, date: String): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getCountryFixtures(season, date)
            )
        )
    }

    override suspend fun getFixturesByRound(
        leagueId: Int,
        season: Int,
        round: String
    ): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getFixturesByRound(leagueId, season, round)
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
  /*
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
*/
    override suspend fun getFixtureFollowedTeams(
        season: Int,
        date: String
    ): Flow<Resource<List<FixtureResponse>>> = flow{

        //emit(Resource.Loading)
        emit(Resource.Loading)
        Log.d("getFixtureFollowedTeams", "1️⃣ Loading emitido")

        val localDate = LocalDate.parse(date)
        val zone = ZoneId.systemDefault()

        val startTs = localDate.atStartOfDay(zone).toEpochSecond()
        val endTs = localDate.plusDays(1).atStartOfDay(zone).toEpochSecond()

        Log.d("getFixtureFollowedTeams", "2️⃣ Consultando Room con range: $startTs - $endTs")

        try {
            // Recolectamos el Flow de Room
            fixtureDao.getFollowedFixturesTeamsOrLeagues(startTs, endTs)
                .collect { cached ->
                    if (cached.isNotEmpty()) {

                        Log.d("getFixtureFollowedTeams", "Emito cache de followed players")
                        // Emitimos el cache
                        emit(Resource.Success(cached.map { it.toDomain() }))
                    } else {
                        Log.d("getFixtureFollowedTeams", "5️⃣ Llamando backend…")

                        val result = teamRemoteDataSource.getFixtureFollowedTeams(season, date)
                        val response = ResponseToRequest.send(result)

                        if (response is Resource.Success) {
                            val fixtures = response.data!!

                            fixtureDao.insertFixtures(fixtures.map { it.toEntity() })
                            Log.d("getFixtureFollowedTeams", "6️⃣ Guardado en Room (API)")

                            emit(Resource.Success(fixtures))
                        } else {
                            Log.d("getFixtureFollowedTeams", "6️⃣ API devolvió error")
                            emit(response)
                        }
                    }
                }
        } catch (e: Exception) {
            emit(Resource.Failure(e.localizedMessage ?: "Error al obtener los fixtures de ligas o equipos seguidas"))
        }

    }.flowOn(kotlinx.coroutines.Dispatchers.IO)

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



    override suspend fun getFixtureTeam(
        teamId: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow {

        emit(Resource.Loading)
        Log.d("precachegetFixtureTeam", "1️⃣ Loading emitido")

        // 📅 Rango: 60 días atrás → 60 días adelante
        val zone = ZoneId.systemDefault()
        val now = LocalDate.now()

        val startTs = now.minusDays(30).atStartOfDay(zone).toEpochSecond()
        val endTs = now.plusDays(30).atStartOfDay(zone).toEpochSecond()

        Log.d(
            "precachegetFixtureTeam",
            "2️⃣ Consultando Room con range: $startTs - $endTs | teamId=$teamId "
        )

        // 1️⃣ Consultar CACHE local
        val cached = runCatching {
            fixtureDao.getFixturesByTeam(startTs, endTs, teamId)
        }
            .onFailure { Log.e("getFixtureTeam", "❌ Error leyendo Room", it) }
            .getOrDefault(emptyList())

        Log.d("precachegetFixtureTeam", "3️⃣ Cache size = ${cached.size}")

        if (cached.isNotEmpty()) {

            Log.d("precachegetFixtureTeam", "4️⃣ Emitiendo cache inmediatamente (${cached.size})")
            emit(Resource.Success(cached.map { it.toDomain() }))

            Log.d("precachegetFixtureTeam", "🟩 DESPUÉS DE emit() cache")
            return@flow // ⛔ evitar la llamada a la API
        }

        // No existe cache → mostramos loading
        Log.d("precachegetFixtureTeam ","4️⃣ No hay cache → mostrando Loading")
        emit(Resource.Loading)

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("precachegetFixtureTeam", "5️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getFixtureTeam(teamId)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {

                val fixtures = response.data!!
                Log.d(
                    "precachegetFixtureTeam",
                    "6️⃣ API devolvió ${fixtures.size} fixtures."
                )

                // guardo en cache
                fixtureDao.insertFixtures(fixtures.map { it.toEntity() })
                Log.d("precacheFixtureTeam", "6️⃣ Guardado en Room (API)")
                emit(Resource.Success(fixtures))

            } else {
                Log.d("precachegetFixtureTeam", "6️⃣ API devolvió error → $response")
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("precachegetFixtureTeam", "❌ Excepción backend", e)
            emit(Resource.Failure("Error al obtener fixtures: ${e.localizedMessage ?: e.message}"))
        }

    }.flowOn(kotlinx.coroutines.Dispatchers.IO)



    override suspend fun getNextFixtureTeam(
        teamId: Int
    ): Flow<Resource<FixtureResponse>> = flow {

        // 1️⃣ Leer cache local
        val now = System.currentTimeMillis() / 1000 // segundos (API-Football)

        val cached = runCatching {
            fixtureDao.getNextTeamFixture(teamId, now)
        }
            .onFailure { Log.e("precachegetNextFixtureTeam", "❌ Error leyendo Room", it) }
            .getOrNull()

        if (cached != null) {
            Log.d("precachegetNextFixtureTeam", "✅ Fixture recuperado desde cache id=${cached.id}")
            emit(Resource.Success(cached.toDomain()))
            return@flow // ⛔ NO llamar API
        }

        // 2️⃣ Llamada API solo si no hay cache
        try {
            Log.d("precachegetNextFixtureTeam", "🌐 Cache vacío → llamando API")

            val result = teamRemoteDataSource.getNextFixtureTeam(teamId)
            val response = ResponseToRequest.send(result)

            emit(response)

        } catch (e: Exception) {
            Log.e("precachegetNextFixtureTeam", "❌ Excepción backend", e)
            emit(
                Resource.Failure(
                    e.localizedMessage ?: "Error al obtener el próximo partido"
                )
            )
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun getTopFiveFixtureTeam(teamId: Int): Flow<Resource<List<FixtureResponse>>> = flow{
        emit(Resource.Loading)

        // 1️⃣ Leer cache local
        val limit = 5
        val cached = runCatching {
            fixtureDao.getLastTeamFixtures(teamId, limit)
        }.getOrDefault(emptyList())
        Log.d("precacheLastFIxturesTeam", " ultimos fixtures de team recuperados desde cache size = ${cached.size}")

        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached.map { it.toDomain() }))
            return@flow //  ⛔ No llamar API
        }

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("precacheLastFIxturesTeam", "5️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getTopFiveFixtureTeam(teamId)
            val response = ResponseToRequest.send(result)
            emit(response)

        } catch (e: Exception) {
            Log.e("precacheLastFIxturesTeam", "❌ Excepción backend", e)
            emit(Resource.Failure("Error al obtener el top ligas desde api: ${e.localizedMessage ?: e.message}"))
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun getLeagueFixture(
        leagueId: Int,
        season: Int,
        teamId: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow {

        emit(Resource.Loading)
        Log.d("precachegetLeagueFixture", "1️⃣ Loading emitido")

        // 📅 Rango: 60 días atrás → 60 días adelante
        val zone = ZoneId.systemDefault()
        val now = LocalDate.now()

        val startTs = now.minusDays(60).atStartOfDay(zone).toEpochSecond()
        val endTs = now.plusDays(60).atStartOfDay(zone).toEpochSecond()

        Log.d(
            "precachegetLeagueFixture",
            "2️⃣ Consultando Room con range: $startTs - $endTs | leagueId=$leagueId | season=$season"
        )

        // 1️⃣ Consultar CACHE local
        val cached = runCatching {
            fixtureDao.getFixturesByLeague(startTs, endTs, leagueId, season)
        }
            .onFailure { Log.e("precachegetLeagueFixture", "Error leyendo Room", it) }
            .getOrDefault(emptyList())

        Log.d("precachegetLeagueFixture", "3️⃣ Cache size = ${cached.size}")

        if (cached.isNotEmpty()) {

            Log.d("precachegetLeagueFixture", "4️⃣ Emitiendo cache inmediatamente (${cached.size})")
            emit(Resource.Success(cached.map { it.toDomain() }))

            Log.d("precachegetLeagueFixture", "🟩 DESPUÉS DE emit() cache")
            return@flow // ⛔ evitar la llamada a la API
        }

        // No existe cache → mostramos loading
        Log.d("precachegetLeagueFixture", "4️⃣ No hay cache → mostrando Loading")
        emit(Resource.Loading)

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("precachegetLeagueFixture", "5️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getLeagueFixture(leagueId, season, teamId)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {

                val fixtures = response.data!!
                Log.d(
                    "precachegetLeagueFixture",
                    "6️⃣ API devolvió ${fixtures.size} fixtures. (No se guardan en Room)"
                )

                fixtureDao.insertFixtures(fixtures.map { it.toEntity() })
                Log.d("precachegetLeagueFixture", "6️⃣ Guardado en Room (API)")
                emit(Resource.Success(fixtures))

            } else {
                Log.d("precachegetLeagueFixture", "6️⃣ API devolvió error → $response")
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("precachegetLeagueFixture", "❌ Excepción backend", e)
            emit(Resource.Failure("Error al obtener fixtures: ${e.localizedMessage ?: e.message}"))
        }

    }.flowOn(kotlinx.coroutines.Dispatchers.IO)



    override suspend fun getFixturesByDate(
        date: String,
        limit: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow {

        //emit(Resource.Loading)
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



    override suspend fun updateFixturesByDate(
        date: String,
        limit: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow {

            Log.d("precacheupdateFixturesByDate", "4️⃣ No hay cache → mostrando Loading")
            emit(Resource.Loading)

            // 🔥 AQUI SÍ SE LLAMA API
            try {
                Log.d("precacheupdateFixturesByDate", "5️⃣ Llamando backend…")

                val result = teamRemoteDataSource.getFixturesByDate(date, limit)
                val response = ResponseToRequest.send(result)

                if (response is Resource.Success) {
                    val fixtures = response.data!!

                    fixtureDao.insertFixtures(fixtures.map { it.toEntity() })
                    Log.d("precacheupdateFixturesByDate", "SE actualiza fixtures del dia y se guarda en Room (API)")

                    emit(Resource.Success(fixtures))
                } else {
                    Log.d("precacheupdateFixturesByDate", "6️⃣ API devolvió error")
                    emit(response)
                }

            } catch (e: Exception) {
                Log.e("precacheupdateFixturesByDate", "❌ Excepción backend", e)
                emit(Resource.Failure("Error: ${e.localizedMessage ?: e.message}"))
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
            Log.d("precache", "⏳ Consultando fixtures desde $start hasta $end...")

            val response = ResponseToRequest.send(
                teamRemoteDataSource.getFixturesByRange(start, end)
            )

            if (response is Resource.Success) {

                val fixtures = response.data!!
                Log.d("precache", "📥 API devolvió: ${fixtures.size} fixtures")

                // Guardar TODO en Room
                fixtureDao.insertFixtures(fixtures.map { it.toEntity() })

                // Obtener cuántos fixtures totales quedaron guardados
                val startTs = LocalDate.parse(start).atStartOfDay(zone).toEpochSecond()
                val endTs = LocalDate.parse(end).plusDays(1).atStartOfDay(zone).toEpochSecond()

                val cachedCount = fixtureDao.getFixturesByDateRange(startTs, endTs).size

                Log.d(
                    "precache",
                    "✔ Precarga completada. Guardados en cache: $cachedCount fixtures"
                )

            } else {
                Log.d("precache", "❌ Error al obtener fixtures por rango: $response")
            }

        } catch (e: Exception) {
            Log.e("precache", "❌ Excepción precache por rango", e)
        }
    }




    override suspend fun getLeagueStandings(
        leagueId: Int,
        season: Int
    ): Flow<Resource<List<StandingResponse>>> = flow{
        emit(Resource.Loading)

        // 1️⃣ Cache
        val cached = runCatching {
            leagueDao.getStandings(leagueId, season)
        }.getOrDefault(emptyList())

        if (cached.isNotEmpty()) {
            Log.d("precachegetLeagueStandings", "✅ stats desde Room")
            emit(Resource.Success(cached.toDomain()))
            return@flow
        }

        // 2️⃣ API
        try {
            Log.d("precachegetLeagueStandings", "🌐 Llamando API")

            val result = teamRemoteDataSource.getLeagueStandings(leagueId, season)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {
                val playerStat = response.data!!

               leagueDao.insertSandings(playerStat.toEntities())
                Log.d("precachegetLeagueStandings", "💾 Guardado en Room")

                emit(Resource.Success(playerStat))
            } else {
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("precachegetLeagueStandings", "❌ Error", e)
            emit(
                Resource.Failure(
                    "Error al obtener stats: ${e.localizedMessage ?: e.message}"
                )
            )
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun createFixturePrediction(
        request: FixturePredictionRequest,
        isAuthenticated: Boolean
    ): Flow<Resource<FixturePredictionResponse>> = flow{

        if (isAuthenticated) {
            emit(
                ResponseToRequest.send(
                    teamRemoteDataSource.createFixturePrediction(request)
                )
            )
        }
        else{
            fixturePredictionDao.insertFixturePrediction(request.toEntity())
        }
    }



    override suspend fun getUserFixturePredictions(
        leagueId: Int,
        season: Int
    ): Flow<Resource<List<FixturePredictionResponse>>> = flow{
        // 1️⃣ Leer cache local
        val cached = runCatching {
            fixturePredictionDao.getAll()
        }.getOrDefault(emptyList())
        Log.d("precache", " predictions recuperadas de cache = ${cached.size}")

        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached.map { it.toDomain() }))
            return@flow //  ⛔ No llamar API
        }


            // 🟦 2️⃣ Llamada al backend solo si no hay cache
            try {
                Log.d("precache", "5️⃣ Llamando backend…")

                val result = teamRemoteDataSource.getUserFixturePredictions(leagueId, season)
                val response = ResponseToRequest.send(result)
                emit(response)

            } catch (e: Exception) {
                Log.e("precache", "❌ Excepción backend", e)
                emit(Resource.Failure("Error al obtener las predicciones de la api: ${e.localizedMessage ?: e.message}"))
            }


    }.flowOn(Dispatchers.IO)


    // RANKING DE PREDICCTIONS
    override suspend fun getPredictionRanking(
        top: Int
    ): Flow<Resource<List<UserPredictionRanking>>> =flow{
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getPredictionRanking(top)
            )
        )
    }

    override suspend fun getUserPredictionSummary(
        userId: Int,
        season: Int
    ): Resource<UserPredictionSummaryResponse> =
            ResponseToRequest.send(
                teamRemoteDataSource.getUserPredictionSummary(userId, season)
            )




    // get User prediccion summary enriquecido para el frontend
    override suspend fun getUserPredictionSummaryEnriched(
        userId: Int,
        season: Int
    ): Flow<Resource<UserPredictionSummaryEnriched>> = flow {
        // 1️⃣ Llamamos al repo puro (suspend)
        val resource = getUserPredictionSummary(userId, season)

        when (resource) {
            is Resource.Success -> {
                val summary = resource.data!!

                // 2️⃣ Enriquecemos las ligas
                val enrichedLeagues = summary.leagues.mapNotNull { leagueSummary ->
                    val leagueEntity = leagueDao.getById(leagueSummary.leagueId)
                        ?: return@mapNotNull null
                    val league = leagueEntity.toDomain()

                    val enrichedFixture = leagueSummary.fixture?.let { mini ->
                        fixtureDao.getById(mini.fixtureId)?.let {
                            FixturePredictionEnriched(prediction = mini, fixture = it.toDomain())
                        }
                    }

                    LeaguePredictionSummaryEnriched(
                        league = league,
                        cantFixtures = leagueSummary.cantFixtures,
                        totalPoints = leagueSummary.totalPoints,
                        fixture = enrichedFixture
                    )
                }

                // 3️⃣ Enriquecemos la última predicción
                val enrichedLastPrediction = summary.lastPrediction?.let { last ->
                    fixtureDao.getById(last.fixtureId)?.let {
                        LastPredictionSummaryEnriched(fixture = it.toDomain(), createdAt = last.createdAt)
                    }
                }

                // 4️⃣ Emitimos el Resource enriquecido
                emit(
                    Resource.Success(
                        UserPredictionSummaryEnriched(
                            userId = summary.userId,
                            season = summary.season,
                            totalPoints = summary.totalPoints,
                            totalFixtures = summary.totalFixtures,
                            rankingPosition = summary.rankingPosition,
                            leagues = enrichedLeagues,
                            lastPrediction = enrichedLastPrediction
                        )
                    )
                )
            }

            else -> {}
        }
    }


                    // funciones de carga de precache

    override suspend fun precacheAllLeagues() {

                try {
                    Log.d("precache", "⏳ Consultando TODAS las ligas desde API…")

                    // Llamada al endpoint con parámetros vacíos
                    val response = ResponseToRequest.send(
                        teamRemoteDataSource.getLeagues(
                            name = "",
                            type = "",
                            countryName = ""
                        )
                    )

                    if (response is Resource.Success) {

                        val leagues = response.data!!
                        Log.d("precache", "📥 API devolvió: ${leagues.size} ligas")

                        // Guardar TODO en Room
                        leagueDao.insertLeagues(
                            leagues.map { it.toEntity() }
                        )

                        val cachedCount = leagueDao.getTotalCount()

                        Log.d(
                            "precache",
                            "✔ Precarga completada. Guardadas en cache: $cachedCount ligas"
                        )

                    } else {
                        Log.d("precache", "❌ Error al obtener ligas: $response")
                    }

                } catch (e: Exception) {
                    Log.e("precache", "❌ Excepción precache ligas", e)
                }
            }

                    override suspend fun precacheAllPlayers() {

                try {
                    Log.d("precache", "⏳ Consultando TODOS los players desde API…")

                    // Llamada al endpoint con parámetros vacíos o sin parámetros según tu API
                    val response = ResponseToRequest.send(
                        teamRemoteDataSource.getPlayers("", 1, 20)  // trae todos
                    )

                    if (response is Resource.Success) {

                        val players = response.data!!
                        Log.d("precache", "📥 API devolvió: ${players.size} players")

                        // Guardar TODO en Room
                        playerDao.insertPlayers(
                            players.map { it.toEntity() }
                        )

                        // Optional: contar cuántos quedaron guardados
                        val cachedCount = playerDao.getPlayers().size

                        Log.d(
                            "precache",
                            "✔ Precarga completada. Guardados en cache: $cachedCount players"
                        )

                    } else {
                        Log.d("precache", "❌ Error al obtener players: $response")
                    }

                } catch (e: Exception) {
                    Log.e("precache", "❌ Excepción precache players", e)
                }
            }


    override suspend fun precacheAllTeams(
                name:String,
                country: String,
                page: Int,
                size: Int
            ) {

                try {
                    Log.d("precache", "⏳ Consultando TODOS los teams desde API…")

                    // Llamada al endpoint con parámetros vacíos o sin parámetros según tu API
                    val response = ResponseToRequest.send(
                        teamRemoteDataSource.getAll(name, country, page, size)
                    )

                    if (response is Resource.Success) {

                        val teams = response.data!!
                        Log.d("precache", "📥 API devolvió: ${teams.size} players")

                        // Guardar TODO en Room
                        teamDao.insertTeams( teams.map { it.toEntity() })

                        // Optional: contar cuántos quedaron guardados
                        val cachedCount = teamDao.getTeams().size

                        Log.d(
                            "precache",
                            "✔ Precarga completada. Guardados en cache: $cachedCount teams"
                        )

                    } else {
                        Log.d("precache", "❌ Error al obtener teams: $response")
                    }

                } catch (e: Exception) {
                    Log.e("precache", "❌ Excepción precache teams", e)
                }
            }


                    // sicronizacion de cache con base de datos, se usa al momento de loguearse

    override suspend fun syncCachedPredictions() {
                val cached = fixturePredictionDao.getAll()
                if (cached.isEmpty()) return

                cached.forEach { entity ->
                    ResponseToRequest.send(
                        teamRemoteDataSource.createFixturePrediction(entity.toRequest())
                    )
                }

                //fixturePredictionDao.clearAll()
            }


    override suspend fun syncCachedPlayers() {
                val cached = playerDao.getAllFollowedPlayers()
                if (cached.isEmpty()) return

                cached.forEach { entity ->
                    ResponseToRequest.send(
                        teamRemoteDataSource.createFollowedPlayer(entity.player_id)
                    )
                }

                //fixturePredictionDao.clearAll()
            }


    override suspend fun syncCachedTeams() {
                val cached = teamDao.getAllFollowedTeams()
                if (cached.isEmpty()) return

                cached.forEach { entity ->
                    ResponseToRequest.send(
                        teamRemoteDataSource.createFollowedTeam(entity.team_id)
                    )
                }

                //fixturePredictionDao.clearAll()
            }

    override suspend fun syncCachedLeagues() {
                val cached = leagueDao.getAllFollowedLeagues()
                if (cached.isEmpty()) return

                cached.forEach { entity ->
                    ResponseToRequest.send(
                        teamRemoteDataSource.createFollowedTeam(entity.league_id)
                    )
                }

                //fixturePredictionDao.clearAll()
            }

    override suspend fun syncCached(){
                syncCachedPlayers()
                syncCachedPredictions()
                syncCachedTeams()
                syncCachedLeagues()
            }

 */

    //clients
    override suspend fun getClientsByProvider(
        providerId: Int,
        fullName: String,
        email:String
    ): Flow<Resource<List<ClientResponse>>> =flow{
    emit(
        ResponseToRequest.send(
            reservasRemoteDataSource.getClientsByProvider(providerId, fullName, email)
        )
    )
}

    override suspend fun createClient(
        request: ClientCreateRequest
    ): Flow<Resource<ClientResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.createClient(request)
            )
        )
    }

    override suspend fun updateClient(
        clientId:Int,
        request: ClientUpdateRequest
    ): Flow<Resource<ClientResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.updateClient(clientId = clientId, request = request)
            )
        )
    }

    override suspend fun getClientById(
        clientId: Int
    ): Flow<Resource<ClientResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getClientById(clientId)
            )
        )
    }


    //services

    override suspend fun getServicesByProvider(
        providerId: Int,
        name: String
    ): Flow<Resource<List<ServiceResponse>>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getServicesByProvider(providerId, name)
            )
        )
    }

    override suspend fun getStaffTotales(

    ): Flow<Resource<List<StaffResponse>>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getStaffTotales()
            )
        )
    }

    override suspend fun createService(
        request: ServiceCreateRequest
    ): Flow<Resource<ServiceResponse>> = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.createService(request)
            )
        )
    }

    override suspend fun updateService(
        serviceId: Int,
        request: ServiceUpdateRequest
    ): Flow<Resource<ServiceResponse>>  = flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.updateService(serviceId, request)
            )
        )
    }

    override suspend fun getServiceById(
        serviceId: Int
    ): Flow<Resource<ServiceResponse>> =flow{
        emit(
            ResponseToRequest.send(
                reservasRemoteDataSource.getServiceById(serviceId)
            )
        )
    }
}

