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

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _fixtureTeamsState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureTeamsState : StateFlow<Resource<List<FixtureResponse>>> = _fixtureTeamsState

    private val _fixturesNoFollow = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixturesNoFollow : StateFlow<Resource<List<FixtureResponse>>> = _fixturesNoFollow

    private val _fixtureCountryState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureCountryState  : StateFlow<Resource<List<FixtureResponse>>> = _fixtureCountryState

    companion object {
        private const val TAG = "MatchesViewModel"
    }
    // FIXTURES de los equipos que sigue el usuario
    fun getFixtureFollowedTeams(season: Int = 2023, date: String = "2023-09-17") {
        viewModelScope.launch {
            teamUseCase.getFixtureFollowedTeamsUC(season, date).collectLatest { result ->
                _fixtureTeamsState.value = result
            }
        }
    }

    // FIXTURES de los equipos no seguidos
    fun getNoFixtureFollowedTeams(season: Int = 2023, date: String = "2023-09-17") {
        viewModelScope.launch {
            teamUseCase.getNoFollowFixturesUC(season, date).collectLatest { result ->
                Log.d(TAG, "getFixtureFollowedTeams() -> result = $result")
                _fixturesNoFollow.value = result
            }
        }
    }

    // recupera los fixtures acorde al pais del usuario conectado, mediante su IP,
    // ESTO lo hace internamente el backend
    fun getFixturesByCountry(season: Int = 2023, date: String = "2023-09-17") {
        viewModelScope.launch {
            teamUseCase.getCountryFixturesUC(season, date).collectLatest { result ->
                Log.d(TAG, "getFixturesByCountry(() -> result = $result")
                _fixtureCountryState.value = result
            }
        }
    }
}