package com.optic.ecommerceappmvvm.presentation.screens.player.playerStats

import android.util.Log
import androidx.lifecycle.ViewModel
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.player.PlayerComplete
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class PlayerStatsViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _playerStatsState =  MutableStateFlow<Resource<PlayerWithStats>>(Resource.Loading)
    val playerStatsState: StateFlow<Resource<PlayerWithStats>> = _playerStatsState

    private val _playerTeamsState =  MutableStateFlow<Resource<PlayerTeamsResponse>>(Resource.Loading)
    val playerTeamsState: StateFlow<Resource<PlayerTeamsResponse>> = _playerTeamsState

    //last team
    private val _playerLastTeamState =  MutableStateFlow<Resource<PlayerLastTeamResponse>>(Resource.Loading)
    val playerLastTeamState : StateFlow<Resource<PlayerLastTeamResponse>> = _playerLastTeamState

    // fixture para el team del jugador
    private val _fixtureTeamsState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureTeamsState : StateFlow<Resource<List<FixtureResponse>>> = _fixtureTeamsState

    // player por id
    private val _singlePlayerState = MutableStateFlow<Resource<PlayerComplete>>(Resource.Loading)
    val singlePlayerState : StateFlow<Resource<PlayerComplete>> = _singlePlayerState





    fun getPlayerStats(playerId: Int) {
        viewModelScope.launch {
            teamUseCase.getPlayerStatsUseCase(playerId).collectLatest { result ->
                _playerStatsState.value = result
            }
        }
    }

    fun getPlayerPorId(playerId: Int) {
        viewModelScope.launch {
            teamUseCase.getPlayerPorIdUC(playerId).collectLatest { result ->
                _singlePlayerState.value = result
            }
        }
    }

    //trayectoria
    fun getPlayerTeams(playerId: Int) {
        viewModelScope.launch {
            try {
                teamUseCase.getPlayerTeamsUC(playerId).collectLatest { result ->
                    Log.d("PlayerScreen", "Result received: $result")
                    _playerTeamsState.value = result
                }
            } catch (e: Exception) {
                Log.e("PlayerScreen", "Error fetching trayectoria", e)
                _playerTeamsState.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }


    //Last Team
    fun getPlayerLastTeam(playerId: Int) {
        viewModelScope.launch {
            try {
                teamUseCase.getPlayerLastTeamUC(playerId).collectLatest { result ->
                    Log.d("PlayerScreen", "Result received last team: $result")
                    _playerLastTeamState.value = result
                }
            } catch (e: Exception) {
                Log.e("PlayerScreen", "Error fetching last team", e)
                _playerLastTeamState.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }

    // fixture para un team
    fun getFixtureTeam(teamId: Int) {
        viewModelScope.launch {
            teamUseCase.getFixtureTeamUC(teamId).collectLatest { result ->
                _fixtureTeamsState.value = result
            }
        }
    }

}