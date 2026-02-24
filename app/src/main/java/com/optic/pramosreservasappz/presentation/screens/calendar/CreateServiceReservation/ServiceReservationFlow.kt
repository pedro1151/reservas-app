package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

enum class ServiceReservationStep {
    SELECT_SERVICE,
    SELECT_DATETIME,
    SELECT_CLIENT,
    SUMMARY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReservationFlow(
    onDismiss: () -> Unit,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    serviceViewModel: ServiceViewModel = hiltViewModel(),
    clientViewModel: ClientViewModel = hiltViewModel()
) {
    var currentStep by remember { mutableStateOf(ServiceReservationStep.SELECT_SERVICE) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Estados
    val selectedServiceId by calendarViewModel.selectedServiceIdForReservation.collectAsState()
    val selectedClientId by calendarViewModel.selectedClientIdForReservation.collectAsState()
    val selectedDate by calendarViewModel.selectedDateForReservation.collectAsState()
    val selectedTime by calendarViewModel.selectedTimeForReservation.collectAsState()

    val services by serviceViewModel.localServicesList.collectAsState()
    val clients by clientViewModel.localClientsList.collectAsState()

    val selectedService = services.firstOrNull { it.id == selectedServiceId }
    val selectedClient = clients.firstOrNull { it.id == selectedClientId }

    ModalBottomSheet(
        onDismissRequest = {
            calendarViewModel.clearReservationForm()
            onDismiss()
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.96f)  // Aplicar altura aquí
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Contenido con transición
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        ) + fadeIn() togetherWith slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        ) + fadeOut()
                    },
                    label = "step_transition",
                    modifier = Modifier.fillMaxSize()
                ) { step ->
                    when (step) {
                        ServiceReservationStep.SELECT_SERVICE -> {
                            SelectServiceStep(
                                services = services,
                                onBack = {
                                    calendarViewModel.clearReservationForm()
                                    onDismiss()
                                },
                                onServiceSelected = { service ->
                                    calendarViewModel.setSelectedServiceForReservation(service.id)
                                    currentStep = ServiceReservationStep.SELECT_DATETIME
                                }
                            )
                        }

                        ServiceReservationStep.SELECT_DATETIME -> {
                            SelectDateTimeStep(
                                initialDate = selectedDate ?: LocalDate.now(),
                                initialTime = selectedTime,
                                onBack = {
                                    currentStep = ServiceReservationStep.SELECT_SERVICE
                                    calendarViewModel.setSelectedDateForReservation(null)
                                    calendarViewModel.setSelectedTimeForReservation(null)
                                },
                                onConfirm = { date, time ->
                                    calendarViewModel.setSelectedDateForReservation(date)
                                    calendarViewModel.setSelectedTimeForReservation(time)
                                    currentStep = ServiceReservationStep.SELECT_CLIENT
                                }
                            )
                        }

                        ServiceReservationStep.SELECT_CLIENT -> {
                            SelectClientStep(
                                clients = clients,
                                onBack = {
                                    currentStep = ServiceReservationStep.SELECT_DATETIME
                                    calendarViewModel.setSelectedClientForReservation(null)
                                },
                                onClientSelected = { client ->
                                    calendarViewModel.setSelectedClientForReservation(client.id)
                                    currentStep = ServiceReservationStep.SUMMARY
                                }
                            )
                        }

                        ServiceReservationStep.SUMMARY -> {
                            SummaryStep(
                                service = selectedService,
                                client = selectedClient,
                                date = selectedDate,
                                time = selectedTime,
                                onBack = {
                                    currentStep = ServiceReservationStep.SELECT_CLIENT
                                },
                                onCreate = {
                                    // TODO: Llamar API aquí
                                    showSuccessMessage = true
                                }
                            )
                        }
                    }
                }
            }

            // Snackbar de éxito - más visible
            AnimatedVisibility(
                visible = showSuccessMessage,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeOut(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 80.dp)  // Más arriba para que sea visible
            ) {
                SuccessSnackbar()
            }
        }
    }

    // Cerrar automáticamente después del mensaje de éxito
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(2000)  // 2 segundos para que sea más visible
            calendarViewModel.clearReservationForm()
            onDismiss()
        }
    }
}

@Composable
private fun SuccessSnackbar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1A1A1A),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(22.dp)
            )
            Text(
                "Cita creada con éxito",
                fontSize = 15.sp,
                fontWeight = FontWeight.W500,
                color = Color.White
            )
        }
    }
}

