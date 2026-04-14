package com.optic.pramosreservasappz.presentation.screens.menu


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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewMode
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.Green

@Composable
fun PrincipalMenuDrawer(
    onDrawerClose: () -> Unit,
    navController: NavHostController
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.fillMaxWidth(0.82f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔴 BOTÓN CERRAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDrawerClose) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

            ProUpgradeBanner(
                navController = navController
            )

            Spacer(Modifier.height(10.dp))

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

            SaleMenuItem(
                onClick = { navController.navigate(ClientScreen.RapidSale.route)},
                title = "Venta Rapida",
                icon =  Icons.Default.Bolt
            )


            SaleMenuItem(
                onClick = { navController.navigate(ClientScreen.CompleteSaleStepOne.route)},
                title = "Venta Completa",
                icon =  Icons.Default.AddTask
            )

            SaleMenuItem(
                onClick = { navController.navigate(ClientScreen.Historial.route)
                },
                title = "Historial de tus Ventas",
                icon = Icons.Default.History
            )


            SaleMenuItem(
                onClick = { navController.navigate(ClientScreen.SaleStats.route)},
                title = "Mis Estadisticas",
                icon =  Icons.Default.BarChart
            )

            SaleMenuItem(
                onClick = { navController.navigate(ClientScreen.Clientes.route) },
                title = "Mis Clientes",
                icon =  Icons.Default.EmojiEmotions
            )

            SaleMenuItem(
                onClick = { navController.navigate(ClientScreen.Productos.route) },
                title = "Tus Productos/Servicios",
                icon =  Icons.Default.GifBox,
            )

            SaleMenuItem(
                onClick = { navController.navigate(ClientScreen.Mas.route) },
                title = "Configuracion",
                icon =  Icons.Default.Brightness7,
            )
        }
    }
}

@Composable
private fun ProUpgradeBanner(
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
            Spacer(Modifier.height(8.dp))
            Text(
                text = "SABER MAS",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { }
            )
        }
    }


}





