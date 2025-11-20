package com.optic.ecommerceappmvvm.presentation.screens.leagues.league

import android.util.Log
import androidx.lifecycle.ViewModel
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LeagueViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _leagueState = MutableStateFlow<Resource<LeagueCompleteResponse>>(Resource.Loading)
    val leagueState: StateFlow<Resource<LeagueCompleteResponse>> = _leagueState

    //standings ( Clasificaciones )
    private val _standingState = MutableStateFlow<Resource<List<StandingResponse>>>(Resource.Loading)
    val standingState: StateFlow<Resource<List<StandingResponse>>> = _standingState

    private val _fixtureLeagueState = MutableStateFlow<Resource<List<FixtureResponse>>>(Resource.Loading)
    val fixtureLeagueState: StateFlow<Resource<List<FixtureResponse>>> = _fixtureLeagueState



    // 👉 Guardamos la última season aquí
    var latestSeason: Int? = null

    fun getLeagueById(leagueId:Int) {
        viewModelScope.launch {
            teamUseCase.getLeagueByIdUC(leagueId).collectLatest { result ->
                _leagueState.value = result

                // 👇 Cuando tenemos la liga, extraemos la última season
                if (result is Resource.Success) {
                    latestSeason = result.data?.seasons
                        ?.maxByOrNull { it.year }  // Último año
                        ?.year
                    /*if (latestSeason!! > 2023) {
                        latestSeason = 2023
                    }

                     */
                }
            }
        }
    }



    fun getLeagueStandings(leagueId: Int) {
        Log.d("StandingViewModel", "getLeagueStandings called with leagueId=$leagueId")
        val season = latestSeason
        Log.d("StandingViewModel", "getLeagueStandings called with leagueId=$leagueId, season=$season")

        if (season == null) {
            _standingState.value = Resource.Failure("No season available for this league")
            return
        }
        viewModelScope.launch {
            try {
                teamUseCase.getLeagueStandingsUC(leagueId, season).collectLatest { result ->
                    Log.d("StandingViewModel", "Result received standings: $result")
                    _standingState.value = result
                }
            } catch (e: Exception) {
                Log.e("StandingViewModel", "Error fetching standings", e)
                _standingState.value = Resource.Failure(e.message ?: "Unknown error")
            }
        }
    }


    fun getLeagueFixture(leagueId: Int, season:Int) {
        viewModelScope.launch {
            teamUseCase.getFixtureLeagueUC(leagueId, season) .collectLatest { result ->
                _fixtureLeagueState.value = result
            }
        }
    }


}