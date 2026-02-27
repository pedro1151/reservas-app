package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

enum class ServiceReservationStep(val index: Int, val title: String) {
    SELECT_SERVICE(0, "Seleccionar servicio"),
    SELECT_DATETIME(1, "Fecha y hora"),
    SELECT_CLIENT(2, "Invitado"),
    SUMMARY(3, "Resumen")
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
    var isForward   by remember { mutableStateOf(true) }
    var showSuccess by remember { mutableStateOf(false) }

    val selectedServiceId by calendarViewModel.selectedServiceIdForReservation.collectAsState()
    val selectedClientId  by calendarViewModel.selectedClientIdForReservation.collectAsState()
    val selectedDate      by calendarViewModel.selectedDateForReservation.collectAsState()
    val selectedTime      by calendarViewModel.selectedTimeForReservation.collectAsState()
    val services          by serviceViewModel.localServicesList.collectAsState()
    val clients           by clientViewModel.localClientsList.collectAsState()

    val selectedService = services.firstOrNull { it.id == selectedServiceId }
    val selectedClient  = clients.firstOrNull  { it.id == selectedClientId  }

    fun goTo(step: ServiceReservationStep, forward: Boolean = true) {
        isForward   = forward
        currentStep = step
    }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            delay(2_200)
            calendarViewModel.clearReservationForm()
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { calendarViewModel.clearReservationForm(); onDismiss() },
        containerColor   = Color.White,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle       = null,
        modifier         = Modifier.fillMaxHeight(0.97f)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Drag handle
            Box(
                Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.width(40.dp).height(4.dp).background(Color(0xFFDDDDDD), CircleShape))
            }

            // Header con progreso
            StepHeader(currentStep = currentStep, onBack = {
                when (currentStep) {
                    ServiceReservationStep.SELECT_SERVICE  -> { calendarViewModel.clearReservationForm(); onDismiss() }
                    ServiceReservationStep.SELECT_DATETIME -> goTo(ServiceReservationStep.SELECT_SERVICE, false)
                    ServiceReservationStep.SELECT_CLIENT   -> goTo(ServiceReservationStep.SELECT_DATETIME, false)
                    ServiceReservationStep.SUMMARY         -> goTo(ServiceReservationStep.SELECT_CLIENT, false)
                }
            })

            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

            // ── Contenido + snackbar en Box separado del Column principal ──────
            // La clave está en StepContentWithSnackbar: es una función @Composable
            // independiente, sin ColumnScope en su scope, por lo que AnimatedVisibility
            // resuelve al overload standalone correcto (no ColumnScope).
            StepContentWithSnackbar(
                modifier      = Modifier.weight(1f),
                currentStep   = currentStep,
                isForward     = isForward,
                showSuccess   = showSuccess,
                services      = services,
                clients       = clients,
                selectedService = selectedService,
                selectedClient  = selectedClient,
                selectedDate    = selectedDate,
                selectedTime    = selectedTime,
                onServiceSelected = { s ->
                    calendarViewModel.setSelectedServiceForReservation(s.id)
                    goTo(ServiceReservationStep.SELECT_DATETIME)
                },
                onDateTimeConfirmed = { date, time ->
                    calendarViewModel.setSelectedDateForReservation(date)
                    calendarViewModel.setSelectedTimeForReservation(time)
                    goTo(ServiceReservationStep.SELECT_CLIENT)
                },
                onClientSelected = { c ->
                    calendarViewModel.setSelectedClientForReservation(c.id)
                    goTo(ServiceReservationStep.SUMMARY)
                },
                onCreate = {
                    // TODO: API — calendarViewModel.createReservation(
                    //     ReservationCreateRequest(
                    //         serviceId = selectedServiceId!!,
                    //         clientId  = selectedClientId!!,
                    //         date      = selectedDate.toString(),
                    //         time      = selectedTime.toString()
                    //     )
                    // )
                    showSuccess = true
                }
            )
        }
    }
}

