package com.optic.ecommerceappmvvm.presentation.screens.matches


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.datetopbar.MatchesDateTopBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.folllowfixtures.FollowFixtureList
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight
import java.time.LocalDate
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.home.components.ClientBottomBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.countryfixtures.CountryFixtures
import com.optic.ecommerceappmvvm.presentation.screens.matches.fixturesbydate.FixturesByDate
import com.optic.ecommerceappmvvm.presentation.screens.matches.nofollowfixtures.NoFollowFixtures
import com.optic.ecommerceappmvvm.presentation.ui.theme.IconSecondaryColor

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MatchesScreen(
    navController: NavHostController,
    isAuthenticated : Boolean
) {
    val viewModel: MatchesViewModel = hiltViewModel()
    val fixtureState by viewModel.fixtureTeamsState.collectAsState()
    val fixtureStateCountry by viewModel.fixtureCountryState.collectAsState()
    //val fixtureStateNoFollow by viewModel.fixturesNoFollow.collectAsState()
    val fixtureStateDate by viewModel.fixtureDateState.collectAsState()

    // fecha real de hoy
    val today = LocalDate.now()

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val fakeToday = LocalDate.of(2023, 9, 17)

    // estado para la fecha seleccionada
    var selectedDate by remember { mutableStateOf(fakeToday) }
    var previousDate by remember { mutableStateOf(fakeToday) }

    // cargar datos iniciales
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getFixtureFollowedTeams(
            season = 2025,
            date = today.toString()
        )
    }

    // cargar datos iniciales no follow
    /*
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getNoFixtureFollowedTeams(
            season = 2025,
            date = today.toString()
        )
    }

     */

    // cargar datos iniciales
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getFixturesByCountry(
            season = 2025,
            date = today.toString()
        )
    }

    // cargar datos iniciales
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getFixturesByDate(
            date = today.toString(),
            limit = 100
        )
    }


    Scaffold(
        topBar = {
            Column {
                PrimaryTopBar(
                    navController = navController,
                    title = "UNIFOT"
                )
                MatchesDateTopBar { newDate ->
                    previousDate = selectedDate
                    selectedDate = newDate
                    viewModel.getFixtureFollowedTeams(
                        season = 2025,
                        date = newDate.toString()
                    )
                   /*
                    viewModel.getNoFixtureFollowedTeams(
                        season = 2025,
                        date = newDate.toString()
                    )

                    */

                    viewModel.getFixturesByCountry(
                        season = 2025,
                        date = newDate.toString()
                    )

                    viewModel.getFixturesByDate(
                        date = newDate.toString(),
                        limit = 100
                    )


                }
            }
        },
        containerColor = GreyLight
    ) { paddingValues ->

        AnimatedContent(
            targetState = selectedDate,
            transitionSpec = {
                if (targetState > initialState) {
                    // futura → desliza hacia la izquierda
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                } else {
                    // pasada → desliza hacia la derecha
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            },
            label = "FixturesAnimation"
        ) { _ ->
            Column(
                modifier = Modifier.fillMaxSize())
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()   // ocupa todo el alto y ancho disponible
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.Top // los ítems empiezan arriba

                ) {

                    if (isAuthenticated) {
                        item {
                            FollowFixtureList(
                                navController = navController,
                                fixtureState = fixtureState
                            )
                        }

                        item {
                            FixturesByDate(
                                navController = navController,
                                fixtureState = fixtureStateDate
                            )
                        }
                        /*
                        item {
                            NoFollowFixtures(
                                navController = navController,
                                fixtureState = fixtureStateNoFollow
                            )
                        }

                         */

                        item {
                            CountryFixtures(
                                navController = navController,
                                fixtureState = fixtureStateCountry,
                                modifier = Modifier.fillParentMaxHeight() // 👈 ocupa todo el resto
                            )
                        }
                    } else {

                        item {
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(horizontal = 1.dp, vertical = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center  // 👈 centra los elementos en la fila
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.IconSecondaryColor
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Logueate para empezar a seguir equipos",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center,   // 👈 centrado
                                        ),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }

                        item {
                            FixturesByDate(
                                navController = navController,
                                fixtureState = fixtureStateDate,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
