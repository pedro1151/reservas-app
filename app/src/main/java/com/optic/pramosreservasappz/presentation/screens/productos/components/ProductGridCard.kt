package com.optic.pramosreservasappz.presentation.screens.productos.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.tusventas.components.getAvatarColor
import kotlinx.coroutines.delay


// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue700  = Color(0xFF1D4ED8)
private val Blue600  = Color(0xFF2563EB)
private val Blue500  = Color(0xFF3B82F6)
private val Blue400  = Color(0xFF60A5FA)
private val Blue100  = Color(0xFFDBEAFE)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate600 = Color(0xFF475569)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate100 = Color(0xFFF1F5F9)
private val Red500   = Color(0xFFEF4444)
private val PageBg   = Color(0xFFF8FAFC)

// ─── Grid Card ──────────────────────────────────────────────────────────────────
@Composable
fun ProductGridCard(
    product       : ProductResponse,
    modifier      : Modifier = Modifier,
    navController : NavHostController,
    onDelete      : (ProductResponse) -> Unit
) {
    val avatarColor = remember(product.id) { getAvatarColor(product.id) }
    val priceText   = remember(product.price) {
        try { "Bs. %,.0f".format(product.price.toString().toDouble()) }
        catch (_: Exception) { "Bs. ${product.price}" }
    }

    var showMenu by remember { mutableStateOf(false) }
    var visible  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(30L * (product.id % 8))
        visible = true
    }

    AnimatedVisibility(
        visible  = visible,
        enter    = fadeIn(tween(260)) + scaleIn(tween(260), initialScale = 0.92f),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = 3.dp,
                    shape        = RoundedCornerShape(20.dp),
                    ambientColor = Blue500.copy(alpha = 0.06f),
                    spotColor    = Blue600.copy(alpha = 0.09f)
                )
                .clip(RoundedCornerShape(20.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null
                ) {
                    navController.navigate(
                        ClientScreen.ServiceDetail.createRoute(serviceId = product.id)
                    )
                },
            shape           = RoundedCornerShape(20.dp),
            color           = Color.White,
            shadowElevation = 0.dp
        ) {
            Column {

                // ── Colored header area ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    avatarColor.copy(alpha = 0.16f),
                                    avatarColor.copy(alpha = 0.04f)
                                )
                            )
                        )
                ) {
                    // MoreVert in top-right
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.80f))
                                .clickable(remember { MutableInteractionSource() }, null) {
                                    showMenu = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.MoreVert, null,
                                tint     = Slate600,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        // Dropdown menu
                        DropdownMenu(
                            expanded          = showMenu,
                            onDismissRequest  = { showMenu = false },
                            modifier          = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Editar",
                                        fontSize   = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color      = Slate900
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Edit, null,
                                        tint     = Blue600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(
                                        ClientScreen.ABMServicio.createRoute(
                                            serviceId = product.id,
                                            editable  = true
                                        )
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Eliminar",
                                        fontSize   = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color      = Red500
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.DeleteOutline, null,
                                        tint     = Red500,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onDelete(product)
                                }
                            )
                        }
                    }

                    // Centered avatar
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(avatarColor, avatarColor.copy(alpha = 0.60f))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = getServiceInitials(product.name),
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color.White,
                                letterSpacing = (-0.3).sp
                            )
                        }
                    }
                }

                // ── Content area ──
                Column(
                    modifier = Modifier.padding(
                        start  = 12.dp,
                        end    = 12.dp,
                        top    = 10.dp,
                        bottom = 12.dp
                    )
                ) {
                    Text(
                        text          = product.name,
                        fontSize      = 13.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = Slate900,
                        maxLines      = 2,
                        overflow      = TextOverflow.Ellipsis,
                        lineHeight    = 17.sp,
                        letterSpacing = (-0.1).sp
                    )
                    Spacer(Modifier.height(8.dp))

                    // Price
                    Text(
                        text          = priceText,
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = Blue600,
                        letterSpacing = (-0.4).sp
                    )

                    Spacer(Modifier.height(8.dp))

                    // Category pill
                    Box(
                        modifier = Modifier
                            .background(Blue50, RoundedCornerShape(7.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Category, null,
                                tint     = Blue600,
                                modifier = Modifier.size(8.dp)
                            )
                            Text(
                                "Producto",
                                fontSize      = 9.sp,
                                color         = Blue600,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