// ============ SELECT SERVICE STEP ============
@Composable
private fun SelectServiceStep(
    services: List<ServiceResponse>,
    onBack: () -> Unit,
    onServiceSelected: (ServiceResponse) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredServices = remember(searchQuery, services) {
        if (searchQuery.isBlank()) services
        else services.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        StepHeader(
            title = "Seleccionar un servicio",
            onBack = onBack
        )

        // Búsqueda
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            placeholder = { Text("Buscar", fontSize = 15.sp, color = Color(0xFFAAAAAA)) },
            leadingIcon = {
                Icon(Icons.Outlined.Search, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(18.dp))
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // Lista
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp)
        ) {
            items(filteredServices) { service ->
                ServiceItemRow(service = service, onClick = { onServiceSelected(service) })
            }
        }
    }
}

@Composable
private fun ServiceItemRow(service: ServiceResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = service.name.take(1).uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF424242)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = service.name, fontSize = 15.sp, fontWeight = FontWeight.W500, color = Color.Black)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${service.durationMinutes} mins", fontSize = 13.sp, color = Color(0xFF757575))
                Text("·", fontSize = 13.sp, color = Color(0xFF757575))
                Text(
                    if (service.price == null || service.price == 0.0) "Gratis" else "Bs ${service.price}",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }
        }
        Icon(Icons.Outlined.ChevronRight, null, modifier = Modifier.size(20.dp), tint = Color(0xFFCCCCCC))
    }
}

// ============ SELECT DATETIME STEP ============
@Composable
private fun SelectDateTimeStep(
    initialDate: LocalDate,
    initialTime: LocalTime?,
    onBack: () -> Unit,
    onConfirm: (LocalDate, LocalTime) -> Unit
) {
    SelectDateTimeDialog(
        initialDate = initialDate,
        initialTime = initialTime,
        onDismiss = onBack,
        onConfirm = onConfirm
    )
}

// ============ SELECT CLIENT STEP ============
@Composable
private fun SelectClientStep(
    clients: List<ClientResponse>,
    onBack: () -> Unit,
    onClientSelected: (ClientResponse) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredClients = remember(searchQuery, clients) {
        if (searchQuery.isBlank()) clients
        else clients.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                    it.email?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        StepHeader(title = "Seleccionar invitado(s)", onBack = onBack)

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            placeholder = { Text("Buscar", fontSize = 15.sp, color = Color(0xFFAAAAAA)) },
            leadingIcon = {
                Icon(Icons.Outlined.Search, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFFAAAAAA), modifier = Modifier.size(18.dp))
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* TODO */ }
                )
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Add, null, tint = Color(0xFF424242), modifier = Modifier.size(20.dp))
            Text("Añadir nuevo cliente", fontSize = 15.sp, color = Color(0xFF424242), fontWeight = FontWeight.W400)
        }

        HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp)
        ) {
            items(filteredClients) { client ->
                ClientItemRow(client = client, onClick = { onClientSelected(client) })
            }
        }
    }
}

@Composable
private fun ClientItemRow(client: ClientResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(getAvatarColor(client.id), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getInitials(client.fullName),
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = Color.White
            )
        }
        Text(
            text = client.fullName,
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Outlined.ChevronRight, null, modifier = Modifier.size(20.dp), tint = Color(0xFFCCCCCC))
    }
}

// ============ SUMMARY STEP ============
@Composable
private fun SummaryStep(
    service: ServiceResponse?,
    client: ClientResponse?,
    date: LocalDate?,
    time: LocalTime?,
    onBack: () -> Unit,
    onCreate: () -> Unit
) {
    ReservationSummarySheet(
        service = service,
        client = client,
        date = date,
        time = time,
        onDismiss = onBack,
        onCreate = onCreate
    )
}

// ============ HELPERS ============
@Composable
private fun StepHeader(title: String, onBack: () -> Unit) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.Black, modifier = Modifier.size(24.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 17.sp
            )
        }
        HorizontalDivider(color = Color(0xFFF0F0F0))
    }
}

private fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}

private fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF1A1A1A), Color(0xFF555555), Color(0xFF4A6CF7),
        Color(0xFF7C3AED), Color(0xFF059669), Color(0xFFDC2626),
        Color(0xFFD97706), Color(0xFF0891B2), Color(0xFFDB2777)
    )
    return colors[id % colors.size]
}