// ── FUNCIÓN SEPARADA — sin ColumnScope en scope, AnimatedVisibility resuelve correcto ──
@Composable
private fun StepContentWithSnackbar(
    modifier: Modifier,
    currentStep: ServiceReservationStep,
    isForward: Boolean,
    showSuccess: Boolean,
    services: List<ServiceResponse>,
    clients: List<ClientResponse>,
    selectedService: ServiceResponse?,
    selectedClient: ClientResponse?,
    selectedDate: LocalDate?,
    selectedTime: LocalTime?,
    onServiceSelected: (ServiceResponse) -> Unit,
    onDateTimeConfirmed: (LocalDate, LocalTime) -> Unit,
    onClientSelected: (ClientResponse) -> Unit,
    onCreate: () -> Unit
) {
    Box(modifier = modifier) {

        AnimatedContent(
            targetState  = currentStep,
            transitionSpec = {
                val inOff  = if (isForward)  300 else -300
                val outOff = if (isForward) -300 else  300
                (slideInHorizontally(tween(280, easing = FastOutSlowInEasing)) { inOff } +
                        fadeIn(tween(220))) togetherWith
                        (slideOutHorizontally(tween(250, easing = FastOutSlowInEasing)) { outOff } +
                                fadeOut(tween(200)))
            },
            label    = "step_anim",
            modifier = Modifier.fillMaxSize()
        ) { step ->
            when (step) {
                ServiceReservationStep.SELECT_SERVICE  ->
                    SelectServiceStep(services, onServiceSelected)
                ServiceReservationStep.SELECT_DATETIME ->
                    SelectDateTimeStep(selectedDate ?: LocalDate.now(), selectedTime, onDateTimeConfirmed)
                ServiceReservationStep.SELECT_CLIENT   ->
                    SelectClientStep(clients, onClientSelected)
                ServiceReservationStep.SUMMARY         ->
                    SummaryStep(selectedService, selectedClient, selectedDate, selectedTime, onCreate)
            }
        }

        // AnimatedVisibility aquí NO tiene ColumnScope en scope → resuelve al overload correcto
        AnimatedVisibility(
            visible  = showSuccess,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec  = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
            ) + fadeIn(),
            exit  = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(250)
            ) + fadeOut()
        ) {
            SuccessSnackbar()
        }
    }
}

// ── Step header ────────────────────────────────────────────────────────────────
@Composable
private fun StepHeader(currentStep: ServiceReservationStep, onBack: () -> Unit) {
    Column {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 10.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    imageVector = if (currentStep == ServiceReservationStep.SELECT_SERVICE)
                        Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint     = Color(0xFF1A1A1A),
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text          = currentStep.title,
                fontSize      = 16.sp,
                fontWeight    = FontWeight.W600,
                color         = Color(0xFF111111),
                letterSpacing = (-0.3).sp,
                modifier      = Modifier.align(Alignment.Center)
            )
        }
        val progress by animateFloatAsState(
            targetValue   = (currentStep.index + 1).toFloat() / 4f,
            animationSpec = tween(350, easing = FastOutSlowInEasing),
            label         = "progress"
        )
        Box(Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFF0F0F0))) {
            Box(Modifier.fillMaxWidth(progress).fillMaxHeight().background(Color(0xFF111111), RoundedCornerShape(1.dp)))
        }
    }
}

// ── Step 1: Servicios ──────────────────────────────────────────────────────────
@Composable
private fun SelectServiceStep(
    services: List<ServiceResponse>,
    onServiceSelected: (ServiceResponse) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, services) {
        if (query.isBlank()) services
        else services.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description?.contains(query, ignoreCase = true) == true
        }
    }
    Column(Modifier.fillMaxSize()) {
        TextField(
            value = query, onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
            placeholder  = { Text("Buscar servicio", fontSize = 14.sp, color = Color(0xFFBBBBBB)) },
            leadingIcon  = { Icon(Icons.Outlined.Search, null, tint = Color(0xFFBBBBBB), modifier = Modifier.size(19.dp)) },
            trailingIcon = {
                if (query.isNotBlank()) IconButton({ query = "" }) {
                    Icon(Icons.Default.Close, null, tint = Color(0xFFBBBBBB), modifier = Modifier.size(17.dp))
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = Color(0xFFF6F6F6),
                unfocusedContainerColor = Color(0xFFF6F6F6),
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor             = Color.Black
            ),
            shape = RoundedCornerShape(14.dp), singleLine = true
        )
        if (filtered.isEmpty()) EmptyState("Sin servicios")
        else LazyColumn(contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp)) {
            items(filtered, key = { it.id }) { service ->
                ServiceRow(service) { onServiceSelected(service) }
                HorizontalDivider(color = Color(0xFFF5F5F5))
            }
        }
    }
}

