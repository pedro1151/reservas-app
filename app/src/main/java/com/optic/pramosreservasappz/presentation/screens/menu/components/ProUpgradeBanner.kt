package com.optic.pramosreservasappz.presentation.screens.menu.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.authstate.AuthStateVM
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewMode
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground


@Composable
fun ProUpgradeBanner(
    navController: NavHostController
) {

    val buttonTextColor = Color.White // para cambiar el color


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GradientBackground)
            .drawBehind {
                drawRect(
                    color = Color.Black.copy(alpha = 0.05f)
                )
            }
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Prueba SalesGow PRO",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Obtén funcionalidades mejoradas, diferentes estilos, temas, estadisticas completas de tus ventas, historiales, añade mas colaboradores y mas.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(ClientScreen.Planes.route)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
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
                    text = "Prueba PRO",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = buttonTextColor
                )
            }
         //   Spacer(Modifier.height(4.dp))
            /*
            Text(
                text = "SABER MAS",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { }
            )

             */
        }
    }


}





