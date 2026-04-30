package com.optic.pramosreservasappz.presentation.screens.newsale.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramosreservasappz.presentation.ui.theme.Grafito
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

@Composable
fun NewRapidProduct(
    productName: String,
    onProductNameChange: (String) -> Unit,
    priceText: String,
    onPriceChange: (String) -> Unit,
    cantidad: String,
    onCantidadChange: (String) -> Unit,
    createProduct: () -> Unit,
    isEnabled: Boolean
) {
    val primary = Color(0xFFE91E63)
    val primaryDark = Color(0xFFD81B60)
    val disabled = Color(0xFFE5E7EB)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(30.dp),
                    ambientColor = Color.Black.copy(alpha = 0.07f),
                    spotColor = Color.Black.copy(alpha = 0.13f)
                ),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFF3F7),
                                    Color.White
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(primary, primaryDark)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(Modifier.width(15.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Agregar Item",
                                color = Grafito,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "Créalo y súmalo rápido a esta venta",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    QuickProductInput(
                        value = productName,
                        onValueChange = onProductNameChange,
                        label = "Producto",
                        placeholder = "Ej: Gaseosa",
                        icon = Icons.Default.Inventory2,
                        primary = primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickProductInput(
                            value = priceText,
                            onValueChange = onPriceChange,
                            label = "Precio",
                            placeholder = "50",
                            icon = Icons.Default.Payments,
                            keyboardType = KeyboardType.Number,
                            primary = primary,
                            modifier = Modifier.weight(1f)
                        )

                        QuickProductInput(
                            value = cantidad,
                            onValueChange = onCantidadChange,
                            label = "Cantidad",
                            placeholder = "2",
                            icon = Icons.Default.Numbers,
                            keyboardType = KeyboardType.Number,
                            primary = primary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(2.dp))

                    val interaction = remember { MutableInteractionSource() }
                    val pressed by interaction.collectIsPressedAsState()

                    val scale by animateFloatAsState(
                        targetValue = if (pressed && isEnabled) 0.97f else 1f,
                        animationSpec = spring(dampingRatio = 0.45f),
                        label = "createProductScale"
                    )

                    Button(
                        onClick = createProduct,
                        enabled = isEnabled,
                        interactionSource = interaction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        shape = RoundedCornerShape(19.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonSucessColor,
                            contentColor = Color.White,
                            disabledContainerColor = disabled,
                            disabledContentColor = TextSecondary.copy(alpha = 0.70f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = if (isEnabled) "Crear y agregar" else "Completa los datos",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickProductInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    primary: Color,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val hasValue = value.isNotBlank()

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFAFBFC),
        border = BorderStroke(
            width = 1.dp,
            color = if (hasValue) primary.copy(alpha = 0.24f) else BorderGray.copy(alpha = 0.85f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (hasValue) primary.copy(alpha = 0.10f)
                        else Color.White
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (hasValue) primary else TextSecondary.copy(alpha = 0.82f),
                    modifier = Modifier.size(19.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = if (hasValue) primary else TextSecondary
                )

                Spacer(Modifier.height(6.dp))

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = TextSecondary.copy(alpha = 0.58f),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}