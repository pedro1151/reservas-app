package com.optic.pramosreservasappz.presentation.screens.services.abmservicio


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.*
import com.optic.pramosreservasappz.presentation.components.colorselector.ColorSelectorRow
import com.optic.pramosreservasappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel


@Composable
fun ABMServiceContent(
    navController: NavHostController,
    serviceId: Int?,
    editable: Boolean,
    vm: ServiceViewModel = hiltViewModel()
) {


    val context = LocalContext.current
    val createState = remember { mutableStateOf(ServiceCreateState()) }
    val createResource by vm.createServiceState.collectAsState()
    val updateResource by vm.updateServiceState.collectAsState()
    var isButtonEnabled by remember { mutableStateOf(true) }


    LaunchedEffect(serviceId, editable) {
        if (editable && serviceId != null) {
            vm.getServiceById(serviceId) // debes tener este método
        }
    }

    val serviceResource by vm.serviceState.collectAsState()

    LaunchedEffect(serviceResource) {
                if (serviceResource is Resource.Success) {
                    val service = (serviceResource as Resource.Success<ServiceResponse>).data

                    createState.value = createState.value.copy(
                        name = service.name,
                        description = service.description ?: "",
                        price = service.price?.toString() ?: "",
                        durationMinutes = service.durationMinutes.toString(),
                        isActive = service.isActive ?: true,
                        bufferTime = service.bufferTime.toString() ?: "",
                        category = service.category?:"",
                        hidden = service.hidden ?: false,
                        color =  service.color?: "",
                    )
                }
            }


    // Mostrar mensaje si error
    LaunchedEffect(key1 = createResource) {
        if (createResource is Resource.Failure) {
            Toast.makeText(context, (createResource as Resource.Failure).message, Toast.LENGTH_LONG).show()
            isButtonEnabled = true
        } else if (createResource is Resource.Success) {
            // Ir a pantalla principal de servicios limpiando historial
            navController.navigate(ClientScreen.Servicios.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Mostrar mensaje si error
    LaunchedEffect(key1 = updateResource) {
        if (updateResource is Resource.Failure) {
            Toast.makeText(context, (updateResource as Resource.Failure).message, Toast.LENGTH_LONG).show()
            isButtonEnabled = true
        } else if (updateResource is Resource.Success) {
            // Ir a pantalla principal de servicios limpiando historial
            navController.navigate(ClientScreen.Servicios.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .clickable { /* Puedes manejar navegación aquí */ },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background // Fondo blanco/gris muy claro
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    AlternativeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = createState.value.name,
                        onValueChange = { createState.value = createState.value.copy(name = it) },
                        label = "Titulo",
                        icon = Icons.Default.SpaceDashboard,
                        placeholder = "Ejemplo: Servicio de consultoria"
                    )


                    Spacer(modifier = Modifier.height(20.dp))
                    ColorSelectorRow(
                        label = "Color",
                        selectedColor = createState.value.color,
                        onColorSelected = { selected ->
                            createState.value = createState.value.copy(color = selected)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    AlternativeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = createState.value.description,
                        onValueChange = { createState.value = createState.value.copy(description = it) },
                        label = "Descripción",
                        keyboardType = KeyboardType.Text,
                        icon = Icons.Default.Info,
                        minLines = 4,
                        maxLines = 4,
                        placeholder = "Describe tu servicio"

                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    AlternativeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = createState.value.durationMinutes,
                        onValueChange = { createState.value = createState.value.copy(durationMinutes = it) },
                        label = "Duración",
                        keyboardType = KeyboardType.Number,
                        icon = Icons.Default.AccessTime,
                        placeholder = "Ingresar la duracion (mins)"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    AlternativeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = createState.value.bufferTime,
                        onValueChange = { createState.value = createState.value.copy(bufferTime = it) },
                        label = "Tiempo de buffer",
                        keyboardType = KeyboardType.Number,
                        icon = Icons.Default.AccessTime,
                        placeholder = "Ingresar el tiempo de espera (mins)"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    AlternativeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = createState.value.price,
                        onValueChange = { createState.value = createState.value.copy(price = it) },
                        label = "Precio",
                        keyboardType = KeyboardType.Decimal,
                        icon = Icons.Default.CurrencyExchange,
                        placeholder = "Ingresar el precio"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    AlternativeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = createState.value.category,
                        onValueChange = { createState.value = createState.value.copy(category = it) },
                        label = "Categoria",
                        keyboardType = KeyboardType.Text,
                        icon = Icons.Default.CurrencyExchange,
                        placeholder = "Selecciona la categoria"
                    )

                    AlternativeTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = createState.value.hidden.toString(),
                        onValueChange = { createState.value = createState.value.copy(hidden = it.isNotBlank()) },
                        label = "Oculto",
                        keyboardType = KeyboardType.Text,
                        icon = Icons.Default.CurrencyExchange,
                        placeholder = "Oculto"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        text = if (editable ) "Editar" else "Crear",
                        enabled = isButtonEnabled,
                        color = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.background,
                        onClick = {
                            if (editable == false){
                            isButtonEnabled = false
                            val request = ServiceCreateRequest(
                                providerId = 1, // Cambiar si necesitas dinámico
                                name = createState.value.name,
                                durationMinutes = createState.value.durationMinutes.toIntOrNull() ?: 0,
                                description = createState.value.description.takeIf { it.isNotBlank() },
                                price = createState.value.price.toDoubleOrNull(),
                                isActive = createState.value.isActive,
                                createdBy = "user", // puedes reemplazar con el usuario actual
                                bufferTime = createState.value.bufferTime.toDoubleOrNull(),
                                category = createState.value.category.takeIf { it.isNotBlank() },
                                color = createState.value.color.takeIf { it.isNotBlank() },
                                hidden = createState.value.hidden
                            )
                            vm.createService(request)

                        }else{
                                isButtonEnabled = false
                                val requestUpdate = ServiceUpdateRequest(
                                    name = createState.value.name,
                                    durationMinutes = createState.value.durationMinutes.toIntOrNull() ?: 0,
                                    description = createState.value.description.takeIf { it.isNotBlank() },
                                    price = createState.value.price.toDoubleOrNull(),
                                    isActive = createState.value.isActive,
                                    updatedBy = "user" ,// puedes reemplazar con el usuario actual
                                    bufferTime = createState.value.bufferTime.toDoubleOrNull(),
                                    category = createState.value.category.takeIf { it.isNotBlank() },
                                    color = createState.value.color.takeIf { it.isNotBlank() },
                                    hidden = createState.value.hidden
                                )
                                serviceId?.let { vm.updateService(it, requestUpdate) }
                            }

                            }
                    )
                }
            }
        }

        // ProgressBar mientras se procesa
        if (editable == true ){
            CustomProgressBar(isLoading = updateResource is Resource.Loading)
        }else {
            CustomProgressBar(isLoading = createResource is Resource.Loading)
        }

    }
}
