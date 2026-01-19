package com.optic.pramosfootballappz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosfootballappz.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            teamRepository.precacheFixturesAroundToday()
            teamRepository.precacheAllLeagues()
           // teamRepository.precacheAllPlayers()
           // teamRepository.precacheAllTeams()
        }
    }



    private var fixturesJob: Job? = null

    init {
        startTodayFixturesUpdater()
    }

    private fun startTodayFixturesUpdater() {
        fixturesJob?.cancel()

        fixturesJob = viewModelScope.launch {
            while (isActive) {
                val today = LocalDate.now().toString() // YYYY-MM-DD
                val yesterday = LocalDate.now().minusDays(1).toString()


                teamRepository.updateFixturesByDate(
                    date = today,
                    limit = 1000
                ).collect {
                    // No necesitas hacer nada
                    // El cache se pisa solo
                }

                teamRepository.updateFixturesByDate(
                    date = yesterday,
                    limit = 1000
                ).collect {
                    // No necesitas hacer nada
                    // El cache se pisa solo
                }

                delay(5 * 60 * 1000L) // ⏰ 5 minutos
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fixturesJob?.cancel()
    }


}