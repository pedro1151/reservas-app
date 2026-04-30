package com.optic.pramosreservasappz.presentation.screens.menu


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.authstate.AuthStateVM
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.menu.components.ProUpgradeBanner
import com.optic.pramosreservasappz.presentation.screens.menu.components.SaleMenuItem
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import com.optic.pramosreservasappz.presentation.util.getInitials

@Composable
fun PrincipalMenuDrawer(
    onDrawerClose: () -> Unit,
    navController: NavHostController,
    authStateVM: AuthStateVM = hiltViewModel()
) {

    val sessionData by authStateVM.sessionData.collectAsState()

    val businessId = sessionData.businessId
    val userEmail = sessionData.email
    val planCode = sessionData.planCode

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.fillMaxWidth(0.82f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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

            Spacer(Modifier.height(5.dp))

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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(19.dp),
                    color = Color(0xFFDDDDDD)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = getInitials(userEmail),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242)
                        )
                    }
                }
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 14.dp)
                )
            }

            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {

                //   Spacer(Modifier.height(0.dp))

                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.MyBusiness.route) },
                        title = "Mi Negocio",
                        icon = Icons.Default.Favorite
                    )
                }

                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.CompleteSaleStepTwo.route) },
                        title = "Venta Rapida",
                        icon = Icons.Default.Bolt
                    )
                }

                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.CompleteSaleStepOne.route) },
                        title = "Venta Completa",
                        icon = Icons.Default.AddTask
                    )
                }

                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.BusinessMembers.route) },
                        title = "Tus Colaboradores",
                        icon = Icons.Default.Person,
                    )
                }

                item {
                    SaleMenuItem(
                        onClick = {
                            navController.navigate(ClientScreen.Historial.route)
                        },
                        title = "Transacciones",
                        icon = Icons.Default.History
                    )
                }

                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.SaleStats.route) },
                        title = "Mis Estadisticas",
                        icon = Icons.Default.BarChart
                    )
                }

                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.Clientes.route) },
                        title = "Mis Clientes",
                        icon = Icons.Default.EmojiEmotions
                    )
                }
                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.Productos.route) },
                        title = "Tus Productos/Servicios",
                        icon = Icons.Default.GifBox,
                    )
                }

                item {
                    SaleMenuItem(
                        onClick = { navController.navigate(ClientScreen.Mas.route) },
                        title = "Configuracion",
                        icon = Icons.Default.Brightness7,
                    )
                }
            }
        }
    }


}




