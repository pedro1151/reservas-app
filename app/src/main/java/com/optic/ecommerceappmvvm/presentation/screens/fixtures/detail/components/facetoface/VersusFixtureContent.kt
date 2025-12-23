package com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.components.facetoface

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.FixtureDetailViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.item.FixtureItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VersusFixtureContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    fixture: FixtureResponse,
    title: String = "Siguiendo"
) {

    val viewModel: FixtureDetailViewModel = hiltViewModel()
    val versusFixtureState by viewModel.versusFixtureState.collectAsState()

    LaunchedEffect(
        fixture.teamHome.id,
        fixture.teamAway.id,
        fixture.league.id,
        fixture.leagueSeason
    ) {
        viewModel.getVersusFixture(
            fixture.teamHome.id,
            fixture.teamAway.id,
            fixture.league.id,
            fixture.leagueSeason
        )
    }
        var expanded by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 1.dp, vertical = 8.dp),
        ) {

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                when (versusFixtureState) {
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }

                    is Resource.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                           // .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items((versusFixtureState as Resource.Success).data?: emptyList()) { fixture ->
                                FixtureItem(
                                    fixture = fixture,
                                    navController = navController,
                                    showInfoExtra = true
                                )
                            }
                        }
                    }

                    is Resource.Failure -> {
                        Text(
                            text = "Error al cargar los Partidos",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    else -> {}
                }
            }
        }

}
