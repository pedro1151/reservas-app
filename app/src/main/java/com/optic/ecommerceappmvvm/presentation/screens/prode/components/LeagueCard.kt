package com.optic.ecommerceappmvvm.presentation.screens.prode.components

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.R
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.presentation.components.follow.FollowButton
import com.optic.ecommerceappmvvm.presentation.components.follow.UnFollowButton
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocalizedContext

val ActiveBg = Color(0xFF1E2228)          // grafito profundo
val ActiveAccent = Color(0xFF4DA3FF)     // azul premium (FotMob vibe)
val ActiveText = Color.White
val ActiveSubtle = ActiveAccent.copy(alpha = 0.15f)
@Composable
fun LeagueCard(
    league: League,
    isParticipaed: Boolean = false,
    onFollowClick: () -> Unit,
    navController: NavHostController
) {
    val containerColor =
        if (isParticipaed) ActiveBg else MaterialTheme.colorScheme.primaryContainer

    // para idioma
    val localizedContext = LocalizedContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clickable {
                league.id?.let {
                    navController.navigate("${Graph.PRODE}/$it")
                }
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 ACCENT BAR
            if (isParticipaed) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(ActiveAccent)
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = league.logo,
                        contentDescription = league.name,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = league.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                fontWeight = if (isParticipaed) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isParticipaed) ActiveText else MaterialTheme.colorScheme.onSurface
                            )
                        )

                        if (isParticipaed) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = localizedContext.getString(R.string.prode_screen_participando_subtitle),
                                style = MaterialTheme.typography.labelSmall,
                                color = ActiveAccent
                            )
                        }
                    }
                }

                ParticipateButton(
                    isParticipaed = isParticipaed,
                    onClick = {
                        league.id?.let {
                            navController.navigate("${Graph.PRODE}/$it")
                        }
                    },
                    localizedContext = localizedContext
                )
            }
        }
    }
}

@Composable
fun ParticipateButton(
    isParticipaed: Boolean,
    onClick: () -> Unit,
    localizedContext: Context
) {
    val textColor = if (isParticipaed) ActiveAccent else MaterialTheme.colorScheme.onSurface

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = if (isParticipaed) localizedContext.getString(R.string.prode_screen_ver_button)
            else localizedContext.getString(R.string.prode_screen_participar_button),
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(16.dp)
        )
    }
}

