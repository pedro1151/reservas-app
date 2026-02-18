package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

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
fun ABMClienteContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    clientId: Int? = null,
    editable: Boolean = false
) {
    // Estados del formulario
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

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
                    imageVector = Icons.Outlined.Person,
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
                    text = if (editable) "del cliente" else "del nuevo cliente",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Nombre completo
        ModernTextField(
            label = "Nombre completo",
            value = fullName,
            onValueChange = { fullName = it },
            icon = Icons.Outlined.Person,
            placeholder = "Ej: Juan Pérez"
        )

        // Correo electrónico
        ModernTextField(
            label = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            icon = Icons.Outlined.Email,
            placeholder = "correo@ejemplo.com",
            keyboardType = KeyboardType.Email
        )

        // Teléfono
        ModernTextField(
            label = "Teléfono",
            value = phone,
            onValueChange = { phone = it },
            icon = Icons.Outlined.Phone,
            placeholder = "70000000",
            keyboardType = KeyboardType.Phone
        )

        // Ciudad
        ModernTextField(
            label = "Ciudad",
            value = city,
            onValueChange = { city = it },
            icon = Icons.Outlined.LocationOn,
            placeholder = "La Paz",
            isOptional = true
        )

        // País
        ModernTextField(
            label = "País",
            value = country,
            onValueChange = { country = it },
            icon = Icons.Outlined.Public,
            placeholder = "Bolivia",
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
                text = if (editable) "Guardar cambios" else "Crear cliente",
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
