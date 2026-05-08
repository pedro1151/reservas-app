package com.optic.pramozventicoappz.presentation.screens.newsale.selecclient.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.clients.ClientResponse
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.inicio.SalesViewModel
import com.optic.pramozventicoappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import com.optic.pramozventicoappz.presentation.util.getAvatarColor
import com.optic.pramozventicoappz.presentation.util.getInitials


@Composable
fun SaleSelectClientCard(
    client: ClientResponse,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: NewSaleViewModel
) {
    // ── lógica sin cambios ─────────────────────────────────────────────────────

    var visible          by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val avatarColor = remember(client.id) { getAvatarColor(client.id) }
    val initials    = remember(client.fullName) { getInitials(client.fullName) }

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(250)) + expandVertically(tween(250)),
        exit    = fadeOut(tween(180)) + shrinkVertically(tween(180))
    ) {

        // 🔥 CAPA 1: SOLO SOMBRA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.selectClient(client)
                    navController.navigate(ClientScreen.CompleteSaleStepTree.route)
                }
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                )
        ) {

            // 🔥 CAPA 2: CONTENIDO LIMPIO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)) // 🔥 CLAVE (evita bordes raros)
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🔹 Avatar
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W700,
                            color = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // 🔹 Info
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = client.fullName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal, // 🔥 leve mejora
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        val subtitle = client.email ?: client.phone

                        if (subtitle != null) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = subtitle,
                                fontSize = 14.sp, // 🔥 más moderno
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Icon(
                        Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

}