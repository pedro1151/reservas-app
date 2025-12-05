package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedTeam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.presentation.components.follow.FollowButton
import com.optic.ecommerceappmvvm.presentation.components.follow.UnFollowButton
import com.optic.ecommerceappmvvm.presentation.components.follow.UnFollowButtonAlternative
import com.optic.ecommerceappmvvm.presentation.navigation.Graph


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FinalTeamList(
    followedTeams: List<Team>,
    suggestedTeams: List<Team>,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {},
    onUnFollowClick: (Int) -> Unit = {},
    paddingValues: PaddingValues
) {

    val colors = listOf(
        Color(0xFFBB86FC), Color(0xFF03DAC5), Color(0xFFFFB74D),
        Color(0xFF4CAF50), Color(0xFFE91E63), Color(0xFF2196F3),
        Color(0xFFFF5722), Color(0xFF9C27B0), Color(0xFF00BCD4),
        Color(0xFFFFC107), Color(0xFF8BC34A), Color(0xFFE040FB)
    )
    fun colorForIndex(index: Int) = colors[index % colors.size]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        // ---------------------------------------------------------
        // 🔥 1. SECCIÓN: TEAMS SEGUIDOS CON ANIMACIONES
        // ---------------------------------------------------------

        items(followedTeams.chunked(2)) { row ->

            AnimatedVisibility (
                visible = row.isNotEmpty(),
                modifier = Modifier.animateItemPlacement()
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    row.forEachIndexed { index, team ->

                        AnimatedVisibility(
                            visible = true,
                            modifier = Modifier
                                .weight(1f)
                                .animateItemPlacement()
                        ) {
                            FollowedTeamItem(
                                team = team,
                                backgroundColor = colorForIndex(index),
                                onUnFollowClick = onUnFollowClick,
                                navController = navController,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (row.size < 2) {
                        Spacer(
                            Modifier
                                .weight(1f)
                                .animateItemPlacement()
                        )
                    }
                }
            }
        }


        // ---------------------------------------------------------
        // 📌 SEPARADOR MODERNO ENTRE LISTAS
        // ---------------------------------------------------------

        item {
            AnimatedVisibility(
                visible = true,
                modifier = Modifier.animateItemPlacement()
            ) {
                SectionDividerLabel("Equipos sugeridos")
            }
        }


        // ---------------------------------------------------------
        // 🔥 2. SECCIÓN: TEAMS SUGERIDOS CON ANIMACIÓN
        // ---------------------------------------------------------

        items(
            suggestedTeams,
            key = { it.id ?: 0 }
        ) { team ->

            AnimatedVisibility(
                visible = true,
                modifier = Modifier.animateItemPlacement()
            ) {
                SuggestedTeamItem(
                    team = team,
                    navController = navController,
                    onFollowClick = onFollowClick
                )
            }
        }
    }
}


