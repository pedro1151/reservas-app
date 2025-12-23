package com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.player.PlayerComplete
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.util.Resource


@Composable
fun PlayerProfileTab(
    player: Resource<PlayerComplete>
) {
    when (player) {

        is Resource.Loading -> {
            // 🟡 Estado loading
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is Resource.Failure -> {
            // 🔴 Estado error
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = player.message ?: "Error al cargar información del jugador",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        is Resource.Success -> {
            // 🟢 Estado success
            val data = player.data ?: return

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxSize(), // ← Esto es CLAVE para que use todo el alto
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileInfoItem(
                            value = data.age?.toString() ?: "0",
                            label = "Edad"
                        )
                        ProfileInfoItem(
                            value = data.height ?: "-",
                            label = "Altura"
                        )
                        ProfileInfoItem(
                            value = data.weight ?: "-",
                            label = "Peso"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileInfoItemWithIcon(
                            value = data.nationality ?: "-",
                            label = "Nacionalidad"
                        )
                        ProfileInfoItem(
                            value = if (data.injured == true) "Sí" else "No",
                            label = "Lesionado"
                        )
                        ProfileInfoItem(
                            value = data.birthDate ?: "-",
                            label = "Nacimiento"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileInfoItem(
                            value = data.birthPlace ?: "-",
                            label = "Lugar de Nacimiento"
                        )
                        ProfileInfoItem(
                            value = data.birthCountry ?: "-",
                            label = "País de Nacimiento"
                        )
                    }
                }
            }
        }

        else -> {}
    }
}

@Composable
fun ProfileInfoItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun ProfileInfoItemWithIcon(value: String, label: String ){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