@Composable
private fun ServiceRow(service: ServiceResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Box(Modifier.size(42.dp).background(Color(0xFFF0F0F0), RoundedCornerShape(10.dp)), Alignment.Center) {
            Text(service.name.take(1).uppercase(), fontSize = 16.sp, fontWeight = FontWeight.W700, color = Color(0xFF333333))
        }
        Column(Modifier.weight(1f)) {
            Text(service.name, fontSize = 15.sp, fontWeight = FontWeight.W500, color = Color(0xFF111111))
            Spacer(Modifier.height(2.dp))
            Text(
                "${service.durationMinutes} min  ·  ${if (service.price == null || service.price == 0.0) "Gratis" else "Bs ${service.price}"}",
                fontSize = 13.sp, color = Color(0xFF9E9E9E)
            )
        }
        Icon(Icons.Outlined.ChevronRight, null, modifier = Modifier.size(18.dp), tint = Color(0xFFCCCCCC))
    }
}

// ── Step 2: Fecha y hora ───────────────────────────────────────────────────────
@Composable
private fun SelectDateTimeStep(
    initialDate: LocalDate,
    initialTime: LocalTime?,
    onConfirm: (LocalDate, LocalTime) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }
    var selectedTime by remember { mutableStateOf(initialTime) }
    val scroll = rememberScrollState()

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(scroll)) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton({ currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(Icons.Default.ChevronLeft, null, tint = Color(0xFF555555))
                }
                Text(
                    "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es","ES")).replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                    fontSize = 15.sp, fontWeight = FontWeight.W600, color = Color(0xFF111111)
                )
                IconButton({ currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF555555))
                }
            }
            InlineCalendar(currentMonth, selectedDate) { selectedDate = it }
            Spacer(Modifier.height(20.dp))
            Text(
                "Hora", fontSize = 13.sp, fontWeight = FontWeight.W600,
                color = Color(0xFF9E9E9E), letterSpacing = 0.8.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            InlineTimeGrid(selectedTime) { selectedTime = it }
            Spacer(Modifier.height(100.dp))
        }
        Surface(color = Color.White, shadowElevation = 6.dp) {
            Button(
                onClick  = { selectedTime?.let { onConfirm(selectedDate, it) } },
                enabled  = selectedTime != null,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = Color(0xFF111111),
                    disabledContainerColor = Color(0xFFE0E0E0),
                    contentColor           = Color.White,
                    disabledContentColor   = Color(0xFFAAAAAA)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Confirmar", fontSize = 15.sp, fontWeight = FontWeight.W600, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
private fun InlineCalendar(currentMonth: YearMonth, selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val today          = LocalDate.now()
    val firstDay       = currentMonth.atDay(1)
    val daysInMonth    = currentMonth.lengthOfMonth()
    val firstDayOfWeek = firstDay.dayOfWeek.value

    Column(Modifier.padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth()) {
            listOf("L","M","M","J","V","S","D").forEach { label ->
                Text(label, Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, fontWeight = FontWeight.W500, color = Color(0xFFBBBBBB))
            }
        }
        Spacer(Modifier.height(10.dp))
        val weeks = ((daysInMonth + firstDayOfWeek - 1) + 6) / 7
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            for (week in 0 until weeks) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (dow in 1..7) {
                        val dom = week * 7 + dow - firstDayOfWeek + 1
                        if (dom in 1..daysInMonth) {
                            val date    = currentMonth.atDay(dom)
                            val isSel   = date == selectedDate
                            val isToday = date == today
                            Box(
                                Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF111111) else Color.Transparent)
                                    .clickable { onDateSelected(date) },
                                Alignment.Center
                            ) {
                                Text(dom.toString(), fontSize = 14.sp,
                                    fontWeight = if (isSel || isToday) FontWeight.W700 else FontWeight.W400,
                                    color = when { isSel -> Color.White; isToday -> Color(0xFF111111); else -> Color(0xFF555555) })
                                if (isToday && !isSel) {
                                    Box(Modifier.size(4.dp).background(Color(0xFF111111), CircleShape)
                                        .align(Alignment.BottomCenter).offset(y = (-3).dp))
                                }
                            }
                        } else Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun InlineTimeGrid(selectedTime: LocalTime?, onTimeSelected: (LocalTime) -> Unit) {
    val slots = remember { buildList { for (h in 7..21) for (m in listOf(0, 30)) add(LocalTime.of(h, m)) } }
    val fmt   = remember { DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH) }
    LazyVerticalGrid(
        columns               = GridCells.Fixed(4),
        modifier              = Modifier.fillMaxWidth().height(320.dp).padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp)
    ) {
        items(slots) { time ->
            val sel = time == selectedTime
            Box(
                Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(10.dp))
                    .background(if (sel) Color(0xFF111111) else Color(0xFFF6F6F6))
                    .clickable { onTimeSelected(time) },
                Alignment.Center
            ) {
                Text(time.format(fmt), fontSize = 11.sp,
                    fontWeight = if (sel) FontWeight.W700 else FontWeight.W400,
                    color = if (sel) Color.White else Color(0xFF555555))
            }
        }
    }
}

