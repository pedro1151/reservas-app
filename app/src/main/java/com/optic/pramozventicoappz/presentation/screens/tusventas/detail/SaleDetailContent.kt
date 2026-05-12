package com.optic.pramozventicoappz.presentation.screens.tusventas.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramozventicoappz.presentation.screens.tusventas.components.getAvatarColor
import com.optic.pramozventicoappz.presentation.util.formatSaleDate

@Composable
fun SaleDetailContent(
    sale: SaleWithItemsResponse,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val bg = Color(0xFFF9FAFB)
    val cardBg = Color.White
    val border = Color(0xFFE5E7EB)
    val softBorder = Color(0xFFF1F5F9)
    val textPrimary = Color(0xFF0F172A)
    val textMuted = Color(0xFF64748B)
    val accent = Color(0xFFE91E63)
    val accentDark = Color(0xFFD81B60)
    val success = Color(0xFF10B981)

    val saleAvatarColor = remember(sale.sale.id) { getAvatarColor(sale.sale.id) }

    val clientName = sale.sale.client?.fullName
        ?.takeIf { it.isNotBlank() }
        ?: "Cliente no informado"

    val clientEmail = sale.sale.client?.email
        ?.takeIf { it.isNotBlank() }
        ?: "Sin email"

    val clientPhone = sale.sale.client?.phone
        ?.takeIf { it.isNotBlank() }
        ?: "Sin teléfono"

    val salesmanName = sale.sale.salesman?.username
        ?.takeIf { it.isNotBlank() }
        ?: sale.sale.salesman?.email
        ?: "Vendedor no informado"

    val salesmanEmail = sale.sale.salesman?.email
        ?.takeIf { it.isNotBlank() }
        ?: "Sin email"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF9FAFB),
                        Color(0xFFF1F5F9)
                    )
                )
            ),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 40.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = cardBg,
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, border)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(width = 180.dp, height = 110.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        accent.copy(alpha = 0.06f),
                                        accentDark.copy(alpha = 0.10f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Venta registrada",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = textPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = formatSaleDate(sale.sale.created),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "$ ${"%,.2f".format(sale.sale.amount)}",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = textPrimary,
                            letterSpacing = (-1.5).sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFFF8F4F6))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "N° de operación: ${sale.sale.id}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = textMuted
                            )
                        }
                    }
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = cardBg,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, border)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Detalle de operación",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textPrimary
                    )

                    Spacer(Modifier.height(18.dp))

                    OperationDetailRow(
                        icon = Icons.Outlined.Payments,
                        iconColor = accent,
                        label = "Método de pago",
                        value = sale.sale.paymentMethod ?: "N/A"
                    )

                    Spacer(Modifier.height(18.dp))

                    OperationDetailRow(
                        icon = Icons.Outlined.ReceiptLong,
                        iconColor = success,
                        label = "Cantidad de artículos",
                        value = "${sale.items.size}"
                    )

                    Spacer(Modifier.height(18.dp))

                    OperationDetailRow(
                        icon = Icons.Outlined.Storefront,
                        iconColor = accent,
                        label = "Vendedor",
                        value = salesmanName
                    )
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = cardBg,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, border)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Personas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textPrimary
                    )

                    Spacer(Modifier.height(18.dp))

                    PersonResumeRow(
                        icon = Icons.Outlined.Person,
                        avatarColor = saleAvatarColor,
                        title = clientName,
                        subtitle = "Cliente",
                        extra = clientEmail
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = softBorder,
                        thickness = 1.dp
                    )

                    PersonResumeRow(
                        icon = Icons.Outlined.Storefront,
                        avatarColor = accent,
                        title = salesmanName,
                        subtitle = "Vendedor",
                        extra = salesmanEmail
                    )
                }
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = cardBg,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, border)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Datos del cliente",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textPrimary
                    )

                    Spacer(Modifier.height(18.dp))

                    ClientDetailRow(
                        icon = Icons.Outlined.Person,
                        label = "Nombre",
                        value = clientName
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = softBorder,
                        thickness = 1.dp
                    )

                    ClientDetailRow(
                        icon = Icons.Outlined.Email,
                        label = "Email",
                        value = clientEmail
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = softBorder,
                        thickness = 1.dp
                    )

                    ClientDetailRow(
                        icon = Icons.Outlined.Phone,
                        label = "Teléfono",
                        value = clientPhone
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Productos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textPrimary
                )

                Text(
                    text = "${sale.items.size} artículos",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textMuted
                )
            }
        }

        items(sale.items) { item ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }

            val avatarColor = remember(item.product?.id) {
                getAvatarColor(item.product?.id ?: 0)
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(200)) + slideInVertically(tween(220)) { it / 3 }
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = cardBg,
                    shadowElevation = 1.dp,
                    border = BorderStroke(1.dp, border)
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(avatarColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingBag,
                                contentDescription = null,
                                tint = avatarColor,
                                modifier = Modifier.size(23.dp)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product?.name ?: "Producto",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "${item.quantity} × $ ${"%.2f".format(item.price)}",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = textMuted
                            )
                        }

                        Text(
                            text = "$ ${"%.2f".format(item.quantity * item.price)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = accent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OperationDetailRow(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconColor.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PersonResumeRow(
    icon: ImageVector,
    avatarColor: Color,
    title: String,
    subtitle: String,
    extra: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(avatarColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = avatarColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )
        }

        Text(
            text = extra,
            fontSize = 11.5.sp,
            color = Color(0xFF94A3B8),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 120.dp)
        )
    }
}

@Composable
private fun ClientDetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFE91E63),
            modifier = Modifier.size(19.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF64748B),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0F172A),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 190.dp)
        )
    }
}