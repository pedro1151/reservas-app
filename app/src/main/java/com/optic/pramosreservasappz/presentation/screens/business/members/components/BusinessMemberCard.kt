package com.optic.pramosreservasappz.presentation.screens.business.members.components


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
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary
import com.optic.pramosreservasappz.presentation.util.getAvatarColor
import com.optic.pramosreservasappz.presentation.util.getInitials


@Composable
fun BusinessMemberCard(
    member: UserMemberResponse,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: BusinessViewModel
) {
    // ── lógica sin cambios ─────────────────────────────────────────────────────

    var visible          by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val avatarColor = remember(member.user.id) { getAvatarColor(member.user.id) }
    val initials    = remember(member.user.email) { getInitials(member.user.email) }

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
                    navController.navigate(
                        ClientScreen.UpdateBusinessMember.createRoute(userId = member.user.id)
                    )
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
                            text = member.user.email,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal, // 🔥 leve mejora
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )



                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = member.user.username,
                                fontSize = 14.sp, // 🔥 más moderno
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = member.businessMember.roleLabel,
                            fontSize = 14.sp, // 🔥 más moderno
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = member.businessMember.statusLabel,
                            fontSize = 14.sp, // 🔥 más moderno
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

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