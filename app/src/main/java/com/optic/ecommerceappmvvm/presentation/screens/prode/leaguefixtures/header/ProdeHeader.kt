package com.optic.ecommerceappmvvm.presentation.screens.prode.leaguefixtures.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse

@Composable
fun ProdeHeader(
    league: LeagueCompleteResponse,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // --- PRIMERA FILA: INFO + BOTÓN GUARDAR ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // 📌 IZQUIERDA: Logo + texto
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = league.logo,
                    contentDescription = "Foto de la liga",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        color = Color.White,
                        text = league.name ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = league.country?.name ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }
    }
}
