package com.optic.pramosreservasappz.presentation.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    navController: NavHostController,
    serviceId: Int,
    viewModel: ServiceViewModel = hiltViewModel()
) {
    val serviceState by viewModel.serviceState.collectAsState()

    LaunchedEffect(serviceId) {
        viewModel.getServiceById(serviceId)
    }

    when (val state = serviceState) {
        is Resource.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        is Resource.Success -> {
            ServiceDetailContent(service = state.data, navController = navController)
        }
        is Resource.Failure -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se pudo cargar el servicio", color = Color(0xFFAAAAAA), fontSize = 14.sp)
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServiceDetailContent(
    service: ServiceResponse,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()

    val serviceColor = remember(service.color) {
        if (!service.color.isNullOrBlank()) {
            try { Color(android.graphics.Color.parseColor(service.color)) }
            catch (e: Exception) { Color(0xFF4A6CF7) }
        } else Color(0xFF4A6CF7)
    }

    val initials = remember(service.name) {
        val parts = service.name.trim().split(" ")
        when {
            parts.isEmpty() -> "?"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
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
                title = {},
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            ClientScreen.ABMServicio.createRoute(
                                serviceId = service.id,
                                editable = true
                            )
                        )
                    }) {
                        Icon(Icons.Outlined.Edit, null, tint = Color.Black, modifier = Modifier.size(22.dp))
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.MoreVert, null, tint = Color.Black, modifier = Modifier.size(22.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            // Avatar con iniciales y color del servicio
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(serviceColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                // Barra de color izquierda
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(serviceColor)
                        .align(Alignment.CenterStart)
                )
                Text(
                    text = initials,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = serviceColor,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            // Nombre del servicio
            Text(
                text = service.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                letterSpacing = (-0.3).sp
            )

            // Duración y precio
            Spacer(Modifier.height(6.dp))
            Text(
                text = buildString {
                    append("${service.durationMinutes} min")
                    service.price?.let {
                        if (it == 0.0) append(" · Gratis") else append(" · Bs ${it.toInt()}")
                    }
                },
                fontSize = 14.sp,
                color = Color(0xFFAAAAAA)
            )

            Spacer(Modifier.height(20.dp))

            // Botón copiar enlace
            OutlinedButton(
                onClick = { /* TODO: copiar enlace */ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Icon(Icons.Outlined.Link, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Copiar enlace", fontSize = 14.sp)
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

            // Campos de información
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                ServiceDetailField(
                    label = "Duración",
                    value = "${service.durationMinutes} min"
                )
                ServiceDetailField(
                    label = "Precio",
                    value = service.price?.let {
                        if (it == 0.0) "Gratis" else "Bs ${it.toInt()}"
                    } ?: "No especificado"
                )
                if (!service.description.isNullOrBlank()) {
                    ServiceDetailField(label = "Descripción", value = service.description)
                }
                if (!service.category.isNullOrBlank()) {
                    ServiceDetailField(label = "Categoría", value = service.category)
                }
                ServiceDetailField(
                    label = "Estado",
                    value = if (service.isActive == true) "Activo" else "Inactivo",
                    valueColor = if (service.isActive == true) Color(0xFF059669) else Color(0xFFAAAAAA)
                )
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

            // Sección citas
            DetailNavRow(
                title = "Citas con este servicio",
                onClick = { /* TODO */ }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ServiceDetailField(
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF888888),
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            modifier = Modifier.weight(1f)
        )
    }
    HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 0.5.dp)
}

@Composable
private fun DetailNavRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Icon(Icons.Outlined.ChevronRight, null, tint = Color(0xFFCCCCCC), modifier = Modifier.size(20.dp))
    }
}
