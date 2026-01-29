package com.optic.pramosreservasappz.presentation.screens.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen

@Composable
fun ServiceCard(
    service: ServiceResponse,
    navController: NavHostController
) {
        var serviceColor = MaterialTheme.colorScheme.primary
        if (!service.color.isNullOrBlank()) {
            serviceColor = Color(android.graphics.Color.parseColor(service.color))
        }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {
                navController.navigate(
                    ClientScreen.CreateServicio.createRoute(
                        serviceId = service.id,
                        editable = true
                    )
                )
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            // ✅ Barra lateral izquierda de color
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(serviceColor)
            )

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Nombre principal
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Descripción
                if (!service.description.isNullOrBlank()) {
                    Text(
                        text = service.description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Precio y duración
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    service.price?.let {
                        Text(
                            text = "$${"%.2f".format(it)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                        )
                    }

                    Text(
                        text = "${service.durationMinutes} min",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color =MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}
