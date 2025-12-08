package com.optic.ecommerceappmvvm.presentation.screens.follow


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import com.optic.ecommerceappmvvm.domain.useCase.trivias.TriviasUseCase
import com.optic.ecommerceappmvvm.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.types.TypeCheckerState.SupertypesPolicy.None

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase,
    private val triviasUseCase: TriviasUseCase
) : ViewModel() {

    init {
        getTeams("", "",1 ,20) // inicialmente trae solo 20 datos
        getPlayers("null", 1, 20) // inicialmente trae solo 20 datos
        getFollowedPlayers()
        getFollowedTeams()
    }

    private val _teamsState = MutableStateFlow<Resource<List<Team>>>(Resource.Loading)
    val teamsState: StateFlow<Resource<List<Team>>> = _teamsState

    private val _playersState = MutableStateFlow<Resource<List<Player>>>(Resource.Loading)
    val playersState: StateFlow<Resource<List<Player>>> = _playersState



    // VAl para Jugadores seguidos y crear relacion jugador seguido - User

    private val _followedPlayerState = MutableStateFlow<Resource<FollowedPlayerResponse>>(Resource.Loading)
    val followedPlayerState: StateFlow<Resource<FollowedPlayerResponse>> = _followedPlayerState

    private val _followedPlayerListState = MutableStateFlow<Resource<List<Player>>>(Resource.Loading)
    val followedPlayerListState: StateFlow<Resource<List<Player>>> = _followedPlayerListState

    private val _deleteFollowedState = MutableStateFlow<Resource<DefaultResponse>>(Resource.Loading)
    val deleteFollowedState : StateFlow<Resource< DefaultResponse>> = _deleteFollowedState

    // PAGINADO de players
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
                    .first() // obtenemos solo un emit, no collect
                when (result) {
                    is Resource.Success -> {
                        val newPlayers = result.data ?: emptyList()
                        if (newPlayers.isEmpty()) {
                            allPlayersLoaded = true
                        } else {
                            loadedPlayers.addAll(newPlayers)
                            currentPage++
                            _playersState.value = Resource.Success(loadedPlayers.toList())
                        }
                    }
                    is Resource.Failure -> {
                        _playersState.value = result
                    }
                    is Resource.Loading -> {
                        if (loadedPlayers.isEmpty()) {
                            _playersState.value = Resource.Loading
                        }
                    }

                    else -> {}
                }
            } finally {
                isLoadingPage = false
            }
        }

    }


    // PAGINADO de players
    private var currentPageTeam = 1
    private val pageSizeTeam = 20
    var isLoadingPageTeam = false
    private var allLoadedTeam = false
    private val loadedTeams = mutableListOf<Team>()


    fun getTeams(name: String, country:String) {
        if (isLoadingPageTeam || allLoadedTeam) return
        isLoadingPageTeam = true


        viewModelScope.launch {
            try {
                val result = teamUseCase.getallTeamUseCase(name, country,currentPageTeam, pageSizeTeam)
                    .first() // obtenemos solo un emit, no collect
                when (result) {
                    is Resource.Success -> {
                        val newTeams = result.data ?: emptyList()
                        if (newTeams.isEmpty()) {
                            allLoadedTeam = true
                        } else {
                            loadedTeams.addAll(newTeams)
                            currentPageTeam++
                            _teamsState.value = Resource.Success(loadedTeams.toList())
                        }
                    }
                    is Resource.Failure -> {
                        _teamsState.value = result
                    }
                    is Resource.Loading -> {
                        if (loadedTeams.isEmpty()) {
                            _teamsState.value = Resource.Loading
                        }
                    }

                    else -> {}
                }
            } finally {
                isLoadingPageTeam = false
            }
        }

    }





    //LISTA DE TEAMS SEGUIDOS
    private val _followedTeamListState = MutableStateFlow<Resource<List<Team>>>(Resource.Loading)
    val followedTeamListState: StateFlow<Resource<List<Team>>> = _followedTeamListState

    // estado para el Crear un team seguido
    private val _createfollowedTeamState = MutableStateFlow<Resource<FollowedTeamResponse>>(Resource.Loading)
    val createfollowedTeamState : StateFlow<Resource<FollowedTeamResponse>> = _createfollowedTeamState

      // estado para delete team seguido
    private val _deleteFollowedTeamState = MutableStateFlow<Resource<DefaultResponse>>(Resource.Loading)
    val deleteFollowedTeamState : StateFlow<Resource< DefaultResponse>> = _deleteFollowedTeamState



    private fun getPlayers(name:String, page: Int, size:Int) {
        viewModelScope.launch {
            teamUseCase.getPlayersUseCase(name, page, size).collectLatest { result ->
                _playersState.value = result
            }
        }
    }


    private fun getSuggestedPlayers(limit: Int) {
        viewModelScope.launch {
            triviasUseCase.getSuggestedPlayersUC(limit).collectLatest { result ->
                _playersState.value = result
            }
        }
    }
    private fun getTeams(name: String, country: String, page: Int, size: Int) {
        viewModelScope.launch {
            teamUseCase.getallTeamUseCase(name, country, page, size).collectLatest { result ->
                _teamsState.value = result
            }
        }
    }


    private fun getSuggestedTeams(limit: Int) {
        viewModelScope.launch {
            teamUseCase.getSuggestedTeamsUC(limit) .collectLatest { result ->
                _teamsState.value = result
            }
        }
    }

    private fun getFollowedPlayers() {
        viewModelScope.launch {
            teamUseCase.getFollowedPlayersUC().collectLatest { result ->
                _followedPlayerListState.value = result
            }
        }
    }

    // borrar un seguido

    fun deleteFollowedPlayer(playerId: Int) {
        viewModelScope.launch {
            teamUseCase.deleteFollowedPlayerUC(playerId).collectLatest { result ->
                Log.d("FollowVM", "deleteFollowedPlayer result: $result")
                _deleteFollowedState.value = result

                if (result is Resource.Success) {
                    Log.d("FollowVM", "Refresh followed and all players")
                    getFollowedPlayers() // 👈 refresca la lista de jugadores seguidos
                    getPlayers("null", 1, 20)

                    // Forzar emitir valores nuevos aunque sean iguales (para test)
                    _playersState.value = _playersState.value
                    _followedPlayerListState.value = _followedPlayerListState.value
                }
            }


        }
    }



    // funciones para Jugadores seguidos y crear relacion jugador seguido - User

    fun createFollowedPlayer(playerId: Int) {
        viewModelScope.launch {
            teamUseCase.createFollowedPlayerUC(playerId).collectLatest { result ->
                Log.d("FollowVM", "createFollowedPlayer result: $result")
                _followedPlayerState.value = result

                if (result is Resource.Success) {
                    Log.d("FollowVM", "Refresh followed and all players")
                    getFollowedPlayers() // 👈 refresca la lista de jugadores seguidos
                    getPlayers("null", 1, 20)

                    // Forzar emitir valores nuevos aunque sean iguales (para test)
                    _playersState.value = _playersState.value
                    _followedPlayerListState.value = _followedPlayerListState.value
                }
            }


        }
    }

    // Lista Filtrada, jugadores que aun no ha sido seguidos

    val filteredPlayersState: StateFlow<List<Player>> = combine(
        playersState,
        followedPlayerListState
    ) { allPlayersResource, followedPlayersResource ->
        val allPlayers = (allPlayersResource as? Resource.Success)?.data?.toList() ?: emptyList()
        val followedPlayers = (followedPlayersResource as? Resource.Success)?.data?.toList() ?: emptyList()

        val followedIds = followedPlayers.mapNotNull { it.id }.toSet()
        val filtered = allPlayers.filter { it.id !in followedIds }

        println("combine ejecutado: jugadores=${allPlayers.size}, seguidos=${followedPlayers.size}, filtrados=${filtered.size}")
        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )





    // funciones para TEAMS SEGUIDOS

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
                Log.d("FollowVM", "createFollowedPlayer result: $result")
                _createfollowedTeamState.value = result

                if (result is Resource.Success) {
                    Log.d("FollowVM", "Refresh followed and all teams")
                    getFollowedTeams() // 👈 refresca la lista de teams seguidos
                    getSuggestedTeams(50)

                    // Forzar emitir valores nuevos aunque sean iguales (para test)
                    _teamsState.value = _teamsState.value
                    _followedTeamListState.value =  _followedTeamListState.value
                }
            }


        }
    }


    // borrar un TEAM seguido

    fun deleteFollowedTeam(teamId: Int) {
        viewModelScope.launch {
            teamUseCase.deleteFollowedTeamUC(teamId).collectLatest { result ->
                Log.d("FollowVM", "deleteFollowedGTeam result: $result")
                _deleteFollowedTeamState.value = result

                if (result is Resource.Success) {
                    Log.d("FollowVM", "Refresh followed and all teams")
                    getFollowedTeams() // 👈 refresca la lista de teams seguidos
                    getSuggestedTeams(50)

                    // Forzar emitir valores nuevos aunque sean iguales (para test)
                    _teamsState.value = _teamsState.value
                    _followedTeamListState.value =  _followedTeamListState.value
                }
            }


        }
    }

    // Lista Filtrada de teams

    val filteredTeamsState: StateFlow<List<Team>> = combine(
        teamsState,
        followedTeamListState
    ) { allTeamsResource, followedTeamsResource ->
        val allPlayers = (allTeamsResource as? Resource.Success)?.data?.toList() ?: emptyList()
        val followedPlayers = (followedTeamsResource as? Resource.Success)?.data?.toList() ?: emptyList()

        val followedIds = followedPlayers.mapNotNull { it.id }.toSet()
        val filtered = allPlayers.filter { it.id !in followedIds }

        println("combine ejecutado: equipos=${allPlayers.size}, seguidos=${followedPlayers.size}, filtrados=${filtered.size}")
        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


}