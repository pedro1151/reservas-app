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
    val oneClientState by viewModel.oneClientState.collectAsState()

    // Pre-fill con datos existentes cuando editamos
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var dataLoaded by remember { mutableStateOf(false) }

    // Cargar datos del cliente si estamos editando
    LaunchedEffect(clientId) {
        if (editable && clientId != null) {
            viewModel.getClientById(clientId)
        }
    }

    // Cuando llegan los datos, pre-rellenar los campos (solo una vez)
    LaunchedEffect(oneClientState) {
        if (editable && !dataLoaded) {
            val state2 = oneClientState
            if (state2 is Resource.Success) {
                val client = state2.data
                fullName = client.fullName
                email = client.email ?: ""
                phone = client.phone ?: ""
                city = client.city ?: ""
                country = client.country ?: ""
                dataLoaded = true
            }
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
                            if (editable && clientId != null) {
                                // TODO: viewModel.updateClient(clientId, ...)
                            } else {
                                // TODO: viewModel.createClient(...)
                            }
                            navController.popBackStack()
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
            fullName = fullName, onFullNameChange = { fullName = it },
            phone = phone, onPhoneChange = { phone = it },
            email = email, onEmailChange = { email = it },
            company = company, onCompanyChange = { company = it },
            country = country, onCountryChange = { country = it },
            address = address, onAddressChange = { address = it },
            city = city, onCityChange = { city = it },
            state = state, onStateChange = { state = it }
        )
    }
}
