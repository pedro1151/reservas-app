package com.optic.pramosfootballappz.presentation.screens.player.players.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.domain.useCase.team.TeamUseCase
import com.optic.pramosfootballappz.domain.util.Resource
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
        getPlayers("", 1, 20)
    }

    private fun getPlayers(name:String, page:Int, size: Int) {
        viewModelScope.launch {
            teamUseCase.getPlayersUseCase(name, page, size).collectLatest { result ->
                Log.i("PlayerListViewModel", "Resultado: $result")
                _playersState.value = result
            }
        }
    }
}