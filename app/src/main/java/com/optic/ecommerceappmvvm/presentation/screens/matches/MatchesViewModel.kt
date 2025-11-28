package com.optic.ecommerceappmvvm.presentation.screens.matches

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
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _fixtureTeamsState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureTeamsState : StateFlow<Resource<List<FixtureResponse>>> = _fixtureTeamsState

    // todos los fixrures para una fecha dada
    private val _fixtureDateState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureDateState : StateFlow<Resource<List<FixtureResponse>>>  = _fixtureDateState

    // limite de registros
    val limit = 50

    companion object {
        private const val TAG = "MatchesViewModel"
    }
    // FIXTURES de los equipos que sigue el usuario
    fun getFixtureFollowedTeams(season: Int , date: String ) {
        viewModelScope.launch {
            teamUseCase.getFixtureFollowedTeamsUC(season, date).collectLatest { result ->
                _fixtureTeamsState.value = result
            }
        }
    }

    fun getFixturesByDate(date: String, limit:Int) {
        resetStatesForNewDate()
        viewModelScope.launch {
            teamUseCase.getFixtureByDateUC(date, limit).collectLatest { result ->
                _fixtureDateState.value = result
            }
        }
    }


    fun resetStatesForNewDate() {
        _fixtureTeamsState.value = Resource.Loading
        _fixtureDateState.value = Resource.Loading
    }
}