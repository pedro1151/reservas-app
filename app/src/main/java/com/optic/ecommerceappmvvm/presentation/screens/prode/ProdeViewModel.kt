package com.optic.ecommerceappmvvm.presentation.screens.prode

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import com.optic.ecommerceappmvvm.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionResponse
import android.util.Log
import kotlinx.coroutines.Job

@HiltViewModel
class ProdeViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
    // ---------------------------------------------

    // ---------------------------------------------
    // STATE: ligas en general, todas
    // ---------------------------------------------
    private val _leaguesState = MutableStateFlow<Resource<List<League>>>(Resource.Loading)
    val leaguesState: StateFlow<Resource<List<League>>> = _leaguesState

    private val _fixtureLeagueState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureLeagueState: StateFlow<Resource<List<FixtureResponse>>> = _fixtureLeagueState

    private val _leagueStateSingle = MutableStateFlow<Resource<LeagueCompleteResponse>>(Resource.Loading)
    val leagueStateSingle: StateFlow<Resource<LeagueCompleteResponse>> = _leagueStateSingle


    // ---------------------------------------------
    // STATE: lista de top ligas
    // ---------------------------------------------
    private val _leaguesTopState = MutableStateFlow<Resource<List<League>>>(Resource.Loading)
    val leaguesTopState: StateFlow<Resource<List<League>>> = _leaguesTopState



    // ---------------------------------------------
    // STATE: Ligas seguidas
    // ---------------------------------------------
    private val _followedLeaguesListState = MutableStateFlow<Resource<List<League>>>(Resource.Loading)
    val followedLeaguesListState: StateFlow<Resource<List<League>>> = _followedLeaguesListState

    // ---------------------------------------------
    // STATE: Crear liga seguida
    // ---------------------------------------------
    private val _createFollowedLeagueState =
        MutableStateFlow<Resource<FollowedLeagueResponse>>(Resource.Loading)
    val createFollowedLeagueState: StateFlow<Resource<FollowedLeagueResponse>> =
        _createFollowedLeagueState


    // ---------------------------------------------
    // QUERY en StateFlow
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    //prode
    private val _createFixturePredictionState = MutableStateFlow<Resource<FixturePredictionResponse>>(Resource.Loading)
    val createFixturePredictionState  : StateFlow<Resource<FixturePredictionResponse>> = _createFixturePredictionState

    private val _getUserPredictions = MutableStateFlow<Resource<List<FixturePredictionResponse>>>(Resource.Loading)
    val getUserPredictions  : StateFlow<Resource<List<FixturePredictionResponse>>> = _getUserPredictions

    //loading para guardado de Prodes
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    init {
        observeSearch()
        observeUserPredictions()
        observeFixtures()
    }

    data class LeagueSections(
        val followed: List<League>,
        val top: List<League>,
        val explore: List<League>
    )

    val leagueSections: StateFlow<LeagueSections> =
        combine(
            leaguesState,
            leaguesTopState,
            followedLeaguesListState,
            searchQuery
        ) { allRes, topRes, followedRes, query ->

            val all = (allRes as? Resource.Success)?.data.orEmpty()
            val top = (topRes as? Resource.Success)?.data.orEmpty()
            val followed = (followedRes as? Resource.Success)?.data.orEmpty()

            val q = query.trim().lowercase()

            fun League.matches(): Boolean =
                q.isBlank() ||
                        name?.lowercase()?.contains(q) == true ||
                        country?.name?.lowercase()?.contains(q) == true

            val followedFiltered = followed.filter { it.matches() }

            val followedIds = followed.mapNotNull { it.id }.toSet()
            val topIds = top.mapNotNull { it.id }.toSet()

            val topFiltered = top
                .filter { it.id !in followedIds }
                .filter { it.matches() }

            val exploreFiltered = all
                .filter { it.id !in followedIds && it.id !in topIds }
                .filter { it.matches() }

            LeagueSections(
                followed = followedFiltered,
                top = topFiltered,
                explore = exploreFiltered
            )

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            LeagueSections(emptyList(), emptyList(), emptyList())
        )



    private fun observeFixtures() {
        viewModelScope.launch {
            _fixtureLeagueState.collectLatest { resource ->
                if (resource is Resource.Success) {
                    tryMerge()
                }
            }
        }
    }


    // 👉 Guardamos la última season aquí
    var latestSeason: Int = 2025
    var roundCurrent: String? = "League Stage - 6"

    fun getLeagueById(leagueId:Int) {
        viewModelScope.launch {
            teamUseCase.getLeagueByIdUC(leagueId).collectLatest { result ->
                _leagueStateSingle.value = result

                // 👇 Cuando tenemos la liga, extraemos la última season
                if (result is Resource.Success) {
                    latestSeason = result.data?.seasons
                        ?.maxByOrNull { it.year }  // Último año
                        ?.year!!

                    // 👉 Nombre del round actual
                    roundCurrent = result.data?.rounds
                        ?.firstOrNull { it.isCurrent == true }
                        ?.roundName
                }
            }
        }
    }

    // ---------------------------------------------
    // OBSERVAR EL SEARCH QUERY
    // ---------------------------------------------
    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { q ->
                    if (q.isBlank()) {
                        getLeagues()
                    } else {
                        getLeagues(
                            name = q,
                            type = q,
                            countryName = q
                        )
                    }
                }
        }
    }

    // Llamado desde el UI
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // ---------------------------------------------
    // API CALLS
    // ---------------------------------------------
    fun getLeagues(
        name: String = "",
        type: String = "",
        countryName: String = ""
    ) {
        viewModelScope.launch {
            teamUseCase.getLeaguesUseCase(name, type, countryName)
                .collectLatest { result ->
                    _leaguesState.value = result
                }
        }
    }


    fun getTopLeagues() {
        viewModelScope.launch {
            teamUseCase.getTopLeaguesUC()
                .collectLatest { result ->
                    _leaguesTopState.value = result
                }
        }
    }
    fun getFollowedLeagues() {
        viewModelScope.launch {
            teamUseCase.getFollowedLeaguesUC()
                .collectLatest { result ->
                    _followedLeaguesListState.value = result
                }
        }
    }

    fun createFollowedLeague(leagueId: Int) {
        viewModelScope.launch {
            teamUseCase.createFollowedLeagueUC(leagueId).collectLatest { result ->
                _createFollowedLeagueState.value = result

                if (result is Resource.Success) {
                    // 🔥 NO LLAMAMOS getLeagues() para no romper la búsqueda
                   // getFollowedLeagues()
                }
            }
        }
    }


    private var fixtureJob: Job? = null

    fun getFixtureByRound(leagueId: Int, season: Int, round: String) {
        fixtureJob?.cancel() // 🔥 cancelar la anterior

        fixtureJob = viewModelScope.launch {

            // 🔥 limpiar estado ANTES de cargar nuevo round
            _fixtureLeagueState.value = Resource.Loading
            _userPredictions.value = emptyMap()

            teamUseCase.getFixturesByRoundUC(leagueId, season, round)
                .collectLatest { result ->
                    _fixtureLeagueState.value = result

                    if (result is Resource.Success) {
                        val freshPredictions = mutableMapOf<Int, UserPrediction>()

                        result.data?.forEach { fixture ->
                            val id = fixture.id ?: return@forEach
                            freshPredictions[id] = UserPrediction()
                        }

                        _userPredictions.value = freshPredictions
                        tryMerge()
                    }
                }
        }
    }




    // ---------------------------------------------
    // LISTA DE LIGAS NO SEGUIDAS (por si la necesitas)
    // ---------------------------------------------
    val filteredLeaguesState: StateFlow<List<League>> =
        combine(leaguesState, followedLeaguesListState) { allLeaguesRes, followedRes ->

            val all = (allLeaguesRes as? Resource.Success)?.data ?: emptyList()
            val followed = (followedRes as? Resource.Success)?.data ?: emptyList()

            val followedIds = followed.mapNotNull { it.id }.toSet()
            all.filter { it.id !in followedIds }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    // ---------------------------------------------
// PRODE - Predicciones del usuario
// ---------------------------------------------

    data class UserPrediction(
        var prediction: String? = "SELECCIONAR",
        var goalsHome: Int? = 0,
        var goalsAway: Int? = 0
    )

    var isEditing = mutableStateOf(false)
        private set

    fun toggleEditing() {
        isEditing.value = !isEditing.value
    }

    fun stopEditing() {
        isEditing.value = false
    }



    private val _userPredictions = MutableStateFlow<Map<Int, UserPrediction>>(emptyMap())
    val userPredictions = _userPredictions.asStateFlow()

    fun updatePrediction(fixtureId: Int, prediction: String) {
        val map = _userPredictions.value.toMutableMap()
        val item = map[fixtureId] ?: UserPrediction()
        map[fixtureId] = item.copy(prediction = prediction)
        _userPredictions.value = map
    }

    fun updateGoalsHome(fixtureId: Int, goals: Int) {
        val map = _userPredictions.value.toMutableMap()
        val item = map[fixtureId] ?: UserPrediction()
        map[fixtureId] = item.copy(goalsHome = goals)
        _userPredictions.value = map
    }

    fun updateGoalsAway(fixtureId: Int, goals: Int) {
        val map = _userPredictions.value.toMutableMap()
        val item = map[fixtureId] ?: UserPrediction()
        map[fixtureId] = item.copy(goalsAway = goals)
        _userPredictions.value = map
    }



    private fun observeUserPredictions() {
        viewModelScope.launch {
            _getUserPredictions.collectLatest { resource ->
                if (resource is Resource.Success) {
                    tryMerge()
                }
            }
        }
    }

    private fun mergePredictions(predictions: List<FixturePredictionResponse>) {

        // 1) Mapear predicciones del backend por fixtureId
        val backendMap = predictions.associate { pred ->
            pred.fixtureId to UserPrediction(
                prediction = when (pred.prediction) {
                    "home" -> "GANA LOCAL"
                    "draw" -> "EMPATE"
                    "away" -> "GANA VISITA"
                    else -> "SELECCIONAR"
                },
                goalsHome = pred.goalsHome,
                goalsAway = pred.goalsAway
            )
        }

        // 2) Obtener fixtures cargados
        val fixtures = (fixtureLeagueState.value as? Resource.Success)?.data ?: emptyList()

        // 3) Combinar fixture por fixture SIEMPRE devolviendo un Pair()
        val merged = fixtures.associate { fixture ->
            val id = fixture.id

            id to (backendMap[id]
                ?: _userPredictions.value[id]
                ?: UserPrediction())
        }

        // 4) Actualizar estado
        _userPredictions.value = merged
    }



    private fun tryMerge() {
        val fixtureRes = fixtureLeagueState.value
        val predictionRes = getUserPredictions.value

        if (fixtureRes !is Resource.Success) return
        if (predictionRes !is Resource.Success) return

        val fixtures = fixtureRes.data ?: emptyList()
        val predictions = predictionRes.data ?: emptyList()

        mergePredictions(predictions)
    }

    fun getUserFixturePredictions(leagueId: Int, season: Int) {
        viewModelScope.launch {
            Log.d("PRODE", "🔥 leagueid  = ${leagueId}")
            Log.d("PRODE", "🔥 season = ${season}")

            teamUseCase.getUserFixturePredictionsUC(leagueId, season)
                .collectLatest { result ->

                    Log.d("PRODE", "🔥 getUserFixturePredictions() → estado = ${result::class.simpleName}")

                    when(result) {

                        is Resource.Loading -> {
                            Log.d("PRODE", "⏳ Cargando predicciones del backend...")
                        }

                        is Resource.Failure -> {
                            Log.e("PRODE", "❌ Error backend: ${result.message}")
                        }

                        is Resource.Success -> {
                            val list = result.data ?: emptyList()

                            Log.d("PRODE", "✅ Backend devolvió ${list.size} predicciones")

                            list.forEach { pred ->
                                Log.d(
                                    "PRODE",
                                    "➡ fixtureId=${pred.fixtureId}, pred=${pred.prediction}, GH=${pred.goalsHome}, GA=${pred.goalsAway}"
                                )
                            }
                        }

                        else -> {}
                    }

                    _getUserPredictions.value = result
                }
        }
    }



    fun savePrediction(request: FixturePredictionRequest) {
        viewModelScope.launch {
            teamUseCase.createFixturePredictionUC(request).collectLatest { result ->
                _createFixturePredictionState.value = result
            }
        }
    }



    fun saveAllPredictions(context: Context) {
        viewModelScope.launch {

            _isSaving.value = true  // <<< INICIA LOADING
            try {

                val fixturesResource = fixtureLeagueState.value
                if (fixturesResource !is Resource.Success) {
                    Toast.makeText(context, "No hay fixtures cargados", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val fixtures = fixturesResource.data ?: emptyList()

                // Si no hay season o round → NO enviamos nada
                val season = latestSeason
                val round = roundCurrent

                if (season == null || round == null) {
                    Toast.makeText(context, "Faltan datos de la liga", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Recorrer todas las predicciones hechas por el usuario
                val predictions = _userPredictions.value

                var savedCount = 0

                fixtures.forEach { fixture ->

                    val pred = predictions[fixture.id]

                    // Si no hay predicción → no lo guardamos
                    if (pred == null) return@forEach

                    // Si No eligio el ganador entonces no guardar
                    if (pred.prediction == "SELECCIONAR") return@forEach

                    // Crear el request que tu API espera
                    val request = FixturePredictionRequest(
                        fixtureId = fixture.id!!,
                        leagueId = fixture.league?.id ?: return@forEach,
                        leagueSeason = latestSeason,
                        round = round,
                        userId = 1,  // ⚠️ <-- pon aquí tu user real
                        prediction = when (pred.prediction) {
                            "GANA LOCAL" -> "home"
                            "EMPATE" -> "draw"
                            "GANA VISITA" -> "away"
                            else -> "draw"
                        },
                        goalsHome = pred.goalsHome,
                        goalsAway = pred.goalsAway
                    )

                    // Guardar usando tu función existente
                    savePrediction(request)
                    savedCount++
                }

                Toast.makeText(
                    context,
                    "Se guardaron $savedCount predicciones",
                    Toast.LENGTH_SHORT
                ).show()

                toggleEditing() // cerrar modo edición
            }
            finally{
                _isSaving.value = false   // <<< FINALIZA LOADING SIEMPRE
            }
        }

    }


}