// ── Step 3: Clientes ───────────────────────────────────────────────────────────
@Composable
private fun SelectClientStep(
    clients: List<ClientResponse>,
    onClientSelected: (ClientResponse) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, clients) {
        if (query.isBlank()) clients
        else clients.filter {
            it.fullName.contains(query, ignoreCase = true) ||
                    it.email?.contains(query, ignoreCase = true) == true
        }
    }
    Column(Modifier.fillMaxSize()) {
        TextField(
            value = query, onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
            placeholder  = { Text("Buscar cliente", fontSize = 14.sp, color = Color(0xFFBBBBBB)) },
            leadingIcon  = { Icon(Icons.Outlined.Search, null, tint = Color(0xFFBBBBBB), modifier = Modifier.size(19.dp)) },
            trailingIcon = {
                if (query.isNotBlank()) IconButton({ query = "" }) {
                    Icon(Icons.Default.Close, null, tint = Color(0xFFBBBBBB), modifier = Modifier.size(17.dp))
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = Color(0xFFF6F6F6),
                unfocusedContainerColor = Color(0xFFF6F6F6),
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor             = Color.Black
            ),
            shape = RoundedCornerShape(14.dp), singleLine = true
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable(remember { MutableInteractionSource() }, indication = null) {
                    // TODO: API — navController.navigate(Routes.CreateClient)
                }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Box(Modifier.size(42.dp).background(Color(0xFFF6F6F6), RoundedCornerShape(10.dp)), Alignment.Center) {
                Icon(Icons.Outlined.PersonAdd, null, tint = Color(0xFF555555), modifier = Modifier.size(18.dp))
            }
            Text("Añadir nuevo cliente", fontSize = 15.sp, color = Color(0xFF333333), fontWeight = FontWeight.W400)
        }
        HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(horizontal = 20.dp))
        if (filtered.isEmpty()) EmptyState("Sin clientes")
        else LazyColumn(contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp)) {
            items(filtered, key = { it.id }) { client ->
                ClientRow(client) { onClientSelected(client) }
                HorizontalDivider(color = Color(0xFFF5F5F5))
            }
        }
    }
}

@Composable
private fun ClientRow(client: ClientResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Box(Modifier.size(42.dp).background(avatarColor(client.id), CircleShape), Alignment.Center) {
            Text(initials(client.fullName), fontSize = 14.sp, fontWeight = FontWeight.W700, color = Color.White)
        }
        Column(Modifier.weight(1f)) {
            Text(client.fullName, fontSize = 15.sp, fontWeight = FontWeight.W500, color = Color(0xFF111111))
            client.email?.let { Text(it, fontSize = 12.sp, color = Color(0xFF9E9E9E)) }
        }
        Icon(Icons.Outlined.ChevronRight, null, modifier = Modifier.size(18.dp), tint = Color(0xFFCCCCCC))
    }
}

