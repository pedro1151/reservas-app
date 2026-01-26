package com.optic.pramosreservasappz.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier.fillMaxWidth(), // ahora por defecto ocupa todo el ancho
    text: String,
    enabled : Boolean = true,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.primary,
    icon: ImageVector? = null, // icono opcional
    color: Color = Color(0xFF2196F3), // Puedes usar Blue500 o definir aquí
    //icon: ImageVector = Icons.Default.ArrowForward
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        )
    ) {
        // Si hay icono, mostrarlo
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = textColor
            )
        }


        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color =     textColor
        )
    }
}