package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components.ClientDefaultField

@Composable
fun ABMClienteContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    clientId: Int? = null,
    editable: Boolean = false,
    viewModel: ClientViewModel
) {
    val scrollState = rememberScrollState()
    val formState by viewModel.formState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        // ── Avatar circular con badge de cámara ──
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Círculo principal
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint = Color(0xFFBBBBBB),
                    modifier = Modifier.size(32.dp)
                )
            }
            // Badge pequeño con ícono cámara
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDDDDD))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Campos con fondo blanco y borde negro (COMO SERVICIOS) ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nombre completo
            ClientDefaultField(
                value = formState.fullName,
                onValueChange = { viewModel.onFormChange(formState.copy(fullName = it)) },
                placeholder = "Nombre completo"
            )

            // Teléfono con bandera Bolivia
            ClientDefaultField(
                value = formState.phone,
                onValueChange ={ viewModel.onFormChange(formState.copy(phone = it)) },
                placeholder = "Número de teléfono",
                keyboardType = KeyboardType.Phone,
                prefix = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text("🇧🇴", fontSize = 18.sp)
                        Text(
                            "+591",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                },
                suffix = {
                    Text(
                        "PRIMARY",
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            )

            // Correo electrónico
            ClientDefaultField(
                value = formState.email,
                onValueChange = { viewModel.onFormChange(formState.copy(email = it)) },
                placeholder = "Correo electrónico",
                keyboardType = KeyboardType.Email,
                suffix = {
                    Text(
                        "PRIMARY",
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            )

            // Empresa
            ClientDefaultField(
                value = formState.enterprise,
                onValueChange = { viewModel.onFormChange(formState.copy(enterprise = it)) },
                placeholder = "Nombre de la empresa"
            )

            // País (dropdown visual)
            ClientDefaultField(
                value = formState.country,
                onValueChange = { viewModel.onFormChange(formState.copy(country = it)) },
                placeholder = "Pais"
            )

            // Dirección
            ClientDefaultField(
                value = formState.address,
                onValueChange = { viewModel.onFormChange(formState.copy(address = it)) },
                placeholder = "Dirección"
            )

            // Ciudad
            ClientDefaultField(
                value = formState.city,
                onValueChange = { viewModel.onFormChange(formState.copy(city = it)) },
                placeholder = "Ciudad"
            )

            // Estado / Provincia
            ClientDefaultField(
                value = formState.state,
                onValueChange = { viewModel.onFormChange(formState.copy(state = it)) },
                placeholder = "Estado"
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

