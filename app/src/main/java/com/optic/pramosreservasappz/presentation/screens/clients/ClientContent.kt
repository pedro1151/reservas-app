package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.clients.components.ClientCard
import com.optic.pramosreservasappz.presentation.screens.clients.components.getAvatarColor
import com.optic.pramosreservasappz.presentation.screens.clients.components.getInitials
import kotlinx.coroutines.launch

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue700  = Color(0xFF1D4ED8)
private val Blue600  = Color(0xFF2563EB)
private val Blue500  = Color(0xFF3B82F6)
private val Blue400  = Color(0xFF60A5FA)
private val Blue100  = Color(0xFFDBEAFE)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate600 = Color(0xFF475569)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate100 = Color(0xFFF1F5F9)
private val Red500   = Color(0xFFEF4444)
private val PageBg   = Color(0xFFF8FAFC)

enum class ClientViewType { LIST, GRID }

// ─── Main Content ────────────────────────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientContent(
    modifier        : Modifier = Modifier,
    clients         : List<ClientResponse>,
    paddingValues   : PaddingValues,
    viewModel       : ClientViewModel,
    navController   : NavHostController,
    isAuthenticated : Boolean = false
) {
    val query        by viewModel.searchQuery.collectAsState()
    val deleteState  by viewModel.deleteClientState
    val localClients by viewModel.localClientsList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var isDeleting        by remember { mutableStateOf(false) }
    var viewType          by remember { mutableStateOf(ClientViewType.LIST) }

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Cliente eliminado ✓",
                        duration = SnackbarDuration.Short
                    )
                }
                isDeleting = false
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Error: ${state.message}",
                        duration = SnackbarDuration.Long
                    )
                }
                isDeleting = false
            }
            is Resource.Loading -> isDeleting = true
            else                -> isDeleting = false
        }
    }

    val hasQuery         = query.isNotBlank()
    val filteredClients  = remember(query, localClients) {
        if (query.isBlank()) localClients
        else localClients.filter { client ->
            client.fullName.contains(query, ignoreCase = true) ||
                    client.email?.contains(query, ignoreCase = true) == true ||
                    client.phone?.contains(query, ignoreCase = true) == true ||
                    client.city?.contains(query, ignoreCase = true) == true ||
                    client.country?.contains(query, ignoreCase = true) == true
        }
    }

    val withPhone = remember(localClients) { localClients.count { !it.phone.isNullOrBlank() } }
    val withEmail = remember(localClients) { localClients.count { !it.email.isNullOrBlank() } }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(PageBg)
    ) {
        if (localClients.isEmpty() && !hasQuery) {
            EmptyClientsState(
                onAddClient = {
                    navController.navigate(
                        ClientScreen.ABMCliente.createRoute(clientId = null, editable = false)
                    )
                }
            )
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {

                // ── Hero Stats ──
                item {
                    AnimatedVisibility(
                        visible = localClients.isNotEmpty(),
                        enter   = fadeIn(tween(400)) + expandVertically(tween(400))
                    ) {
                        ClientHeroStats(
                            totalClients = localClients.size,
                            withPhone    = withPhone,
                            withEmail    = withEmail
                        )
                    }
                }

                // ── Search + View Toggle ──
                item {
                    ClientSearchRow(
                        query            = query,
                        onQueryChange    = { viewModel.onSearchQueryChanged(it) },
                        hasQuery         = hasQuery,
                        totalCount       = localClients.size,
                        filteredCount    = filteredClients.size,
                        viewType         = viewType,
                        onViewTypeChange = { viewType = it }
                    )
                    Spacer(Modifier.height(10.dp))
                }

                // ── Empty search ──
                if (hasQuery && filteredClients.isEmpty()) {
                    item { ClientSearchEmptyState(query = query) }
                } else {
                    when (viewType) {

                        // ─ List mode ─
                        ClientViewType.LIST -> {
                            items(items = filteredClients, key = { it.id }) { client ->
                                ClientCard(
                                    client        = client,
                                    navController = navController,
                                    onDelete      = { viewModel.deleteClient(client.id) },
                                    modifier      = Modifier
                                        .animateItemPlacement(
                                            spring(
                                                Spring.DampingRatioMediumBouncy,
                                                Spring.StiffnessLow
                                            )
                                        )
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }

                        // ─ Grid mode ─
                        ClientViewType.GRID -> {
                            val chunked = filteredClients.chunked(2)
                            items(items = chunked, key = { it.first().id }) { pair ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .animateItemPlacement(
                                            spring(
                                                Spring.DampingRatioMediumBouncy,
                                                Spring.StiffnessLow
                                            )
                                        ),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    pair.forEach { client ->
                                        ClientGridCard(
                                            client        = client,
                                            modifier      = Modifier.weight(1f),
                                            navController = navController,
                                            onDelete      = { viewModel.deleteClient(client.id) }
                                        )
                                    }
                                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                                }
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }

        // ── Snackbar ──
        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData   = data,
                containerColor = Slate900,
                contentColor   = Color.White,
                shape          = RoundedCornerShape(14.dp)
            )
        }

        // ── Loading overlay ──
        AnimatedVisibility(
            visible  = isDeleting,
            enter    = fadeIn(),
            exit     = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape           = RoundedCornerShape(18.dp),
                color           = Color.White,
                shadowElevation = 10.dp
            ) {
                Box(Modifier.padding(22.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color       = Blue600,
                        strokeWidth = 2.5.dp,
                        modifier    = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

// ─── Hero Stats ─────────────────────────────────────────────────────────────────
@Composable
private fun ClientHeroStats(
    totalClients : Int,
    withPhone    : Int,
    withEmail    : Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .shadow(
                elevation    = 2.dp,
                shape        = RoundedCornerShape(18.dp),
                ambientColor = Blue600.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Slate200, RoundedCornerShape(18.dp))
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // ── Total clients ──
            ClientStatItem(
                icon       = Icons.Outlined.Group,
                value      = "$totalClients",
                label      = "Clientes",
                iconBg     = Blue50,
                iconTint   = Blue600,
                valueColor = Slate900
            )

            // ── Divider ──
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Slate200)
            )

            // ── With email ──
            ClientStatItem(
                icon       = Icons.Outlined.Mail,
                value      = "$withEmail",
                label      = "Con email",
                iconBg     = Color(0xFFF0FDF4),
                iconTint   = Color(0xFF059669),
                valueColor = Slate900
            )

            // ── Divider ──
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Slate200)
            )

            // ── With phone ──
            ClientStatItem(
                icon       = Icons.Outlined.Phone,
                value      = "$withPhone",
                label      = "Con tel.",
                iconBg     = Color(0xFFFFF7ED),
                iconTint   = Color(0xFFEA580C),
                valueColor = Slate900
            )
        }
    }
}

@Composable
private fun ClientStatItem(
    icon       : androidx.compose.ui.graphics.vector.ImageVector,
    value      : String,
    label      : String,
    iconBg     : Color,
    iconTint   : Color,
    valueColor : Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier            = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, null,
                tint     = iconTint,
                modifier = Modifier.size(15.dp)
            )
        }
        Text(
            text          = value,
            fontSize      = 18.sp,
            fontWeight    = FontWeight.Black,
            color         = valueColor,
            letterSpacing = (-0.5).sp,
            lineHeight    = 20.sp
        )
        Text(
            text          = label,
            fontSize      = 10.sp,
            color         = Slate400,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 0.1.sp
        )
    }
}

