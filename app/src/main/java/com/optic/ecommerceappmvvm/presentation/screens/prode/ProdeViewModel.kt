package com.optic.ecommerceappmvvm.presentation.screens.prode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import com.optic.ecommerceappmvvm.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProdeViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
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
    // STATE: Quitar liga seguida
    // ---------------------------------------------
    private val _deleteFollowedLeagueState =
        MutableStateFlow<Resource<DefaultResponse>>(Resource.Loading)
    val deleteFollowedLeagueState: StateFlow<Resource<DefaultResponse>> =
        _deleteFollowedLeagueState


    // ---------------------------------------------
    // QUERY en StateFlow
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    init {
        observeSearch()
    }


    // 👉 Guardamos la última season aquí
    var latestSeason: Int? = null
    var roundCurrent: String? = null

    fun getLeagueById(leagueId:Int) {
        viewModelScope.launch {
            teamUseCase.getLeagueByIdUC(leagueId).collectLatest { result ->
                _leagueStateSingle.value = result

                // 👇 Cuando tenemos la liga, extraemos la última season
                if (result is Resource.Success) {
                    latestSeason = result.data?.seasons
                        ?.maxByOrNull { it.year }  // Último año
                        ?.year

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
                    getFollowedLeagues()
                }
            }
        }
    }

    fun deleteFollowedLeague(leagueId: Int) {
        viewModelScope.launch {
            teamUseCase.deleteFollowedLeagueUC(leagueId).collectLatest { result ->
                _deleteFollowedLeagueState.value = result

                if (result is Resource.Success) {
                    // 🔥 NO LLAMAMOS getLeagues() para no resetear el buscador
                    getFollowedLeagues()
                }
            }
        }
    }

    fun getFixtureByRound(leagueId: Int, season:Int, round:String) {
        viewModelScope.launch {
            teamUseCase.getFixturesByRoundUC(leagueId, season, round) .collectLatest { result ->
                _fixtureLeagueState.value = result
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
}
