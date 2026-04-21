package com.optic.pramosreservasappz.presentation.screens.rapidsale.components


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.BluePrimary2
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.DiaglogBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.Grafito
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno
import com.optic.pramosreservasappz.presentation.ui.theme.GrisSuave
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary


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

    val CyanStart = Color(0xFF06B6D4)
    val CyanEnd = Color(0xFF0891B2)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // 🧠 TITULOS
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Ups no encontramos eso",
                fontSize = 18.sp,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )

            Text(
                "Crea el nuevo producto ⚡",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = CyanEnd,
                textAlign = TextAlign.Center
            )
        }

        // 💎 CARD CYAN MODERNA
        Card(
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        ) {

            Box(
                modifier = Modifier
                    .background(GradientBackground)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    SaleInputModern(
                        value = productName,
                        onValueChange = onProductNameChange,
                        label = "Nombre del producto",
                        placeholder = "Ej: Gaseosa"
                    )

                    SaleInputModern(
                        value = priceText,
                        onValueChange = onPriceChange,
                        label = "Precio",
                        keyboardOptions = KeyboardType.Number,
                        placeholder = "Ej: 50"
                    )

                    SaleInputModern(
                        value = cantidad,
                        onValueChange = onCantidadChange,
                        label = "Cantidad",
                        keyboardOptions = KeyboardType.Number,
                        placeholder = "Ej: 2"
                    )

                    // 🔥 BOTÓN MODERNO
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.96f else 1f,
                        animationSpec = spring(dampingRatio = 0.4f)
                    )

                    if(isEnabled) {
                        val contentColor =
                            if (isEnabled)
                                Color.White
                            else
                                GrisSuave

                        Button(
                            onClick = createProduct,
                            enabled = isEnabled,
                            interactionSource = interactionSource,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Grafito,
                                contentColor = contentColor,
                                disabledContainerColor = GrisSuave,
                                disabledContentColor = GrisSuave
                            )
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Text(
                                    "Agregar",
                                    color = contentColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )

                                Spacer(Modifier.width(8.dp))

                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = contentColor,
                                    modifier = Modifier.size(27.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}