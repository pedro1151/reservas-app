package com.optic.ecommerceappmvvm.presentation.screens.matches.fixturesbydate

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.components.LoginLinkCard
import com.optic.ecommerceappmvvm.presentation.components.progressBar.CustomProgressBar
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.item.FixtureItem
import com.optic.ecommerceappmvvm.presentation.ui.theme.IconSecondaryColor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FixturesByDate(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    fixtureState: Resource<List<FixtureResponse>>,
    title: String = "Partidos"
) {

    when (fixtureState) {

        is Resource.Loading -> {
            // Loading opcional
        }

        is Resource.Success -> {

            val fixtures = fixtureState.data ?: emptyList()

            if (fixtures.isEmpty()) {
                Text(
                    text = "No hay partidos para la fecha.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                return
            }

            /**
             * ⛳ Optimización: recordar agrupamiento por liga
             * Se recalcula SOLO cuando cambia "fixtures".
             */
            val groupedByLeague = remember(fixtures) {
                fixtures
                    .filter { it.league?.id != null }
                    .groupBy { it.league!!.id!! }
            }

            /**
             * Un único LazyColumn 👉 virtualización real, sin LAG.
             */
            val expansionState = remember {
                mutableStateMapOf<Int, Boolean>()   // estado por liga
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 2.dp, horizontal = 1.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item{
                    LoginLinkCard(
                        navController = navController
                    )
                }

                groupedByLeague.forEach { (leagueId, leagueFixtures) ->

                    val league = leagueFixtures.first().league

                    item(key = "league-card-$leagueId") {

                        // Estado estable por liga
                        val expanded = expansionState.getOrPut(leagueId) { true }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                                //.padding(horizontal = 1.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {

                            // 🔹 Header clickable
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expansionState[leagueId] = !expanded }
                                    .padding(12.dp)
                            ) {

                                AsyncImage(
                                    model = league?.logo ?: "",
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )

                                Spacer(Modifier.width(10.dp))

                                Text(
                                    text = league?.name ?: "Liga desconocida",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                Icon(
                                    imageVector =
                                    if (expanded) Icons.Default.KeyboardArrowDown
                                    else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Expand/Collapse",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // 🔹 Contenido expandible
                            AnimatedVisibility(
                                visible = expanded,
                                enter = expandVertically(tween(200)),
                                exit = shrinkVertically(tween(200))
                            ) {

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {

                                    leagueFixtures.forEach { fixture ->
                                        FixtureItem(
                                            fixture = fixture,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                }

            }

        }

        is Resource.Failure -> {
            Text(
                text = "Error al cargar los Partidos",
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> Unit
    }
   // CustomProgressBar(isLoading = fixtureState is Resource.Loading)
}
