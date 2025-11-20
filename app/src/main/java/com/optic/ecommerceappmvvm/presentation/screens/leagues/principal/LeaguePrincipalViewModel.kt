package com.optic.ecommerceappmvvm.presentation.screens.leagues.principal

import android.util.Log
import androidx.lifecycle.ViewModel
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LeaguePrincipalViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _leaguesState = MutableStateFlow<Resource<List<League>>>(Resource.Loading)
    val leaguesState: StateFlow<Resource<List<League>>> = _leaguesState

    //LIGAS SEGUIDAS
    private val _followedLeaguesListState = MutableStateFlow<Resource<List<League>>>(Resource.Loading)
    val followedLeaguesListState: StateFlow<Resource<List<League>>> = _followedLeaguesListState

    // estado para el Crear un team seguido
    private val _createfollowedLeagueState = MutableStateFlow<Resource<FollowedLeagueResponse>>(Resource.Loading)
    val createfollowedLeagueState : StateFlow<Resource<FollowedLeagueResponse>> = _createfollowedLeagueState

    // estado para delete team seguido
    private val _deleteFollowedLeagueState = MutableStateFlow<Resource<DefaultResponse>>(Resource.Loading)
    val deleteFollowedLeagueState : StateFlow<Resource<DefaultResponse>> = _deleteFollowedLeagueState



    init {
        getLeagues()
        getFollowedLeagues()

    }
    fun getLeagues(name: String = "", type: String = "", countryName: String = "") {
        viewModelScope.launch {
            teamUseCase.getLeaguesUseCase(name, type, countryName).collectLatest { result ->
                _leaguesState.value = result
            }
        }
    }


    fun getFollowedLeagues() {
        viewModelScope.launch {
            teamUseCase.getFollowedLeaguesUC().collectLatest { result ->
                _followedLeaguesListState.value = result
            }
        }
    }


    fun createFollowedLeague(leagueId: Int) {
        viewModelScope.launch {
            teamUseCase.createFollowedLeagueUC(leagueId).collectLatest { result ->
                Log.d("FollowVM", "createFollowedLeague result: $result")
                _createfollowedLeagueState.value = result

                if (result is Resource.Success) {
                    Log.d("FollowVM", "Refresh followed and all teams")
                    getFollowedLeagues() // 👈 refresca la lista de teams seguidos
                    getLeagues()

                    // Forzar emitir valores nuevos aunque sean iguales (para test)
                    _leaguesState.value = _leaguesState.value
                    _followedLeaguesListState.value =  _followedLeaguesListState.value
                }
            }


        }
    }

    fun deleteFollowedLeague(leagueId: Int) {
        viewModelScope.launch {
            teamUseCase.deleteFollowedLeagueUC(leagueId).collectLatest { result ->
                Log.d("FollowVM", "deleteFollowedLeague result: $result")
                _deleteFollowedLeagueState.value = result

                if (result is Resource.Success) {
                    Log.d("FollowVM", "Refresh followed and all leagues")
                    getFollowedLeagues() // 👈 refresca la lista de leagues seguidos
                    getLeagues()

                    // Forzar emitir valores nuevos aunque sean iguales (para test)
                    _leaguesState.value = _leaguesState.value
                    _followedLeaguesListState.value =  _followedLeaguesListState.value
                }
            }


        }
    }

    // Lista Filtrada para leagues

    val filteredLeaguesState: StateFlow<List<League>> = combine(
        leaguesState,
        followedLeaguesListState
    ) { allLeaguesResource, followedLeaguesResource ->
        val allLeagues = (allLeaguesResource as? Resource.Success)?.data?.toList() ?: emptyList()
        val followedLeagues = (followedLeaguesResource as? Resource.Success)?.data?.toList() ?: emptyList()

        val followedIds = followedLeagues.mapNotNull { it.id }.toSet()
        val filtered = allLeagues.filter { it.id !in followedIds }

        println("combine ejecutado: ligas=${allLeagues.size}, seguidas=${followedLeagues.size}, filtrados=${filtered.size}")
        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

}