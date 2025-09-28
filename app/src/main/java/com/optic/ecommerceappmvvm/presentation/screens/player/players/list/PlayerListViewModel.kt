package com.optic.ecommerceappmvvm.presentation.screens.player.players.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import com.optic.ecommerceappmvvm.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerListViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _playersState = MutableStateFlow<Resource<List<Player>>>(Resource.Loading)
    val playersState: StateFlow<Resource<List<Player>>> = _playersState

    init {
        getPlayers()
    }

    private fun getPlayers() {
        viewModelScope.launch {
            teamUseCase.getPlayersUseCase().collectLatest { result ->
                Log.i("PlayerListViewModel", "Resultado: $result")
                _playersState.value = result
            }
        }
    }
}