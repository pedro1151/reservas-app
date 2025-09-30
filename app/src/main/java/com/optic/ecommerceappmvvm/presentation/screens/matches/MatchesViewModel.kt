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

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _fixtureTeamsState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureTeamsState : StateFlow<Resource<List<FixtureResponse>>> = _fixtureTeamsState

    private val _fixtureCountryState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureCountryState  : StateFlow<Resource<List<FixtureResponse>>> = _fixtureCountryState


    // FIXTURES de los equipos que sigue el usuario
    fun getFixtureFollowedTeams(season: Int = 2023, date: String = "2023-09-17") {
        viewModelScope.launch {
            teamUseCase.getFixtureFollowedTeamsUC(season, date).collectLatest { result ->
                _fixtureTeamsState.value = result
            }
        }
    }

    // recupera los fixtures acorde al pais del usuario conectado, mediante su IP,
    // ESTO lo hace internamente el backend
    fun getFixturesByCountry() {
        viewModelScope.launch {
            teamUseCase.getCountryFixturesUC().collectLatest { result ->
                _fixtureCountryState.value = result
            }
        }
    }
}