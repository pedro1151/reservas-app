package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.optic.pramosreservasappz.presentation.screens.calendar.components.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

enum class CalendarViewMode { AGENDA, DAY, THREE_DAYS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel(),
    activityViewModel: ActivityViewModel = hiltViewModel()
) {
    var viewMode by remember { mutableStateOf(CalendarViewMode.AGENDA) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showActivitySheet by remember { mutableStateOf(false) }

    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedDateReservations by viewModel.selectedDateReservations.collectAsState()
    val activities by activityViewModel.activities.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
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
                MinimalTopBar(
                    currentDate = selectedDate,
                    hasActivity = activities.isNotEmpty(),
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBellClick = { showActivitySheet = true },
                    onDateClick = { }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(22.dp))
                }
            },
            bottomBar = { MinimalBottomBar() },
            containerColor = Color(0xFFF8F8F8)
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    WeekStrip(
                        selectedDate = selectedDate,
                        onDateSelect = { viewModel.selectDate(it) }
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        when (viewMode) {
                            CalendarViewMode.AGENDA -> WeekAgendaView(
                                selectedDate = selectedDate,
                                reservations = selectedDateReservations,
                                onDateSelect = { viewModel.selectDate(it) }
                            )
                            CalendarViewMode.DAY -> DayTimelineView(
                                selectedDate = selectedDate,
                                reservations = selectedDateReservations,
                                onTimeSlotClick = { showCreateDialog = true }
                            )
                            CalendarViewMode.THREE_DAYS -> ThreeDaysView(
                                selectedDate = selectedDate,
                                onDateSelect = { viewModel.selectDate(it) }
                            )
                        }
                    }
                }

                PromoPill(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp, end = 68.dp)
                )
            }
        }
    }

    if (showCreateDialog) {
        CreateSheet(onDismiss = { showCreateDialog = false })
    }

    if (showActivitySheet) {
        ActivityBottomSheet(
            activities = activities,
            onDismiss = { showActivitySheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MinimalTopBar(
    currentDate: LocalDate,
    hasActivity: Boolean,
    onMenuClick: () -> Unit,
    onBellClick: () -> Unit,
    onDateClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDateClick
                )
            ) {
                Text(
                    text = currentDate.month
                        .getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                        .replaceFirstChar { it.uppercase() },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = " ${currentDate.year}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFFAAAAAA),
                    letterSpacing = (-0.3).sp
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(start = 2.dp),
                    tint = Color(0xFFAAAAAA)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBellClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "Actividad",
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
                if (hasActivity) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Color(0xFFEF5350), CircleShape)
                            .align(Alignment.TopEnd)
                            .offset(x = (-8).dp, y = 8.dp)
                    )
                }
            }

            Spacer(Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color(0xFF1A1A1A), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "JT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(Modifier.width(12.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
private fun WeekStrip(
    selectedDate: LocalDate,
    onDateSelect: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val monday = selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() - 1)
    val dayLetters = listOf("L", "M", "M", "J", "V", "S", "D")

    Surface(color = Color.White, shadowElevation = 0.dp) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                dayLetters.forEachIndexed { i, letter ->
                    val date = monday.plusDays(i.toLong())
                    val isSelected = date == selectedDate
                    val isToday = date == today

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onDateSelect(date) }
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = letter,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isToday && !isSelected) Color(0xFF1A1A1A) else Color(0xFFBBBBBB),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    color = if (isSelected) Color(0xFF1A1A1A) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontSize = 13.sp,
                                fontWeight = when {
                                    isSelected -> FontWeight.SemiBold
                                    isToday -> FontWeight.Bold
                                    else -> FontWeight.Normal
                                },
                                color = when {
                                    isSelected -> Color.White
                                    isToday -> Color(0xFF1A1A1A)
                                    else -> Color(0xFF666666)
                                },
                                letterSpacing = (-0.2).sp
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
        }
    }
}

@Composable
private fun MinimalBottomBar() {
    Surface(
        color = Color.White,
        shadowElevation = 0.dp,
        modifier = Modifier.height(68.dp)
    ) {
        Column {
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    label = "Calendario",
                    isSelected = true,
                    content = {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(Color(0xFF1A1A1A), RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                LocalDate.now().dayOfMonth.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                )
                BottomBarItem(
                    label = "Servicios",
                    isSelected = false,
                    content = {
                        Icon(
                            Icons.Outlined.GridView,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFFAAAAAA)
                        )
                    }
                )
                BottomBarItem(
                    label = "Clientes",
                    isSelected = false,
                    content = {
                        Icon(
                            Icons.Outlined.SentimentSatisfied,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFFAAAAAA)
                        )
                    }
                )
                BottomBarItem(
                    label = "Config.",
                    isSelected = false,
                    content = {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFFAAAAAA)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    label: String,
    isSelected: Boolean,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        content()
        Spacer(Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF1A1A1A) else Color(0xFFAAAAAA),
            letterSpacing = 0.sp
        )
    }
}

@Composable
private fun PromoPill(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(21.dp),
        color = Color(0xFFEEF8F0),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(
                Icons.Default.FlightTakeoff,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF2E7D32)
            )
            Text(
                text = "Comenzar prueba gratuita",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2E7D32),
                letterSpacing = (-0.2).sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp)
        ) {
            SheetOption(Icons.Outlined.ContentCut, "Servicio")
            SheetOption(Icons.Outlined.School, "Clase")
            SheetOption(Icons.Outlined.Event, "Evento")
            SheetOption(Icons.Outlined.VideoCall, "Reunión única")

            HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(Icons.Outlined.CreditCard, null, Modifier.size(20.dp), Color(0xFF444444))
                    Text("Pago rápido", fontSize = 15.sp, color = Color.Black)
                }
                Surface(
                    onClick = {},
                    shape = RoundedCornerShape(20.dp),
                    border = ButtonDefaults.outlinedButtonBorder,
                    color = Color.Transparent
                ) {
                    Text(
                        "Conectar",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }

            SheetOption(Icons.Outlined.Groups, "Miembro del equipo")
            SheetOption(Icons.Outlined.PersonAddAlt, "Cliente")

            HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

            SheetOption(Icons.Outlined.QrCode2, "Compartir página de reservas")
        }
    }
}

@Composable
private fun SheetOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
            .padding(horizontal = 24.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, null, Modifier.size(20.dp), Color(0xFF444444))
        Text(title, fontSize = 15.sp, color = Color.Black, fontWeight = FontWeight.Normal)
    }
}
