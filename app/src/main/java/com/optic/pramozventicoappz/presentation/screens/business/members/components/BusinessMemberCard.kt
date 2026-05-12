package com.optic.pramozventicoappz.presentation.screens.business.members.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.business.BusinessViewModel
import com.optic.pramozventicoappz.presentation.util.getAvatarColor
import com.optic.pramozventicoappz.presentation.util.getInitials

// ─── Design Tokens ──────────────────────────────────────────────────────────
private val Magenta700  = Color(0xFFAD1457)
private val Magenta600  = Color(0xFFD81B60)
private val Magenta50   = Color(0xFFFCE4EC)
private val Slate900    = Color(0xFF0F172A)
private val Slate500    = Color(0xFF64748B)
private val Slate200    = Color(0xFFE2E8F0)

@Composable
fun BusinessMemberCard(
    member        : UserMemberResponse,
    navController : NavHostController,
    modifier      : Modifier = Modifier,
    viewModel     : BusinessViewModel
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val avatarColor = remember(member.user.id) { getAvatarColor(member.user.id) }
    val initials    = remember(member.user.email) { getInitials(member.user.email) }

    val isActive   = member.businessMember.statusLabel.contains("activ", ignoreCase = true)
    val statusBg   = if (isActive) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
    val statusTint = if (isActive) Color(0xFF2E7D32) else Color(0xFFE65100)

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(220)) + expandVertically(tween(220))
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(
                        ClientScreen.UpdateBusinessMember.createRoute(userId = member.user.id)
                    )
                },
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ── Avatar ─────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(avatarColor, avatarColor.copy(alpha = 0.65f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = initials,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                }

                Spacer(Modifier.width(14.dp))

                // ── Info ───────────────────────────────────────────────────
                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text          = member.user.email,
                        fontSize      = 14.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = Slate900,
                        maxLines      = 1,
                        overflow      = TextOverflow.Ellipsis,
                        letterSpacing = (-0.1).sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text     = member.user.username,
                        fontSize = 12.sp,
                        color    = Slate500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(8.dp))

                    // ── Chips ──────────────────────────────────────────────
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

                        // Role chip
                        Row(
                            modifier = Modifier
                                .background(Magenta50, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Shield, null,
                                tint     = Magenta600,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text          = member.businessMember.roleLabel,
                                fontSize      = 10.sp,
                                fontWeight    = FontWeight.SemiBold,
                                color         = Magenta600,
                                letterSpacing = 0.2.sp
                            )
                        }

                        // Status chip
                        Box(
                            modifier = Modifier
                                .background(statusBg, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text          = member.businessMember.statusLabel,
                                fontSize      = 10.sp,
                                fontWeight    = FontWeight.SemiBold,
                                color         = statusTint,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }

                // ── Chevron ────────────────────────────────────────────────
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Outlined.ChevronRight, null,
                    tint     = Slate200,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}