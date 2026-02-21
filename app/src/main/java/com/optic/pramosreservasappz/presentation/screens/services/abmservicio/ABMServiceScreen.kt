package com.optic.pramosreservasappz.presentation.screens.services.abmservicio

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
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

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var bufferTime by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isHidden by remember { mutableStateOf(false) }
    var dataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(serviceId) {
        if (editable && serviceId != null) {
            viewModel.getServiceById(serviceId)
        }
    }

    LaunchedEffect(serviceState) {
        if (editable && !dataLoaded) {
            val state = serviceState
            if (state is Resource.Success) {
                val s = state.data
                title = s.name
                description = s.description ?: ""
                duration = s.durationMinutes.toString()
                price = s.price?.toString() ?: ""
                category = s.category ?: ""
                dataLoaded = true
            }
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
                            if (editable && serviceId != null) {
                                // TODO: viewModel.updateService(serviceId, ...)
                            } else {
                                // TODO: viewModel.createService(...)
                            }
                            navController.popBackStack()
                        },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEEEEE),
                            contentColor = Color(0xFF888888)
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 16.dp,
                            vertical = 6.dp
                        )
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
            title = title, onTitleChange = { title = it },
            description = description, onDescriptionChange = { description = it },
            duration = duration, onDurationChange = { duration = it },
            bufferTime = bufferTime, onBufferTimeChange = { bufferTime = it },
            price = price, onPriceChange = { price = it },
            category = category, onCategoryChange = { category = it },
            isHidden = isHidden, onHiddenChange = { isHidden = it }
        )
    }
}
