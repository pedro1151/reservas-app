package com.optic.pramosfootballappz.presentation.screens.fixtures.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.components.BackTopBar

import com.optic.pramosfootballappz.presentation.components.ProgressBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FixtureDetailScreen(
    fixtureId: Int,
    navController: NavHostController

)
{
    val viewModel: FixtureDetailViewModel = hiltViewModel()

    // estados
    val fixtureState by viewModel.fixtureState.collectAsState()

    // Llamar a la función solo una vez al inicio
    LaunchedEffect(fixtureId) {
        viewModel.getFixtureById(fixtureId)
    }


    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        when (fixtureState) {
            is Resource.Loading -> {
                ProgressBar()
            }

            is Resource.Success -> {
                val data = (fixtureState as Resource.Success).data
                FixtureDetailContent(
                    paddingValues = paddingValues,
                    fixture = data,
                    navController
                )
            }

            is Resource.Failure -> {
                // mostrar error: result.message
            }

            else -> {}
        }

    }
}