// ── Step 4: Resumen ────────────────────────────────────────────────────────────
@Composable
private fun SummaryStep(
    service: ServiceResponse?,
    client: ClientResponse?,
    date: LocalDate?,
    time: LocalTime?,
    onCreate: () -> Unit
) {
    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm") }
    Column(Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)) {
            if (service != null) item {
                SummaryCard {
                    SummaryRow(Icons.Outlined.MiscellaneousServices, Color(0xFF009688), "Servicio", service.name)
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    Row(Modifier.fillMaxWidth().padding(start = 44.dp, top = 12.dp, bottom = 12.dp), Arrangement.spacedBy(40.dp)) {
                        LabelValue("Costo", "Bs ${service.price ?: 0}")
                        LabelValue("Duración", "${service.durationMinutes} min")
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            if (date != null && time != null) item {
                val dow = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es","ES")).replaceFirstChar { it.uppercase() }
                val end = time.plusMinutes(service?.durationMinutes?.toLong() ?: 0)
                SummaryCard {
                    SummaryRow(Icons.Outlined.CalendarToday, Color(0xFF9E9E9E), "Fecha",
                        "$dow ${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.SHORT, Locale("es","ES"))} ${date.year}")
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    SummaryRow(Icons.Outlined.AccessTime, Color(0xFF9E9E9E), "Hora",
                        "${time.format(timeFmt)} – ${end.format(timeFmt)}")
                }
                Spacer(Modifier.height(12.dp))
            }
            if (client != null) item {
                SummaryCard {
                    Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.spacedBy(14.dp), Alignment.CenterVertically) {
                        Box(Modifier.size(44.dp).background(avatarColor(client.id), CircleShape), Alignment.Center) {
                            Text(initials(client.fullName), fontSize = 15.sp, fontWeight = FontWeight.W700, color = Color.White)
                        }
                        Column(Modifier.weight(1f)) {
                            Text(client.fullName, fontSize = 15.sp, fontWeight = FontWeight.W500, color = Color(0xFF111111))
                            client.email?.let { Text(it, fontSize = 12.sp, color = Color(0xFF9E9E9E)) }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            item {
                SummaryCard {
                    SummaryRow(Icons.Outlined.Repeat, Color(0xFF9E9E9E), "Repetir", "No se repite")
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    SummaryRow(Icons.Outlined.Description, Color(0xFF9E9E9E), "Notas internas",
                        "") // TODO: API — calendarViewModel.internalNoteForReservation.collectAsState()
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    SummaryRow(Icons.Outlined.LocalOffer, Color(0xFF9E9E9E), "Etiqueta", "Sin etiqueta")
                }
            }
        }
        Surface(color = Color.White, shadowElevation = 6.dp) {
            Button(
                onClick  = onCreate,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF111111), contentColor = Color.White),
                shape    = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Crear cita", fontSize = 15.sp, fontWeight = FontWeight.W600, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

// ── Reusables ──────────────────────────────────────────────────────────────────
@Composable
private fun SummaryCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFF9F9F9), modifier = Modifier.fillMaxWidth()) {
        Column(content = content)
    }
}

@Composable
private fun SummaryRow(icon: ImageVector, iconTint: Color, label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), Arrangement.spacedBy(12.dp), Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = iconTint)
        Column(Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, color = Color(0xFF9E9E9E), fontWeight = FontWeight.W400)
            if (value.isNotBlank()) Text(value, fontSize = 15.sp, color = Color(0xFF111111), fontWeight = FontWeight.W400)
        }
    }
}

@Composable
private fun LabelValue(label: String, value: String) {
    Column {
        Text(label, fontSize = 12.sp, color = Color(0xFF9E9E9E))
        Text(value, fontSize = 15.sp, color = Color(0xFF111111), fontWeight = FontWeight.W500)
    }
}

@Composable
private fun EmptyState(label: String) {
    Box(Modifier.fillMaxWidth().padding(top = 80.dp), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.SearchOff, null, tint = Color(0xFFDDDDDD), modifier = Modifier.size(40.dp))
            Text(label, fontSize = 13.sp, color = Color(0xFFBBBBBB))
        }
    }
}

@Composable
private fun SuccessSnackbar() {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(14.dp), Color(0xFF111111), shadowElevation = 10.dp) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), Arrangement.spacedBy(14.dp), Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(22.dp))
            Column {
                Text("Cita creada con éxito", fontSize = 15.sp, fontWeight = FontWeight.W600, color = Color.White)
                Text("Aparecerá en tu calendario", fontSize = 12.sp, color = Color(0xFF888888))
            }
        }
    }
}

// ── Helpers ────────────────────────────────────────────────────────────────────
private fun initials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}

private fun avatarColor(id: Int): Color {
    val palette = listOf(
        Color(0xFF1A1A1A), Color(0xFF4A6CF7), Color(0xFF7C3AED),
        Color(0xFF059669), Color(0xFFDC2626), Color(0xFFD97706),
        Color(0xFF0891B2), Color(0xFFDB2777), Color(0xFF555555)
    )
    return palette[id % palette.size]
}
