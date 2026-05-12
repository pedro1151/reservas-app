package com.optic.pramozventicoappz.presentation.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.menu.upgradebanner.ProUpgradeBanner
import com.optic.pramozventicoappz.presentation.screens.menu.components.SaleMenuItem
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import com.optic.pramozventicoappz.presentation.util.getInitials

@Composable
fun PrincipalMenuDrawer(
    onDrawerClose: () -> Unit,
    navController: NavHostController,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()

    val userEmail = sessionData.email
    val planCode = sessionData.planCode

    fun navigateAndClose(route: String) {
        onDrawerClose()
        navController.navigate(route)
    }

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth(0.86f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 12.dp, top = 14.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Menú",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = (-0.4).sp,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDrawerClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar menú",
                        tint = TextSecondary
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 6.dp,
                    bottom = 110.dp
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        UserHeaderCard(
                            userEmail = userEmail,
                            planCode = planCode
                        )
                    }
                }

                // 🔥 FULL WIDTH
                item {
                    ProUpgradeBanner(navController = navController)
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        MenuSectionTitle("Negocio")
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.MyBusiness.route) },
                            title = "Mi negocio",
                            subtitle = "Datos, tu logo y configuración",
                            icon = Icons.Default.Storefront
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.Productos.route) },
                            title = "Productos y servicios",
                            subtitle = "Administra tu catálogo",
                            icon = Icons.Default.GifBox
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.BusinessMembers.route) },
                            title = "Colaboradores",
                            subtitle = "Tu equipo y permisos",
                            icon = Icons.Default.Person
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        MenuDivider()
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        MenuSectionTitle("Ventas")
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.CompleteSaleStepTwo.route) },
                            title = "Venta rápida",
                            subtitle = "Cobra en pocos pasos",
                            icon = Icons.Default.Bolt
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.CompleteSaleStepOne.route) },
                            title = "Venta completa",
                            subtitle = "Para ventas mas detalladas",
                            icon = Icons.Default.AddTask
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.Historial.route) },
                            title = "Tus ventas",
                            subtitle = "Historial de tus ventas",
                            icon = Icons.Default.History
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.ReciboConfig.route) },
                            title = "Tu Recibo",
                            subtitle = "Personaliza tu recibo",
                            icon = Icons.Default.Receipt
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        MenuDivider()
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        MenuSectionTitle("Gestión")
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.SaleStats.route) },
                            title = "Mis estadísticas",
                            subtitle = "Métricas y rendimiento",
                            icon = Icons.Default.BarChart,
                            isPro = true
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.Clientes.route) },
                            title = "Mis clientes",
                            subtitle = "Contactos y compradores",
                            icon = Icons.Default.Star
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.Planes.route) },
                            title = "Planes PRO",
                            subtitle = "Mejora tu cuenta",
                            icon = Icons.Default.WorkspacePremium,
                            isPro = true
                        )
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        MenuDivider()
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        MenuSectionTitle("Cuenta")
                    }
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                        SaleMenuItem(
                            onClick = { navigateAndClose(ClientScreen.Mas.route) },
                            title = "Configuración",
                            subtitle = "Preferencias de la app",
                            icon = Icons.Default.Brightness7
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserHeaderCard(
    userEmail: String,
    planCode: String?
) {
    val primary = MaterialTheme.colorScheme.primary
    val currentPlan = planCode?.takeIf { it.isNotBlank() } ?: "FREE"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.55f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getInitials(userEmail),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = primary
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userEmail.ifBlank { "Usuario" },
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(primary.copy(alpha = 0.08f))
                    .padding(horizontal = 9.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(primary.copy(alpha = 0.75f))
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = "Plan $currentPlan",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = primary
                )
            }
        }
    }
}

@Composable
private fun MenuSectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        color = TextSecondary.copy(alpha = 0.78f),
        letterSpacing = 1.1.sp,
        modifier = Modifier.padding(start = 6.dp, top = 14.dp, bottom = 6.dp)
    )
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp),
        color = BorderGray.copy(alpha = 0.8f),
        thickness = 1.dp
    )
}