// ─── Search Row + View Toggle ────────────────────────────────────────────────────
@Composable
private fun ClientSearchRow(
    query            : String,
    onQueryChange    : (String) -> Unit,
    hasQuery         : Boolean,
    totalCount       : Int,
    filteredCount    : Int,
    viewType         : ClientViewType,
    onViewTypeChange : (ClientViewType) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isFocused    by remember { mutableStateOf(false) }

    val badgeText = if (hasQuery) "$filteredCount / $totalCount" else "$totalCount"

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── Search field ──
        Row(
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation    = if (isFocused) 4.dp else 1.dp,
                    shape        = RoundedCornerShape(16.dp),
                    ambientColor = Blue500.copy(alpha = if (isFocused) 0.10f else 0.03f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    width = if (isFocused) 1.5.dp else 1.dp,
                    color = if (isFocused) Blue500.copy(alpha = 0.50f) else Slate200,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Icon(
                Icons.Outlined.Search, null,
                tint     = if (isFocused) Blue600 else Slate400,
                modifier = Modifier.size(17.dp)
            )

            BasicTextField(
                value           = query,
                onValueChange   = onQueryChange,
                modifier        = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle       = TextStyle(
                    fontSize   = 14.sp,
                    color      = Slate900,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush     = SolidColor(Blue600),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                decorationBox   = { inner ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                "Buscar clientes...",
                                fontSize = 14.sp,
                                color    = Slate400
                            )
                        }
                        inner()
                    }
                }
            )

            // Count badge
            AnimatedContent(
                targetState    = badgeText,
                transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(130)) },
                label          = "badge_count"
            ) { label ->
                Box(
                    modifier = Modifier
                        .background(
                            if (hasQuery) Blue50 else Slate100,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        label,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = if (hasQuery) Blue600 else Slate400,
                        letterSpacing = 0.2.sp
                    )
                }
            }

            // Clear button
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter   = scaleIn(tween(150)) + fadeIn(tween(150)),
                exit    = scaleOut(tween(100)) + fadeOut(tween(100))
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Slate200)
                        .clickable(remember { MutableInteractionSource() }, null) {
                            onQueryChange("")
                            focusManager.clearFocus()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Close, "Limpiar",
                        tint     = Slate600,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }

        // ── View toggle ──
        Row(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .border(1.dp, Slate200, RoundedCornerShape(14.dp))
        ) {
            ClientViewToggleButton(
                icon       = Icons.Outlined.ViewList,
                isSelected = viewType == ClientViewType.LIST,
                onClick    = { onViewTypeChange(ClientViewType.LIST) },
                isStart    = true
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Slate200)
                    .align(Alignment.CenterVertically)
            )
            ClientViewToggleButton(
                icon       = Icons.Outlined.GridView,
                isSelected = viewType == ClientViewType.GRID,
                onClick    = { onViewTypeChange(ClientViewType.GRID) },
                isStart    = false
            )
        }
    }
}

