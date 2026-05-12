package com.optic.pramozventicoappz.presentation.screens.productos.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Launch
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramozventicoappz.presentation.ui.theme.BadgeGrisBackground
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGrisSoftCard
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

private val Red500 = Color(0xFFEF4444)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ProductOptionsSheet(
    serviceName: String,
    isHidden: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onPreview: () -> Unit,
    onShare: () -> Unit,
    onToggleHidden: (Boolean) -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    primary: Color
) {
    val primaryDark = Color(0xFFD81B60)
    val glass = Color.White.copy(alpha = 0.13f)
    val glassBorder = Color.White.copy(alpha = 0.18f)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 34.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                primary,
                                primaryDark
                            )
                        )
                    )
                    .padding(horizontal = 18.dp)
                    .padding(top = 12.dp, bottom = 18.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(38.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.34f))
                    )

                    Spacer(Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(glass)
                                    .border(
                                        width = 1.dp,
                                        color = glassBorder,
                                        shape = RoundedCornerShape(999.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .background(AmarrilloSuave, CircleShape)
                                )

                                Spacer(Modifier.width(7.dp))

                                Text(
                                    text = "Producto",
                                    color = AmarrilloSuave,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = serviceName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(Modifier.height(3.dp))

                            Text(
                                text = "Gestiona las acciones rápidas",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.76f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(Modifier.width(14.dp))

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(glass)
                                .border(
                                    width = 1.dp,
                                    color = glassBorder,
                                    shape = CircleShape
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Cerrar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OptionRow(
                    icon = Icons.Outlined.Edit,
                    label = "Editar producto",
                    subtitle = "Actualiza nombre, precio o categoría",
                    onClick = onEdit,
                    tint = primary,
                    iconBackground = primary.copy(alpha = 0.10f)
                )

                OptionRow(
                    icon = Icons.Outlined.Launch,
                    label = "Vista previa",
                    subtitle = "Revisa cómo se muestra este producto",
                    onClick = onPreview,
                    tint = TextPrimary,
                    iconBackground = BadgeGrisBackground
                )

                OptionRow(
                    icon = Icons.Outlined.DeleteOutline,
                    label = "Borrar producto",
                    subtitle = "Elimina este producto definitivamente",
                    onClick = onDelete,
                    tint = Red500,
                    iconBackground = Color(0xFFFEF2F2),
                    destructive = true
                )
            }
        }
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    label: String,
    subtitle: String,
    onClick: () -> Unit,
    tint: Color,
    iconBackground: Color,
    destructive: Boolean = false
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (destructive) Color(0xFFFFFBFB) else Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (destructive) Color(0xFFFEE2E2) else BorderGrisSoftCard
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBackground)
                    .border(
                        width = 1.dp,
                        color = tint.copy(alpha = if (destructive) 0.14f else 0.10f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    color = if (destructive) Red500 else TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    letterSpacing = (-0.15).sp
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary.copy(alpha = 0.78f),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}