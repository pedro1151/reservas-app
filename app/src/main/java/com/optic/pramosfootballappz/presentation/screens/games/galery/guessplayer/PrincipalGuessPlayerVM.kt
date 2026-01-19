package com.optic.pramosfootballappz.presentation.screens.games.galery.guessplayer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.pramosfootballappz.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.optic.pramosfootballappz.domain.model.trivias.guessplayer.GuessPlayerResponse
import com.optic.pramosfootballappz.domain.useCase.trivias.TriviasUseCase

@HiltViewModel
class PrincipalGuessPlayerVM @Inject constructor(
    private val triviasUseCase: TriviasUseCase
) : ViewModel() {

    private val _guessPlayerState = MutableStateFlow<Resource<GuessPlayerResponse>>(Resource.Loading)
    val guessPlayerState : StateFlow<Resource<GuessPlayerResponse>> = _guessPlayerState

    // administrar el puntaje
    private val _score = mutableStateOf(4000)
    val score: State<Int> = _score

    fun resetScore() {
        _score.value = 4000
    }

    fun onWrongGuess() {
        _score.value = (_score.value - 400).coerceAtLeast(0)
    }



    companion object {
        private const val TAG = "PrincipalGuessPlayerVM"
    }

    fun getGuessPlayer(topK: Int) {
        viewModelScope.launch {
            triviasUseCase.getGuessPlayerUC(topK).collect { result ->
                Log.d(TAG, "getGUessPlayer() -> result = $result")
                _guessPlayerState.value = result
            }
        }
    }


}