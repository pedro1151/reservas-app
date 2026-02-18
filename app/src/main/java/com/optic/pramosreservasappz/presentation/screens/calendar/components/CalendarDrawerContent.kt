package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewMode

@Composable
fun CalendarDrawerContent(
    currentViewMode: CalendarViewMode,
    onViewModeChange: (CalendarViewMode) -> Unit,
    onDrawerClose: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.fillMaxWidth(0.82f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ProUpgradeBanner()

            Spacer(Modifier.height(8.dp))

            Text(
                text = "MODO DE VISTA",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9E9E9E),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )

            ViewModeItem(
                icon = Icons.Outlined.ViewAgenda,
                title = "Agenda",
                isSelected = currentViewMode == CalendarViewMode.AGENDA,
                onClick = { onViewModeChange(CalendarViewMode.AGENDA) }
            )

            ViewModeItem(
                icon = Icons.Outlined.ViewDay,
                title = "Día",
                isSelected = currentViewMode == CalendarViewMode.DAY,
                onClick = { onViewModeChange(CalendarViewMode.DAY) }
            )

            ViewModeItem(
                icon = Icons.Outlined.ViewWeek,
                title = "3 Días",
                isSelected = currentViewMode == CalendarViewMode.THREE_DAYS,
                onClick = { onViewModeChange(CalendarViewMode.THREE_DAYS) }
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "MIS CALENDARIOS",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9E9E9E),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(19.dp),
                    color = Color(0xFFDDDDDD)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "PR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242)
                        )
                    }
                }
                Text(
                    text = "Pedro Ramos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 14.dp)
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF424242)
                )
                Text(
                    text = "Conectar calendario",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF424242)
                )
            }
        }
    }
}

@Composable
private fun ProUpgradeBanner() {

    val buttonTextColor = Color.White // para cambiar el color

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F5E9))
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Prueba Reserv Pro gratis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Obtén mejoradas funcionalidades, diferentes estilos, temas, recoradtorios y más.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF424242),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.FlightTakeoff,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = buttonTextColor
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Comenzar prueba gratuita",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = buttonTextColor
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Saber más",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF424242),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Composable
private fun ViewModeItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected) Color(0xFFF5F5F5) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = if (isSelected) Color.Black else Color(0xFF757575)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color(0xFF424242)
        )
    }
}
