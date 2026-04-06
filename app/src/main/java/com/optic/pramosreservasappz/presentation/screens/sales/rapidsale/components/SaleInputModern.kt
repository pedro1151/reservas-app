package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.ui.theme.BluePrimary2
import com.optic.pramosreservasappz.presentation.ui.theme.BlueSoft
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.Grafito
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary
import com.optic.pramosreservasappz.presentation.ui.theme.VioletPrimary


@Composable
fun SaleInputModern(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {

    val GrayPlaceholder = Color(0xFF9CA3AF)

    Column(modifier = modifier) {

        // 🏷 LABEL MÁS GRANDE Y SUAVE
        Text(
            text = label,
            fontSize = 18.sp,
            color = Grafito,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardOptions),

            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 17.sp,
                    color =Color(0xFF9CA3AF)
                )
            },

            textStyle = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = MaterialTheme.colorScheme.secondary
            ),

            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp), // 🔥 más grande

            shape = RoundedCornerShape(10.dp),

            colors = TextFieldDefaults.colors(

                // texto
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                // fondo blanco limpio
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                // sin bordes feos
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

                cursorColor = BluePrimary2,

                // placeholder fallback
                unfocusedPlaceholderColor = TextSecondary,
                focusedPlaceholderColor = TextSecondary
            )
        )
    }
}