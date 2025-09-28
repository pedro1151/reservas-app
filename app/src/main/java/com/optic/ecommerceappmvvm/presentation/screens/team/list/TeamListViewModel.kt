package com.optic.ecommerceappmvvm.presentation.screens.team.list

import androidx.lifecycle.ViewModel
import com.optic.ecommerceappmvvm.domain.useCase.team.TeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class TeamListViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase
) : ViewModel() {

    private val _teamsState = MutableStateFlow<Resource<List<Team>>>(Resource.Loading)
    val teamsState: StateFlow<Resource<List<Team>>> = _teamsState

    init {
        getTeams()
    }

    private fun getTeams() {
        viewModelScope.launch {
            teamUseCase.getallTeamUseCase().collectLatest { result ->
                _teamsState.value = result
            }
        }
    }
}