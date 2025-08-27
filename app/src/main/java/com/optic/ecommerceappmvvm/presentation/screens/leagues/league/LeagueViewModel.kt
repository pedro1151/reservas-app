package com.optic.ecommerceappmvvm.presentation.screens.leagues.league

import android.util.Log
import androidx.lifecycle.ViewModel
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.response.DefaultResponse
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

    private val _leagueState = MutableStateFlow<Resource<League>>(Resource.Loading)
    val leagueState: StateFlow<Resource<League>> = _leagueState



    fun getLeagueById(leagueId:Int) {
        viewModelScope.launch {
            teamUseCase.getLeagueByIdUC(leagueId).collectLatest { result ->
                _leagueState.value = result
            }
        }
    }


}