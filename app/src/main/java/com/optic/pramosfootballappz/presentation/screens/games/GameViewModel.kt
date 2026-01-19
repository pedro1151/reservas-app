package com.optic.pramosfootballappz.presentation.screens.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.pramosfootballappz.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.optic.pramosfootballappz.domain.model.trivias.game.GameResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreCreate
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreResponse
import com.optic.pramosfootballappz.domain.useCase.trivias.TriviasUseCase

@HiltViewModel
class GameViewModel @Inject constructor(
    private val triviasUseCase: TriviasUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow<Resource<List<GameResponse>>>(Resource.Loading)
    val gameState: StateFlow<Resource<List<GameResponse>>> = _gameState

    private val _dificultyState = MutableStateFlow<Resource<List<GameDificulty>>>(Resource.Loading)
    val dificultyState : StateFlow<Resource<List<GameDificulty>>> = _dificultyState

    //score
    // 📌 Estado local del score actual (para Compose)
    var gameScoreState by mutableStateOf(
        GameScoreCreate(
            gameCode = "",
            dificulty = "",
            score = 0,
            createdBy = null
        )
    )
        private set
    private val _scoreState = MutableStateFlow<Resource<GameScoreResponse>>(Resource.Loading)
    val scoreState : StateFlow<Resource<GameScoreResponse>> = _scoreState


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
    fun getDificultys() {
        viewModelScope.launch {
            triviasUseCase.getDificultysUC().collect { result ->
                Log.d(TAG, "getDificultys() -> result = $result")
                _dificultyState.value = result
            }
        }
    }

    fun saveScore(gameScore: GameScoreCreate) {
        viewModelScope.launch {
            triviasUseCase.createGameScoreUC(gameScore) .collect { result ->
                Log.d(TAG, "saveScore -> result = $result")
                _scoreState.value = result
            }
        }
    }

}