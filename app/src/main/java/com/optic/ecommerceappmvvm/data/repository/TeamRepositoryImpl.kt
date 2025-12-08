package com.optic.ecommerceappmvvm.data.repository



import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixtureDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.LeagueDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.PlayerDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.TeamDao
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.ZoneId

class TeamRepositoryImpl(
    private val teamRemoteDataSource: TeamRemoteDataSource,
    private val fixtureDao: FixtureDao,
    private val leagueDao: LeagueDao,
    private val playerDao: PlayerDao,
    private val teamDao: TeamDao
): TeamRepository
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
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getTopLeagues()
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



    override suspend fun getFixtureTeam(
        teamId: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow {

        emit(Resource.Loading)
        Log.d("getFixtureTeam", "1️⃣ Loading emitido")

        // 📅 Rango: 60 días atrás → 60 días adelante
        val zone = ZoneId.systemDefault()
        val now = LocalDate.now()

        val startTs = now.minusDays(60).atStartOfDay(zone).toEpochSecond()
        val endTs = now.plusDays(60).atStartOfDay(zone).toEpochSecond()

        Log.d(
            "getFixtureTeam",
            "2️⃣ Consultando Room con range: $startTs - $endTs | teamId=$teamId "
        )

        // 1️⃣ Consultar CACHE local
        val cached = runCatching {
            fixtureDao.getFixturesByTeam(startTs, endTs, teamId)
        }
            .onFailure { Log.e("getFixtureTeam", "❌ Error leyendo Room", it) }
            .getOrDefault(emptyList())

        Log.d("getFixtureTeam", "3️⃣ Cache size = ${cached.size}")

        if (cached.isNotEmpty()) {

            Log.d("getFixtureTeam", "4️⃣ Emitiendo cache inmediatamente (${cached.size})")
            emit(Resource.Success(cached.map { it.toDomain() }))

            Log.d("ggetFixtureTeam", "🟩 DESPUÉS DE emit() cache")
            return@flow // ⛔ evitar la llamada a la API
        }

        // No existe cache → mostramos loading
        Log.d("getFixtureTeam ","4️⃣ No hay cache → mostrando Loading")
        emit(Resource.Loading)

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("getFixtureTeam", "5️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getFixtureTeam(teamId)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {

                val fixtures = response.data!!
                Log.d(
                    "getFixtureTeam",
                    "6️⃣ API devolvió ${fixtures.size} fixtures."
                )

                // guardo en cache
                fixtureDao.insertFixtures(fixtures.map { it.toEntity() })
                Log.d("getLeagueFixture", "6️⃣ Guardado en Room (API)")
                emit(Resource.Success(fixtures))

            } else {
                Log.d("getFixtureTeam", "6️⃣ API devolvió error → $response")
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("getFixtureTeam", "❌ Excepción backend", e)
            emit(Resource.Failure("Error al obtener fixtures: ${e.localizedMessage ?: e.message}"))
        }

    }.flowOn(kotlinx.coroutines.Dispatchers.IO)

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
        season: Int,
        teamId: Int
    ): Flow<Resource<List<FixtureResponse>>> = flow {

        emit(Resource.Loading)
        Log.d("getLeagueFixture", "1️⃣ Loading emitido")

        // 📅 Rango: 60 días atrás → 60 días adelante
        val zone = ZoneId.systemDefault()
        val now = LocalDate.now()

        val startTs = now.minusDays(60).atStartOfDay(zone).toEpochSecond()
        val endTs = now.plusDays(60).atStartOfDay(zone).toEpochSecond()

        Log.d(
            "getLeagueFixture",
            "2️⃣ Consultando Room con range: $startTs - $endTs | leagueId=$leagueId | season=$season"
        )

        // 1️⃣ Consultar CACHE local
        val cached = runCatching {
            fixtureDao.getFixturesByLeague(startTs, endTs, leagueId, season)
        }
            .onFailure { Log.e("getLeagueFixture", "Error leyendo Room", it) }
            .getOrDefault(emptyList())

        Log.d("getLeagueFixture", "3️⃣ Cache size = ${cached.size}")

        if (cached.isNotEmpty()) {

            Log.d("getLeagueFixture", "4️⃣ Emitiendo cache inmediatamente (${cached.size})")
            emit(Resource.Success(cached.map { it.toDomain() }))

            Log.d("getLeagueFixture", "🟩 DESPUÉS DE emit() cache")
            return@flow // ⛔ evitar la llamada a la API
        }

        // No existe cache → mostramos loading
        Log.d("getLeagueFixture", "4️⃣ No hay cache → mostrando Loading")
        emit(Resource.Loading)

        // 🟦 2️⃣ Llamada al backend solo si no hay cache
        try {
            Log.d("getLeagueFixture", "5️⃣ Llamando backend…")

            val result = teamRemoteDataSource.getLeagueFixture(leagueId, season, teamId)
            val response = ResponseToRequest.send(result)

            if (response is Resource.Success) {

                val fixtures = response.data!!
                Log.d(
                    "getLeagueFixture",
                    "6️⃣ API devolvió ${fixtures.size} fixtures. (No se guardan en Room)"
                )

                fixtureDao.insertFixtures(fixtures.map { it.toEntity() })
                Log.d("getLeagueFixture", "6️⃣ Guardado en Room (API)")
                emit(Resource.Success(fixtures))

            } else {
                Log.d("getLeagueFixture", "6️⃣ API devolvió error → $response")
                emit(response)
            }

        } catch (e: Exception) {
            Log.e("getLeagueFixture", "❌ Excepción backend", e)
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

        val start = today.minusDays(65).toString() // yyyy-MM-dd
        val end = today.plusDays(65).toString()

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
        emit(
            ResponseToRequest.send(
                teamRemoteDataSource.getLeagueStandings(leagueId, season)
            )
        )
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

}