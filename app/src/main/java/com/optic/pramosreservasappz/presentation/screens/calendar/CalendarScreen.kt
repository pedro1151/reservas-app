package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.screens.calendar.components.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

enum class CalendarViewMode {
    AGENDA, DAY, THREE_DAYS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    var viewMode by remember { mutableStateOf(CalendarViewMode.AGENDA) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedDateReservations by viewModel.selectedDateReservations.collectAsState()
    val weeklyEarnings by viewModel.weeklyEarnings.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CalendarDrawerContent(
                currentViewMode = viewMode,
                onViewModeChange = {
                    viewMode = it
                    scope.launch { drawerState.close() }
                },
                onDrawerClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CalendarTopAppBar(
                    currentDate = selectedDate,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onDateClick = { }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Agregar",
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            bottomBar = {
                CalendarBottomBar(
                    selectedTab = 0,
                    onTabSelected = { }
                )
            },
            containerColor = Color(0xFFFAFAFA)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Semana horizontal siempre visible
                    WeekStripHeader(
                        selectedDate = selectedDate,
                        onDateSelect = { viewModel.selectDate(it) }
                    )

                    HorizontalDivider(color = Color(0xFFE0E0E0))

                    // Contenido según modo
                    Box(modifier = Modifier.weight(1f)) {
                        when (viewMode) {
                            CalendarViewMode.AGENDA -> {
                                WeekAgendaView(
                                    selectedDate = selectedDate,
                                    reservations = selectedDateReservations,
                                    onDateSelect = { viewModel.selectDate(it) }
                                )
                            }
                            CalendarViewMode.DAY -> {
                                DayTimelineView(
                                    selectedDate = selectedDate,
                                    reservations = selectedDateReservations,
                                    onTimeSlotClick = { showCreateDialog = true }
                                )
                            }
                            CalendarViewMode.THREE_DAYS -> {
                                ThreeDaysView(
                                    selectedDate = selectedDate,
                                    onDateSelect = { viewModel.selectDate(it) }
                                )
                            }
                        }
                    }
                }

                // Banner flotante "Comenzar prueba gratuita"
                PromoBanner(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp, start = 16.dp, end = 80.dp)
                )
            }
        }
    }

    // Bottom sheet para crear
    if (showCreateDialog) {
        CreateEventBottomSheet(onDismiss = { showCreateDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarTopAppBar(
    currentDate: LocalDate,
    onMenuClick: () -> Unit,
    onDateClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable(onClick = onDateClick)
            ) {
                Text(
                    text = currentDate.month
                        .getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 20.sp
                )
                Text(
                    text = currentDate.year.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9E9E9E),
                    fontSize = 20.sp
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.Black
                )
            }
            IconButton(onClick = { }) {
                Surface(
                    modifier = Modifier.size(34.dp),
                    shape = RoundedCornerShape(17.dp),
                    color = Color(0xFFE0E0E0)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "JT",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
private fun WeekStripHeader(
    selectedDate: LocalDate,
    onDateSelect: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    // Calculamos el lunes de la semana actual del selectedDate
    val monday = selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() - 1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val dayLabels = listOf("L", "M", "M", "J", "V", "S", "D")
        dayLabels.forEachIndexed { index, label ->
            val date = monday.plusDays(index.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDateSelect(date) }
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = when {
                                isSelected && isToday -> Color.Black
                                isSelected -> Color.Black
                                isToday -> Color(0xFFEEEEEE)
                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                        color = when {
                            isSelected -> Color.White
                            isToday -> Color.Black
                            else -> Color(0xFF424242)
                        },
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PromoBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFE8F5E9),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FlightTakeoff,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF2E7D32)
            )
            Text(
                text = "Comenzar prueba gratuita",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CalendarBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.height(70.dp)
    ) {
        NavigationBarItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(if (selectedTab == 0) 36.dp else 0.dp)
                        .background(Color.Black, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedTab == 0) {
                        Text(
                            text = LocalDate.now().dayOfMonth.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            },
            label = {
                Text(
                    "Calendario",
                    fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal
                )
            },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = Color.Black,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.List, contentDescription = null) },
            label = { Text("Servicios") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = Color.Black,
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.SentimentSatisfied, contentDescription = null) },
            label = { Text("Clientes") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = Color.Black,
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            label = { Text("Configuración") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = Color.Black,
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEventBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        ) {
            CreateOption(Icons.Outlined.List, "Servicio")
            CreateOption(Icons.Outlined.School, "Clase")
            CreateOption(Icons.Outlined.Event, "Evento")
            CreateOption(Icons.Outlined.CalendarToday, "Reunión única")

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Outlined.AttachMoney,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                    Text(
                        "Pago rápido",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Conectar", color = Color.Black)
                }
            }

            CreateOption(Icons.Outlined.People, "Miembro del equipo")
            CreateOption(Icons.Outlined.Person, "Cliente")

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CreateOption(Icons.Outlined.QrCode, "Compartir página de reservas")
        }
    }
}

@Composable
private fun CreateOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Black)
        Text(title, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
    }
}
