package com.optic.pramosreservasappz.presentation.screens.newsale.stepone

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

@Composable
fun CompleteSalePaso1Content(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    viewModel: NewSaleViewModel
) {
    val primary = MaterialTheme.colorScheme.primary
    val background = MaterialTheme.colorScheme.background
    val surfaceCard = Color.White
    val softSurface = Color(0xFFFAFBFC)

    val saleName = viewModel.saleName
    val selectedMethod = viewModel.paymentMethod

    val paymentMethods = listOf(
        "Efectivo",
        "Tarjeta Débito",
        "Tarjeta Crédito",
        "Transferencia",
        "Mercado Pago",
        "Otro"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(top = 70.dp)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {
                ModernSectionCard {
                    Text(
                        text = "Nombre de la venta",
                        fontSize = 14.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Usa un nombre corto para identificarla rápido.",
                        fontSize = 12.5.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = saleName,
                        onValueChange = { viewModel.onSaleNameChange(it) },
                        placeholder = {
                            Text(
                                text = "Ej: Venta mostrador",
                                color = TextSecondary.copy(alpha = 0.65f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primary.copy(alpha = 0.65f),
                            unfocusedBorderColor = BorderGray,
                            cursorColor = primary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            item {
                ModernSectionCard {
                    Text(
                        text = "Método de pago",
                        fontSize = 14.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Selecciona cómo se registrará esta venta.",
                        fontSize = 12.5.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(16.dp))

                    paymentMethods.chunked(2).forEach { rowMethods ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowMethods.forEach { method ->
                                PaymentMethodItem(
                                    method = method,
                                    isSelected = selectedMethod == method,
                                    primary = primary,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        viewModel.onPaymentMethodSelected(method)
                                    }
                                )
                            }

                            if (rowMethods.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }

        val isEnabled = saleName.isNotBlank() && selectedMethod != null

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.06f),
                    spotColor = Color.Black.copy(alpha = 0.10f)
                )
                .background(
                    color = surfaceCard,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(ClientScreen.CompleteSaleStepTwo.route)
                    viewModel.resetCreateItemState()
                },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    disabledContainerColor = Color(0xFFE5E7EB),
                    contentColor = Color.White,
                    disabledContentColor = TextSecondary.copy(alpha = 0.70f)
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "Siguiente",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(21.dp)
                )
            }
        }
    }
}

@Composable
private fun ModernSectionCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp),
        content = content
    )
}

@Composable
private fun PaymentMethodItem(
    method: String,
    isSelected: Boolean,
    primary: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.97f
            isSelected -> 1.01f
            else -> 1f
        },
        animationSpec = tween(160),
        label = "paymentScale"
    )

    val checkScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(180),
        label = "paymentCheckScale"
    )

    val icon = when (method) {
        "Efectivo" -> Icons.Default.AttachMoney
        "Tarjeta Débito" -> Icons.Default.CreditCard
        "Tarjeta Crédito" -> Icons.Default.CreditScore
        "Mercado Pago" -> Icons.Default.Memory
        "Transferencia" -> Icons.Default.AccountBalance
        else -> Icons.Default.AccountBalance
    }

    Column(
        modifier = modifier
            .height(104.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(18.dp))
            .background(
                color = if (isSelected) Color(0xFFFFFBFD) else Color(0xFFFAFBFC),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) primary.copy(alpha = 0.26f) else BorderGray.copy(alpha = 0.80f),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = interaction,
                indication = null
            ) {
                onClick()
            }
            .padding(horizontal = 10.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) primary.copy(alpha = 0.10f)
                        else Color.White
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) primary else TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 5.dp, y = (-4).dp)
                    .size(18.dp)
                    .graphicsLayer {
                        scaleX = checkScale
                        scaleY = checkScale
                    }
                    .background(
                        color = ButtonSucessColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = method,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) primary else TextPrimary,
            maxLines = 1
        )
    }
}