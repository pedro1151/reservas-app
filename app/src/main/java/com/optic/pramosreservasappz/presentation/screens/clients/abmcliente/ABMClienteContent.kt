package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.presentation.components.AlternativeTextField
import com.optic.pramosreservasappz.presentation.components.DefaultButton
import com.optic.pramosreservasappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel

@Composable
fun ABMClienteContent(
    navController: NavHostController,
    clientId: Int?,
    editable: Boolean,
    vm: ClientViewModel
) {

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    var isButtonEnabled by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {

        // TOP BAR (como servicios)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = if (editable) "Editar Cliente" else "Nuevo Cliente",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {


        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                AlternativeTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Nombre completo",
                    placeholder = "Ej: Juan Perez"
                )

                Spacer(Modifier.height(12.dp))

                AlternativeTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "correo@gmail.com"
                )

                Spacer(Modifier.height(12.dp))

                AlternativeTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Teléfono",
                    placeholder = "70000000"
                )

                Spacer(Modifier.height(12.dp))

                AlternativeTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = "Ciudad",
                    placeholder = "La Paz"
                )

                Spacer(Modifier.height(12.dp))

                AlternativeTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = "País",
                    placeholder = "Bolivia"
                )

                Spacer(Modifier.height(24.dp))

                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (editable) "Editar" else "Crear",
                    enabled = isButtonEnabled,
                    onClick = {


                        isButtonEnabled = false

                        if (!editable) {
                            vm.createClient(
                                ClientCreateRequest(
                                    providerId = 1,
                                    fullName = fullName,
                                    email = email,
                                    phone = phone,
                                    city = city,
                                    country = country
                                )
                            )
                        } else {
                            clientId?.let {
                                vm.updateClient(
                                    it,
                                    ClientUpdateRequest(
                                        fullName = fullName,
                                        email = email,
                                        phone = phone,
                                        city = city,
                                        country = country
                                    )
                                )
                            }
                        }

                        navController.popBackStack()
                    }
                )
            }
        }
    }

    CustomProgressBar(isLoading = false)
}
