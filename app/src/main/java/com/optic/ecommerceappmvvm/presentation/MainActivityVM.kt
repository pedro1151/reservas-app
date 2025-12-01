package com.optic.ecommerceappmvvm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            teamRepository.precacheFixturesAroundToday()
        }
    }
}