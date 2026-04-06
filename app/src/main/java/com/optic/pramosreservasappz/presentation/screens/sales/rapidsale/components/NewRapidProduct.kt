package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.ui.theme.BluePrimary2
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno
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
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🧠 TITULOS
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                "No no se han encontrado resultados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = GrisModerno,
                textAlign = TextAlign.Center
            )

            Text(
                " ¿ Deseas agregarlo?  ⚡",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )
        }

        // 💎 CARD MODERNA
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    SaleInputModern(
                        value = productName,
                        onValueChange = onProductNameChange,
                        label = "¿Que vendiste?",
                        placeholder = "Ej: Gaseosa, galletitas, servicio técnico"
                    )

                    SaleInputModern(
                        value = priceText,
                        onValueChange = onPriceChange,
                        label = "¿Que Precio?",
                        keyboardOptions = KeyboardType.Number,
                        placeholder = "Ej: 500"
                    )

                    SaleInputModern(
                        value = cantidad,
                        onValueChange = onCantidadChange,
                        label = "¿Que cantidad?",
                        keyboardOptions = KeyboardType.Number,
                        placeholder = "Ej: 5"
                    )
                }

                // 🔥 BOTÓN PREMIUM
                Button(
                    onClick = createProduct,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = productName.isNotBlank() &&
                            priceText.isNotBlank() &&
                            cantidad.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePrimary2
                    )
                ) {
                    Text(
                        "Agregar producto",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}