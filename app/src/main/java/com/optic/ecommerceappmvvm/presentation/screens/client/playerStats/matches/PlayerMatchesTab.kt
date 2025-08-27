package com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.matches

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlayerMatchesTab(
    fixtureState: Resource<List<FixtureResponse>>,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            /*
            .border(

                width = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium.copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
            )
             */
            // .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            when (fixtureState) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        items(fixtureState.data ?: emptyList()) { fixture ->
                            MatchItem(
                                fixture = fixture,
                                navController = navController
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
            }
        }
    }
}