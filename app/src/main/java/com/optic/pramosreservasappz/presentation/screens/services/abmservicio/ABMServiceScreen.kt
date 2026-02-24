package com.optic.pramosreservasappz.presentation.screens.services.abmservicio

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABMServiceScreen(
    navController: NavHostController,
    serviceId: Int?,
    editable: Boolean,
) {
    val viewModel: ServiceViewModel = hiltViewModel()
    val serviceState by viewModel.serviceState.collectAsState()

    LaunchedEffect(serviceId) {
        if (editable && serviceId != null) {
            viewModel.getServiceById(serviceId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, null, tint = Color.Black)
                    }
                },
                title = {
                    Text(
                        text = if (editable) "Editar servicio" else "Agregar servicio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                },
                actions = {
                    // Tres puntos opcionales
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.MoreVert, null, tint = Color.Black)
                    }
                    // Botón Guardar como chip redondeado
                    Button(
                        onClick = {

                            val state = viewModel.formState.value

                            if (editable && serviceId != null) {

                                viewModel.updateService(
                                    serviceId,
                                    ServiceUpdateRequest(
                                        name = state.name,
                                        description = state.description,
                                        price = state.price.toDoubleOrNull(),
                                        durationMinutes = state.durationMinutes.toIntOrNull() ?: 0,
                                        bufferTime = state.bufferTime.toIntOrNull(),
                                        category = state.category,
                                        hidden = state.hidden
                                    )
                                )

                            } else {

                                viewModel.createService(
                                    ServiceCreateRequest(
                                        name = state.name,
                                        description = state.description,
                                        price = state.price.toDoubleOrNull(),
                                        durationMinutes = state.durationMinutes.toIntOrNull() ?: 0,
                                        bufferTime = state.bufferTime.toIntOrNull(),
                                        category = state.category,
                                        hidden = state.hidden,
                                        providerId = 1 // tu provider real
                                    )
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "Guardar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF888888)
                        )
                    }
                    androidx.compose.foundation.layout.Spacer(
                        modifier = androidx.compose.ui.Modifier.width(8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        ABMServiceContent(
            paddingValues = paddingValues,
            navController = navController,
            serviceId = serviceId,
            editable = editable,
            viewModel = viewModel
        )
    }
}
