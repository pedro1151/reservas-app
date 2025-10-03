package com.optic.ecommerceappmvvm.presentation.screens.games

import androidx.lifecycle.ViewModel
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import com.optic.ecommerceappmvvm.domain.model.trivias.game.GameResponse
import com.optic.ecommerceappmvvm.domain.useCase.trivias.TriviasUseCase

@HiltViewModel
class GameViewModel @Inject constructor(
    private val triviasUseCase: TriviasUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow<Resource<List<GameResponse>>>(Resource.Loading)
    val gameState: StateFlow<Resource<List<GameResponse>>> = _gameState

    companion object {
        private const val TAG = "GameViewModel"
    }

    fun getGames() {
        viewModelScope.launch {
            triviasUseCase.getGamesUC().collect { result ->
                Log.d(TAG, "getGames() -> result = $result")
                _gameState.value = result
            }
        }
    }
}