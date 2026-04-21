package com.optic.pramosreservasappz.presentation.screens.salecomplete.stepone


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground

@Composable
fun CompleteSalePaso1Content(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    viewModel: SalesViewModel
) {

    val Cyan = Color(0xFF22C1C3)
    val CyanSoft = Color(0xFF22C1C3).copy(alpha = 0.12f)

    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF475569)
    val BorderSoft = Color(0xFFE2E8F0)

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
            .background(SoftCoolBackground)
            .padding(vertical = 70.dp)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔹 INPUT NOMBRE
            item {
                Text(
                    "Ingresa el nombre de la venta",
                    fontSize = 16.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                            spotColor = Color.Black.copy(alpha = 0.08f)
                        )
                        .padding(16.dp)
                ) {


                    Spacer(Modifier.height(6.dp))

                    OutlinedTextField(
                        value = saleName,
                        onValueChange = { viewModel.onSaleNameChange(it) },
                        placeholder = {
                            Text("Ej: Venta mostrador", color = TextSecondary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Cyan,
                            unfocusedBorderColor = BorderSoft,
                            cursorColor = Cyan
                        )
                    )
                }
            }

            // 🔹 MÉTODOS DE PAGO
            item {
                Text(
                    "Selecciona el método de pago",
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )
            }
            // 🔹 MÉTODOS DE PAGO (CARD CONTENEDORA)
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Color.Black.copy(alpha = 0.05f),
                                spotColor = Color.Black.copy(alpha = 0.08f)
                            )
                            .padding(16.dp)
                    ) {
                       /*
                        Text(
                            "Método de pago",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )

                        */

                        Spacer(Modifier.height(14.dp))


                        // 🔥 GRID 2x2
                       paymentMethods.chunked(2).forEach { rowMethods ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                rowMethods.forEach { method ->

                                    val isSelected = selectedMethod == method

                                    val interaction = remember { MutableInteractionSource() }
                                    val pressed by interaction.collectIsPressedAsState()

                                    val scale by animateFloatAsState(
                                        targetValue = if (isSelected) 1.05f else if (pressed) 0.97f else 1f,
                                        animationSpec = tween(180),
                                        label = ""
                                    )

                                    // 🔥 check animado
                                    val checkScale by animateFloatAsState(
                                        targetValue = if (isSelected) 1f else 0f,
                                        animationSpec = tween(220),
                                        label = ""
                                    )

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .graphicsLayer {
                                                scaleX = scale
                                                scaleY = scale
                                            }
                                            .clip(RoundedCornerShape(14.dp)) // 🔥 IMPORTANTE (limpia bordes internos)
                                            .background(
                                                color = if (isSelected) CyanSoft else Color.Transparent
                                            )
                                            .border(
                                                width = if (isSelected) 1.4.dp else 1.dp,
                                                color = if (isSelected) CyanSoft else BorderSoft,
                                                shape = RoundedCornerShape(14.dp)
                                            )
                                            .clickable(
                                                interactionSource = interaction,
                                                indication = rememberRipple(
                                                    color = Cyan,
                                                    bounded = true
                                                )
                                            ) {
                                                viewModel.onPaymentMethodSelected(method)
                                            }
                                            .padding(vertical = 14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            Box {

                                                Icon(
                                                    imageVector = when (method) {
                                                        "Efectivo" -> Icons.Default.AttachMoney
                                                        "Tarjeta Débito" -> Icons.Default.CreditCard
                                                        "Tarjeta Crédito" -> Icons.Default.CreditScore
                                                        "Mercado Pago" -> Icons.Default.Memory
                                                        "Transferencia" -> Icons.Default.AccountBalance
                                                        else -> Icons.Default.AccountBalance
                                                    },
                                                    contentDescription = null,
                                                    tint = if (isSelected) Cyan else TextSecondary
                                                )

                                                // 🔥 CHECK tipo iOS
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .size(16.dp)
                                                        .graphicsLayer {
                                                            scaleX = checkScale
                                                            scaleY = checkScale
                                                        }
                                                        .background(
                                                            color = Cyan,
                                                            shape = CircleShape
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(10.dp)
                                                    )
                                                }
                                            }

                                            Spacer(Modifier.height(6.dp))

                                            Text(
                                                text = method,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = if (isSelected) Cyan else TextPrimary
                                            )
                                        }
                                    }
                                }

                                // 🔥 si hay impar (por seguridad)
                                if (rowMethods.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }


            item {
                Spacer(Modifier.height(80.dp))
            }
        }

        // 🔥 BOTÓN SIGUIENTE
        val isEnabled = saleName.isNotBlank() && selectedMethod != null

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(ClientScreen.RapidSale.route) // 🔥 ajusta tu ruta
                },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Cyan,
                    disabledContainerColor = Cyan.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Siguiente",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Spacer(Modifier.width(8.dp))

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}