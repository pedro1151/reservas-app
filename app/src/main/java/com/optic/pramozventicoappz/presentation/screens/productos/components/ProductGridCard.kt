package com.optic.pramozventicoappz.presentation.screens.productos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.product.MiniProductResponse
import com.optic.pramozventicoappz.domain.model.product.ProductResponse
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import com.optic.pramozventicoappz.presentation.util.getAvatarColor
import kotlinx.coroutines.delay

@Composable
fun ProductGridCard(
    product: MiniProductResponse,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onDelete: (MiniProductResponse) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val surface = Color.White
    val danger = Color(0xFFE53935)

    val avatarColor = remember(product.id) { getAvatarColor(product.id) }

    val priceText = remember(product.price) {
        try {
            "$ %,.0f".format(product.price.toString().toDouble())
        } catch (_: Exception) {
            "$ ${product.price}"
        }
    }

    var showMenu by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }


    fun goToDetail() {
        navController.navigate(
            ClientScreen.ProductDetail.createRoute(productId = product.id)
        )
    }

    fun goToEdit() {
        navController.navigate(
            ClientScreen.ABMProduct.createRoute(
                productId = product.id,
                editable = true
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(24L * (product.id % 8))
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(220)) + scaleIn(tween(220), initialScale = 0.96f),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp) // 🔥 reducido
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.04f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                )
                .clip(RoundedCornerShape(24.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                  goToDetail()
                },
            shape = RoundedCornerShape(24.dp),
            color = surface,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp) // 🔥 reducido
                        .background(avatarColor.copy(alpha = 0.08f))
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = CircleShape,
                                    ambientColor = Color.Black.copy(alpha = 0.04f),
                                    spotColor = Color.Black.copy(alpha = 0.08f)
                                )
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.92f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    showMenu = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = "Más opciones",
                                tint = TextSecondary,
                                modifier = Modifier.size(14.dp) // 🔥 reducido
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Editar",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = null,
                                        tint = primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    goToEdit()
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Eliminar",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = danger
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = null,
                                        tint = danger,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onDelete(product)
                                }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp) // 🔥 reducido
                                .clip(RoundedCornerShape(21.dp))
                                .background(Color.White.copy(alpha = 0.88f))
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(21.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.03f),
                                    spotColor = Color.Black.copy(alpha = 0.07f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Inventory2,
                                contentDescription = null,
                                tint = avatarColor.copy(alpha = 0.88f),
                                modifier = Modifier.size(24.dp) // 🔥 reducido
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 11.dp,
                            bottom = 11.dp
                        ), // 🔥 compacto
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = product.name,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp,
                        letterSpacing = (-0.1).sp,
                        textAlign = TextAlign.Center
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = priceText,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary,
                            letterSpacing = (-0.2).sp
                        )

                        Spacer(Modifier.height(6.dp)) // 🔥 reducido

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(999.dp))
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Category,
                                    contentDescription = null,
                                    tint = TextSecondary.copy(alpha = 0.72f),
                                    modifier = Modifier.size(11.dp)
                                )

                                Text(
                                    text = product.type,
                                    fontSize = 10.sp,
                                    color = TextSecondary.copy(alpha = 0.82f),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.2.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}