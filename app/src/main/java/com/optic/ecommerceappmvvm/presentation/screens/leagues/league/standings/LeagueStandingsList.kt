package com.optic.ecommerceappmvvm.presentation.screens.leagues.league.standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse

import com.optic.ecommerceappmvvm.domain.model.fixture.League
import com.optic.ecommerceappmvvm.domain.model.fixture.Team
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.FixtureDetailViewModel
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.LeagueViewModel


@Composable
fun LeagueStandingsList(
    paddingValues: PaddingValues,
    league: LeagueCompleteResponse,
    viewModel: LeagueViewModel
) {
    val standingState by viewModel.standingState.collectAsState()

    LaunchedEffect(league.id) {
        viewModel.getLeagueStandings(league.id)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (standingState) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                val standings = (standingState as Resource.Success).data ?: emptyList()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                   // shape = RoundedCornerShape(12.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        /*
                        item {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = league.logo,
                                    contentDescription = league.name,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = league.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                         */

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Rank", modifier = Modifier.width(30.dp), style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Equipo", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                                Text("J", style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(30.dp))
                                Text("DG", style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(30.dp))
                                Text("Pts", style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(30.dp))
                            }
                        }

                        items(standings.sortedBy { it.rank }) { standing ->
                            LeagueStandingItem(standing = standing)
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            StandingsLegendCard()
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }

            is Resource.Failure -> {
                Text(
                    text = (standingState as Resource.Failure).message ?: "Error loading standings",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}