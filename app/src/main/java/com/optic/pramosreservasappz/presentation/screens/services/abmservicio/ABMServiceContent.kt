package com.optic.pramosreservasappz.presentation.screens.services.abmservicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ABMServiceContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    serviceId: Int? = null,
    editable: Boolean = false
) {
    // Estados del formulario
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var bufferTime by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    // Color aleatorio generado automáticamente
    val randomColor = remember {
        val colors = listOf(
            Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF00BCD4),
            Color(0xFF009688), Color(0xFF4CAF50), Color(0xFFFF5722),
            Color(0xFFE91E63), Color(0xFF9C27B0)
        )
        colors.random()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .verticalScroll(scrollState)
    ) {
        // Header moderno con icono
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Icon(
                    imageVector = Icons.Outlined.MiscellaneousServices,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Text(
                    text = if (editable) "Actualizar información" else "Completar datos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF000000),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (editable) "del servicio" else "del nuevo servicio",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Título
        ModernTextField(
            label = "Título",
            value = title,
            onValueChange = { title = it },
            icon = Icons.Outlined.Title,
            placeholder = "Ejemplo: Servicio de consultoría"
        )

        // Descripción
        ModernTextField(
            label = "Descripción",
            value = description,
            onValueChange = { description = it },
            icon = Icons.Outlined.Description,
            placeholder = "Describe tu servicio",
            minLines = 3,
            maxLines = 5
        )

        // Duración
        ModernTextField(
            label = "Duración",
            value = duration,
            onValueChange = { duration = it },
            icon = Icons.Outlined.AccessTime,
            placeholder = "Ingresar la duración (mins)",
            keyboardType = KeyboardType.Number
        )

        // Tiempo de buffer
        ModernTextField(
            label = "Tiempo de buffer",
            value = bufferTime,
            onValueChange = { bufferTime = it },
            icon = Icons.Outlined.HourglassEmpty,
            placeholder = "Ingresar el tiempo de espera (mins)",
            keyboardType = KeyboardType.Number,
            isOptional = true
        )

        // Precio
        ModernTextField(
            label = "Precio",
            value = price,
            onValueChange = { price = it },
            icon = Icons.Outlined.AttachMoney,
            placeholder = "Ingresar el precio",
            keyboardType = KeyboardType.Decimal
        )

        // Categoría
        ModernTextField(
            label = "Categoría",
            value = category,
            onValueChange = { category = it },
            icon = Icons.Outlined.Category,
            placeholder = "Selecciona la categoría",
            isOptional = true
        )

        Spacer(Modifier.height(16.dp))

        // Botón de acción con texto visible
        Button(
            onClick = { /* Guardar */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (editable) "Guardar cambios" else "Crear servicio",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun ModernTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String = "",
    isOptional: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Label con icono
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF000000)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF000000),
                    fontWeight = FontWeight.Normal
                )
            }

            if (isOptional) {
                Text(
                    text = "Opcional",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9E9E9E)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // TextField
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFFBDBDBD),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedTextColor = Color(0xFF000000),
                unfocusedTextColor = Color(0xFF000000),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
