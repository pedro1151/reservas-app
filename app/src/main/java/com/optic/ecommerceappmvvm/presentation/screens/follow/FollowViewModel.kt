package com.optic.ecommerceappmvvm.presentation.screens.follow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import com.optic.ecommerceappmvvm.domain.useCase.trivias.TriviasUseCase
import com.optic.ecommerceappmvvm.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase,
    private val triviasUseCase: TriviasUseCase
) : ViewModel() {

    // ---------------------------------------------------------
    // Estados públicos
    // ---------------------------------------------------------
    private val _teamsState = MutableStateFlow<Resource<List<Team>>>(Resource.Loading)
    val teamsState: StateFlow<Resource<List<Team>>> = _teamsState

    private val _playersState = MutableStateFlow<Resource<List<Player>>>(Resource.Loading)
    val playersState: StateFlow<Resource<List<Player>>> = _playersState

    private val _followedPlayerState = MutableStateFlow<Resource<FollowedPlayerResponse>>(Resource.Loading)
    val followedPlayerState: StateFlow<Resource<FollowedPlayerResponse>> = _followedPlayerState

    private val _deleteFollowedState = MutableStateFlow<Resource<DefaultResponse>>(Resource.Loading)
    val deleteFollowedState: StateFlow<Resource<DefaultResponse>> = _deleteFollowedState

    private val _followedTeamListState = MutableStateFlow<Resource<List<Team>>>(Resource.Loading)
    val followedTeamListState: StateFlow<Resource<List<Team>>> = _followedTeamListState

    private val _createFollowedTeamState = MutableStateFlow<Resource<FollowedTeamResponse>>(Resource.Loading)
    val createFollowedTeamState: StateFlow<Resource<FollowedTeamResponse>> = _createFollowedTeamState

    private val _deleteFollowedTeamState = MutableStateFlow<Resource<DefaultResponse>>(Resource.Loading)
    val deleteFollowedTeamState: StateFlow<Resource<DefaultResponse>> = _deleteFollowedTeamState

    // ---------------------------------------------------------
    // Paginado Players
    // ---------------------------------------------------------
    private var currentPage = 1
    private val pageSize = 20
    var isLoadingPage = false
    private var allPlayersLoaded = false
    private val loadedPlayers = mutableListOf<Player>()

    fun getPlayers(name: String) {
        if (isLoadingPage || allPlayersLoaded) return
        isLoadingPage = true

        viewModelScope.launch {
            try {
                val result = teamUseCase.getPlayersUseCase(name, currentPage, pageSize)
                    .first()
                when (result) {
                    is Resource.Success -> {
                        val newPlayers = result.data ?: emptyList()
                        if (newPlayers.isEmpty()) allPlayersLoaded = true
                        else {
                            loadedPlayers.addAll(newPlayers)
                            currentPage++
                            _playersState.value = Resource.Success(loadedPlayers.toList())
                        }
                    }
                    is Resource.Failure -> _playersState.value = result
                    is Resource.Loading -> if (loadedPlayers.isEmpty()) _playersState.value = Resource.Loading
                    else -> {}
                }
            } finally {
                isLoadingPage = false
            }
        }
    }

    // ---------------------------------------------------------
    // Paginado Teams
    // ---------------------------------------------------------
    private var currentPageTeam = 1
    private val pageSizeTeam = 20
    var isLoadingPageTeam = false
    private var allLoadedTeam = false
    private val loadedTeams = mutableListOf<Team>()

    fun getTeams(name: String, country: String) {
        if (isLoadingPageTeam || allLoadedTeam) return
        isLoadingPageTeam = true

        viewModelScope.launch {
            try {
                val result = teamUseCase.getallTeamUseCase(name, country, currentPageTeam, pageSizeTeam)
                    .first()
                when (result) {
                    is Resource.Success -> {
                        val newTeams = result.data ?: emptyList()
                        if (newTeams.isEmpty()) allLoadedTeam = true
                        else {
                            loadedTeams.addAll(newTeams)
                            currentPageTeam++
                            _teamsState.value = Resource.Success(loadedTeams.toList())
                        }
                    }
                    is Resource.Failure -> _teamsState.value = result
                    is Resource.Loading -> if (loadedTeams.isEmpty()) _teamsState.value = Resource.Loading
                    else -> {}
                }
            } finally {
                isLoadingPageTeam = false
            }
        }
    }

    // ---------------------------------------------------------
    // Jugadores seguidos: Flow directo de UseCase
    // ---------------------------------------------------------
    val followedPlayerListState: StateFlow<Resource<List<Player>>> =
        teamUseCase.getFollowedPlayersUC()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Resource.Loading
            )

    // ---------------------------------------------------------
    // Seguir / Dejar de seguir players
    // ---------------------------------------------------------
    fun createFollowedPlayer(playerId: Int, isAuthenticated: Boolean) {
        viewModelScope.launch {
            teamUseCase.createFollowedPlayerUC(playerId, isAuthenticated)
                .collectLatest { result ->
                    Log.d("FollowVM", "createFollowedPlayer: $result")
                    _followedPlayerState.value = result
                }
        }
    }

    fun deleteFollowedPlayer(playerId: Int, isAuthenticated: Boolean) {
        viewModelScope.launch {
            teamUseCase.deleteFollowedPlayerUC(playerId, isAuthenticated)
                .collectLatest { result ->
                    Log.d("FollowVM", "deleteFollowedPlayer: $result")
                    _deleteFollowedState.value = result
                }
        }
    }

    // ---------------------------------------------------------
    // Lista filtrada de players (no seguidos)
    // ---------------------------------------------------------
    val filteredPlayersState: StateFlow<List<Player>> = combine(
        playersState,
        followedPlayerListState
    ) { allPlayersResource, followedPlayersResource ->
        val allPlayers = (allPlayersResource as? Resource.Success)?.data ?: emptyList()
        val followedPlayers = (followedPlayersResource as? Resource.Success)?.data ?: emptyList()
        val followedIds = followedPlayers.mapNotNull { it.id }.toSet()
        allPlayers.filter { it.id !in followedIds }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // ---------------------------------------------------------
    // Seguir / Dejar de seguir teams
    // ---------------------------------------------------------
    private fun getFollowedTeams() {
        viewModelScope.launch {
            teamUseCase.getFollowedTeamsUC().collectLatest { result ->
                _followedTeamListState.value = result
            }
        }
    }

    fun createFollowedTeam(teamId: Int) {
        viewModelScope.launch {
            teamUseCase.createFollowedTeamUC(teamId).collectLatest { result ->
                _createFollowedTeamState.value = result
                if (result is Resource.Success) {
                    getFollowedTeams()
                    getSuggestedTeams(50)
                    _teamsState.value = _teamsState.value
                    _followedTeamListState.value = _followedTeamListState.value
                }
            }
        }
    }

    fun deleteFollowedTeam(teamId: Int) {
        viewModelScope.launch {
            teamUseCase.deleteFollowedTeamUC(teamId).collectLatest { result ->
                _deleteFollowedTeamState.value = result
                if (result is Resource.Success) {
                    getFollowedTeams()
                    getSuggestedTeams(50)
                    _teamsState.value = _teamsState.value
                    _followedTeamListState.value = _followedTeamListState.value
                }
            }
        }
    }

    val filteredTeamsState: StateFlow<List<Team>> = combine(
        teamsState,
        followedTeamListState
    ) { allTeamsResource, followedTeamsResource ->
        val allTeams = (allTeamsResource as? Resource.Success)?.data ?: emptyList()
        val followedTeams = (followedTeamsResource as? Resource.Success)?.data ?: emptyList()
        val followedIds = followedTeams.mapNotNull { it.id }.toSet()
        allTeams.filter { it.id !in followedIds }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // ---------------------------------------------------------
    // Inicio: carga inicial (page=1, size=20)
    // ---------------------------------------------------------
    fun start() {
        currentPage = 1
        currentPageTeam = 1
        allPlayersLoaded = false
        allLoadedTeam = false
        loadedPlayers.clear()
        loadedTeams.clear()

        getTeams("", "")        // lista inicial equipos 1..20
        getPlayers("null")      // lista inicial jugadores 1..20
        getFollowedTeams()      // cargar equipos seguidos
    }

    private fun getSuggestedTeams(limit: Int) {
        viewModelScope.launch {
            teamUseCase.getSuggestedTeamsUC(limit).collectLatest { result ->
                _teamsState.value = result
            }
        }
    }
}
