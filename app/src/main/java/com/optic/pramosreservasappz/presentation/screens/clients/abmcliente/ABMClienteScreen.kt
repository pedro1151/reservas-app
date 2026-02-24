package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABMClienteScreen(
    navController: NavHostController,
    clientId: Int?,
    editable: Boolean,
) {
    val viewModel: ClientViewModel = hiltViewModel()
    val formState by viewModel.formState.collectAsState()
    val createState by viewModel.createClientState.collectAsState()
    val updateState by viewModel.updateClientState.collectAsState()
    val oneClientState by viewModel.oneClientState.collectAsState()


    // Cargar datos del cliente si estamos editando
    LaunchedEffect(clientId) {
        if (editable && clientId != null) {
            viewModel.getClientById(clientId)
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.Close, contentDescription = null, tint = Color.Black)
                    }
                },
                title = {
                    Text(
                        text = if (editable) "Editar cliente" else "Agregar cliente",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                },
                actions = {
                    TextButton(
                        onClick = {

                            val state = viewModel.formState.value

                            if (editable && clientId != null) {

                                viewModel.updateClient(
                                    clientId,
                                    ClientUpdateRequest(
                                        fullName = state.fullName,
                                        email = state.email,
                                        phone = state.phone,
                                        enterpriseName = state.enterprise,
                                        country = state.country,
                                        address = state.address,
                                        city = state.city,
                                        state = state.state
                                    )
                                )

                            } else {

                                viewModel.createClient(
                                    ClientCreateRequest(
                                        fullName = state.fullName,
                                        email = state.email,
                                        phone = state.phone,
                                        enterpriseName = state.enterprise,
                                        country = state.country,
                                        address = state.address,
                                        city = state.city,
                                        state = state.state,
                                        providerId = 1
                                    )
                                )
                            }
                        }
                    ) {
                        Text(
                            text = if (editable) "Guardar" else "Crear",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        ABMClienteContent(
            paddingValues = paddingValues,
            navController = navController,
            clientId = clientId,
            editable = editable,
            viewModel = viewModel
        )
    }

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {
            viewModel.resetCreateState()
            viewModel.resetForm()
            navController.popBackStack()
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is Resource.Success) {
            viewModel.resetUpdateState()
            viewModel.resetForm()
            navController.popBackStack()
        }
    }
}
