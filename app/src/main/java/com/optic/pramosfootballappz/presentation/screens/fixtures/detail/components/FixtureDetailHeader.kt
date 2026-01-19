package com.optic.pramosfootballappz.presentation.screens.fixtures.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.optic.pramosfootballappz.domain.model.fixture.FixtureResponse

@Composable
fun FixtureDetailHeader(
    fixture: FixtureResponse,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Equipo local
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = fixture.teamHome.logo,
                    contentDescription = fixture.teamHome.name,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = fixture.teamHome.name,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            // Resultado en el medio
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${fixture.goalsHome} - ${fixture.goalsAway}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = fixture.statusLong,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Equipo visitante
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = fixture.teamAway.logo,
                    contentDescription = fixture.teamAway.name,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = fixture.teamAway.name,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}