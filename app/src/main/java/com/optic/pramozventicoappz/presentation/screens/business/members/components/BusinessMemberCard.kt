package com.optic.pramozventicoappz.presentation.screens.business.members.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlin.math.abs

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue700  = Color(0xFF1D4ED8)
private val Blue600  = Color(0xFF2563EB)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Red500   = Color(0xFFEF4444)

@Composable
fun BusinessMemberCard(
    member        : UserMemberResponse,
    navController : NavHostController,
    modifier      : Modifier = Modifier,
    viewModel     : BusinessViewModel
) {
    var offsetX          by remember { mutableStateOf(0f) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible          by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    val avatarColor = remember(member.user.id) { getAvatarColor(member.user.id) }
    val initials    = remember(member.user.email) { getInitials(member.user.email) }

    val maxSwipeDistance = -200f
    val editThreshold    = -60f
    val deleteThreshold  = -140f

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(250)) + expandVertically(tween(250)),
        exit    = fadeOut(tween(180)) + shrinkVertically(tween(180))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    enabled           = offsetX != 0f
                ) { offsetX = 0f }
        ) {

            // ── Swipe action pills ──────────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Edit pill
                AnimatedVisibility(
                    visible = offsetX <= editThreshold,
                    enter   = fadeIn(tween(160)) + scaleIn(tween(160), initialScale = 0.80f),
                    exit    = fadeOut(tween(120)) + scaleOut(tween(120))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .shadow(
                                elevation    = 4.dp,
                                shape        = RoundedCornerShape(14.dp),
                                ambientColor = Blue600.copy(alpha = 0.20f),
                                spotColor    = Blue600.copy(alpha = 0.28f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(listOf(Blue700, Blue600)))
                            .clickable(remember { MutableInteractionSource() }, null) {
                                offsetX = 0f
                                navController.navigate(
                                    ClientScreen.UpdateBusinessMember.createRoute(
                                        userId = member.user.id
                                    )
                                )
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Edit, null,
                                    tint     = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                "Editar",
                                color         = Color.White,
                                fontSize      = 10.sp,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }

                // Delete pill
                AnimatedVisibility(
                    visible = offsetX <= deleteThreshold,
                    enter   = fadeIn(tween(160)) + scaleIn(tween(160), initialScale = 0.80f),
                    exit    = fadeOut(tween(120)) + scaleOut(tween(120))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .shadow(
                                elevation    = 4.dp,
                                shape        = RoundedCornerShape(14.dp),
                                ambientColor = Red500.copy(alpha = 0.20f),
                                spotColor    = Red500.copy(alpha = 0.28f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Red500)
                            .clickable(remember { MutableInteractionSource() }, null) {
                                showDeleteDialog = true
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Delete, null,
                                    tint     = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                "Eliminar",
                                color         = Color.White,
                                fontSize      = 10.sp,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }
            }

            // ── Main card ──────────────────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        scaleY = (1f - abs(offsetX) / 1000f).coerceIn(0.98f, 1f)
                    }
                    .shadow(
                        elevation    = 2.dp,
                        shape        = RoundedCornerShape(18.dp),
                        ambientColor = Blue600.copy(alpha = 0.05f)
                    )
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                offsetX = when {
                                    offsetX <= deleteThreshold -> maxSwipeDistance
                                    offsetX <= editThreshold   -> -80f
                                    else                       -> 0f
                                }
                            },
                            onHorizontalDrag = { _, drag ->
                                offsetX = (offsetX + drag).coerceIn(maxSwipeDistance, 0f)
                            }
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) {
                        if (offsetX == 0f) {
                            navController.navigate(
                                ClientScreen.UpdateBusinessMember.createRoute(
                                    userId = member.user.id
                                )
                            )
                        } else {
                            offsetX = 0f
                        }
                    },
                shape           = RoundedCornerShape(18.dp),
                color           = Color.White,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(avatarColor, avatarColor.copy(alpha = 0.70f))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text          = initials,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(Modifier.width(13.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        // Email (primary)
                        Text(
                            text          = member.user.email,
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.SemiBold,
                            color         = Slate900,
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(Modifier.height(3.dp))

                        // Username + role pill in a row
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text     = member.user.username,
                                fontSize = 12.sp,
                                color    = Slate400,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            // Role pill
                            Box(
                                modifier = Modifier
                                    .background(Blue50, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Row(
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.Shield, null,
                                        tint     = Blue600,
                                        modifier = Modifier.size(9.dp)
                                    )
                                    Text(
                                        text          = member.businessMember.roleLabel,
                                        fontSize      = 9.sp,
                                        color         = Blue600,
                                        fontWeight    = FontWeight.SemiBold,
                                        letterSpacing = 0.2.sp
                                    )
                                }
                            }

                            // Status pill
                            val statusBg   = if (member.businessMember.statusLabel.contains("activ", ignoreCase = true))
                                Color(0xFFF0FDF4) else Color(0xFFFFF7ED)
                            val statusTint = if (member.businessMember.statusLabel.contains("activ", ignoreCase = true))
                                Color(0xFF059669) else Color(0xFFEA580C)

                            Box(
                                modifier = Modifier
                                    .background(statusBg, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text          = member.businessMember.statusLabel,
                                    fontSize      = 9.sp,
                                    color         = statusTint,
                                    fontWeight    = FontWeight.SemiBold,
                                    letterSpacing = 0.2.sp
                                )
                            }
                        }
                    }

                    if (offsetX == 0f) {
                        Icon(
                            Icons.Outlined.ChevronRight, null,
                            tint     = Slate200,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // ── Delete confirmation dialog ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; offsetX = 0f },
            title = {
                Text(
                    "¿Eliminar colaborador?",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Slate900
                )
            },
            text = {
                Text(
                    "Se eliminará a ${member.user.email} del equipo. Esta acción no se puede deshacer.",
                    fontSize   = 14.sp,
                    color      = Slate400,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        visible = false
                        offsetX = 0f
                        // viewModel.deleteMember(member.user.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500),
                    shape  = RoundedCornerShape(12.dp)
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; offsetX = 0f }) {
                    Text("Cancelar", color = Slate900)
                }
            },
            shape          = RoundedCornerShape(18.dp),
            containerColor = Color.White
        )
    }
}