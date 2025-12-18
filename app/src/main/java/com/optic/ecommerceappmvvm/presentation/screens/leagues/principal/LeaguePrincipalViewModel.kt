package com.optic.ecommerceappmvvm.presentation.screens.leagues.principal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import com.optic.ecommerceappmvvm.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguePrincipalViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
    // ---------------------------------------------
    private val _leaguesState = MutableStateFlow<Resource<List<League>>>(Resource.Loading)
    val leaguesState: StateFlow<Resource<List<League>>> = _leaguesState

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

    fun getFollowedLeagues() {
        viewModelScope.launch {
            teamUseCase.getFollowedLeaguesUC()
                .collectLatest { result ->
                    _followedLeaguesListState.value = result
                }
        }
    }

    fun createFollowedLeague(leagueId: Int, isAuthenticated: Boolean) {
        viewModelScope.launch {
            teamUseCase.createFollowedLeagueUC(leagueId, isAuthenticated).collectLatest { result ->
                _createFollowedLeagueState.value = result

                if (result is Resource.Success) {
                    // 🔥 NO LLAMAMOS getLeagues() para no romper la búsqueda
                    getFollowedLeagues()
                }
            }
        }
    }

    fun deleteFollowedLeague(leagueId: Int, isAuthenticated: Boolean) {
        viewModelScope.launch {
            teamUseCase.deleteFollowedLeagueUC(leagueId, isAuthenticated).collectLatest { result ->
                _deleteFollowedLeagueState.value = result

                if (result is Resource.Success) {
                    // 🔥 NO LLAMAMOS getLeagues() para no resetear el buscador
                    getFollowedLeagues()
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
}
