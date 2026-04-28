package com.optic.pramosreservasappz.presentation.screens.newsale.components


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray


@Composable
fun ProductMiniCard(
    name: String,
    price: String,
    modifier: Modifier,
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // 🔥 casi flat (más elegante)
        )
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // 🔹 IZQUIERDA → nombre + precio
            Row(
                modifier =modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {



                Spacer(modifier.width(12.dp))

                // 🔹 TEXTO (nombre + precio)
                Column(
                    modifier = modifier.weight(1f)
                ) {
                    Text(
                        name,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.background
                    )

                    Text(
                        price,
                        color = AmarrilloSuave,
                        fontSize = 13.sp
                    )
                }


            }
        }
    }

}