@Composable
private fun ClientViewToggleButton(
    icon       : androidx.compose.ui.graphics.vector.ImageVector,
    isSelected : Boolean,
    onClick    : () -> Unit,
    isStart    : Boolean
) {
    val bgColor  by animateColorAsState(
        targetValue   = if (isSelected) Blue600 else Color.Transparent,
        animationSpec = tween(200),
        label         = "toggleBg"
    )
    val iconTint by animateColorAsState(
        targetValue   = if (isSelected) Color.White else Slate400,
        animationSpec = tween(200),
        label         = "toggleTint"
    )
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(
                RoundedCornerShape(
                    topStart    = if (isStart) 14.dp else 0.dp,
                    bottomStart = if (isStart) 14.dp else 0.dp,
                    topEnd      = if (!isStart) 14.dp else 0.dp,
                    bottomEnd   = if (!isStart) 14.dp else 0.dp
                )
            )
            .background(bgColor)
            .clickable(remember { MutableInteractionSource() }, null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = iconTint, modifier = Modifier.size(18.dp))
    }
}

// ─── Grid Card ──────────────────────────────────────────────────────────────────
@Composable
private fun ClientGridCard(
    client        : ClientResponse,
    modifier      : Modifier = Modifier,
    navController : NavHostController,
    onDelete      : (ClientResponse) -> Unit
) {
    val avatarColor = remember(client.id) { getAvatarColor(client.id) }
    val initials    = remember(client.fullName) { getInitials(client.fullName) }

    var showMenu by remember { mutableStateOf(false) }
    var visible  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible  = visible,
        enter    = fadeIn(tween(260)) + scaleIn(tween(260), initialScale = 0.92f),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = 3.dp,
                    shape        = RoundedCornerShape(20.dp),
                    ambientColor = Blue500.copy(alpha = 0.06f),
                    spotColor    = Blue600.copy(alpha = 0.09f)
                )
                .clip(RoundedCornerShape(20.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null
                ) {
                    navController.navigate(
                        ClientScreen.ClientDetail.createRoute(clientId = client.id)
                    )
                },
            shape           = RoundedCornerShape(20.dp),
            color           = Color.White,
            shadowElevation = 0.dp
        ) {
            Column {

                // ── Colored header ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    avatarColor.copy(alpha = 0.16f),
                                    avatarColor.copy(alpha = 0.04f)
                                )
                            )
                        )
                ) {
                    // MoreVert top-right
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.80f))
                                .clickable(remember { MutableInteractionSource() }, null) {
                                    showMenu = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.MoreVert, null,
                                tint     = Slate600,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        DropdownMenu(
                            expanded         = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier         = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Editar",
                                        fontSize   = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color      = Slate900
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Edit, null,
                                        tint     = Blue600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(
                                        ClientScreen.ABMCliente.createRoute(
                                            clientId = client.id,
                                            editable = true
                                        )
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Eliminar",
                                        fontSize   = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color      = Red500
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.DeleteOutline, null,
                                        tint     = Red500,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onDelete(client)
                                }
                            )
                        }
                    }

                    // Centered avatar
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(avatarColor, avatarColor.copy(alpha = 0.60f))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text          = initials,
                                fontSize      = 17.sp,
                                fontWeight    = FontWeight.Bold,
                                color         = Color.White,
                                letterSpacing = (-0.3).sp
                            )
                        }
                    }
                }

                // ── Content area ──
                Column(
                    modifier = Modifier.padding(
                        start  = 12.dp,
                        end    = 12.dp,
                        top    = 10.dp,
                        bottom = 12.dp
                    )
                ) {
                    Text(
                        text          = client.fullName,
                        fontSize      = 13.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = Slate900,
                        maxLines      = 2,
                        overflow      = TextOverflow.Ellipsis,
                        lineHeight    = 17.sp,
                        letterSpacing = (-0.1).sp
                    )
                    Spacer(Modifier.height(6.dp))

                    val subtitle = client.email ?: client.phone
                    if (subtitle != null) {
                        Text(
                            text          = subtitle,
                            fontSize      = 11.sp,
                            color         = Slate400,
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    // Location or contact pill
                    Box(
                        modifier = Modifier
                            .background(Blue50, RoundedCornerShape(7.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Person, null,
                                tint     = Blue600,
                                modifier = Modifier.size(8.dp)
                            )
                            Text(
                                "Cliente",
                                fontSize      = 9.sp,
                                color         = Blue600,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Empty Search State ──────────────────────────────────────────────────────────
@Composable
private fun ClientSearchEmptyState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Blue50),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.SearchOff, null, tint = Blue600, modifier = Modifier.size(34.dp))
        }
        Text(
            "Sin resultados",
            fontSize      = 17.sp,
            fontWeight    = FontWeight.Bold,
            color         = Slate900,
            letterSpacing = (-0.3).sp
        )
        Text(
            "No hay clientes que coincidan con \"$query\"",
            fontSize   = 13.sp,
            color      = Slate400,
            textAlign  = TextAlign.Center,
            lineHeight = 19.sp
        )
    }
}

// ─── Empty Clients State ─────────────────────────────────────────────────────────
@Composable
private fun EmptyClientsState(onAddClient: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier            = Modifier.padding(horizontal = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Blue50),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(Blue600, Blue500))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Group, null,
                        tint     = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Sin clientes aún",
                    fontSize      = 20.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = Slate900,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    "Registra tu primer cliente para comenzar a ver tu lista aquí.",
                    fontSize   = 14.sp,
                    color      = Slate400,
                    textAlign  = TextAlign.Center,
                    lineHeight = 21.sp
                )
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick  = onAddClient,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation    = 8.dp,
                        shape        = RoundedCornerShape(16.dp),
                        ambientColor = Blue600.copy(alpha = 0.20f),
                        spotColor    = Blue700.copy(alpha = 0.28f)
                    ),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Icon(Icons.Outlined.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Añadir cliente",
                    fontSize      = 15.sp,
                    fontWeight    = FontWeight.SemiBold,
                    letterSpacing = 0.1.sp
                )
            }
        }
    }
}
