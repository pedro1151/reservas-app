package com.optic.ecommerceappmvvm.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.ArrowForward

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier.fillMaxWidth(), // ahora por defecto ocupa todo el ancho
    text: String,
    enabled : Boolean = true,
    onClick: () -> Unit,
    icon: ImageVector? = null, // icono opcional
    color: Color = Color(0xFF2196F3), // Puedes usar Blue500 o definir aquí
    //icon: ImageVector = Icons.Default.ArrowForward
) {
    Button(
        modifier = modifier.height(60.dp) ,// Ajusta la altura aquí),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ) ,
        shape = RoundedCornerShape(8.dp),

    ) {
        // Si hay icono, mostrarlo
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White
            )
        }


        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text)
    }
}