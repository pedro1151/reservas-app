package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ReservationType {
    SERVICE, CLASS, EVENT, MEETING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationTypeBottomSheet(
    onDismiss: () -> Unit,
    onTypeSelected: (ReservationType) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(2.dp))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Servicio
            ReservationTypeOption(
                icon = Icons.Outlined.MiscellaneousServices,
                label = "Servicio",
                onClick = {
                    onTypeSelected(ReservationType.SERVICE)
                }
            )

            HorizontalDivider(
                color = Color(0xFFF0F0F0),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Clase
            ReservationTypeOption(
                icon = Icons.Outlined.School,
                label = "Clase",
                onClick = {
                    onTypeSelected(ReservationType.CLASS)
                }
            )

            HorizontalDivider(
                color = Color(0xFFF0F0F0),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Evento
            ReservationTypeOption(
                icon = Icons.Outlined.Event,
                label = "Evento",
                onClick = {
                    onTypeSelected(ReservationType.EVENT)
                }
            )

            HorizontalDivider(
                color = Color(0xFFF0F0F0),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Reunión única
            ReservationTypeOption(
                icon = Icons.Outlined.Videocam,
                label = "Reunión única",
                onClick = {
                    onTypeSelected(ReservationType.MEETING)
                }
            )

            Spacer(Modifier.height(16.dp))

            // Otras opciones (no son tipos de reserva)
            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Pago rápido
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* TODO */ }
                    )
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.CreditCard,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF424242)
                )
                Text(
                    text = "Pago rápido",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = { /* TODO */ },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Conectar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }

            // Miembro del equipo
            ReservationTypeOption(
                icon = Icons.Outlined.Group,
                label = "Miembro del equipo",
                onClick = { /* TODO */ }
            )

            // Cliente
            ReservationTypeOption(
                icon = Icons.Outlined.Person,
                label = "Cliente",
                onClick = { /* TODO */ }
            )

            // Compartir página de reservas
            ReservationTypeOption(
                icon = Icons.Outlined.Share,
                label = "Compartir página de reservas",
                onClick = { /* TODO */ }
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ReservationTypeOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF424242)
        )
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = Color.Black
        )
    }
}
