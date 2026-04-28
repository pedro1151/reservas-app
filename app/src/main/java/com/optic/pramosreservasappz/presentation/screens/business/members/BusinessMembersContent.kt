package com.optic.pramosreservasappz.presentation.screens.business.members

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
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.screens.business.members.components.BusinessMemberCard
import com.optic.pramosreservasappz.presentation.util.getAvatarColor
import com.optic.pramosreservasappz.presentation.util.getInitials
import kotlinx.coroutines.launch
import com.optic.pramosreservasappz.presentation.screens.newsale.selecclient.components.SelectClientSearchBar

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue700  = Color(0xFF1D4ED8)
private val Blue600  = Color(0xFF2563EB)
private val Blue500  = Color(0xFF3B82F6)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate600 = Color(0xFF475569)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate100 = Color(0xFFF1F5F9)
private val Red500   = Color(0xFFEF4444)
private val PageBg   = Color(0xFFF8FAFC)

enum class MemberViewType { LIST, GRID }

// ─── Main Content ────────────────────────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BusinessMembersContent(
    modifier      : Modifier = Modifier,
    members       : List<UserMemberResponse>,
    paddingValues : PaddingValues,
    navController : NavHostController,
    viewModel     : BusinessViewModel
) {
    val query        by viewModel.searchQuery.collectAsState()
    val localMembers by viewModel.localMembersList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var viewType          by remember { mutableStateOf(MemberViewType.LIST) }

    val hasQuery = query.isNotBlank()
    val filteredMembers = remember(query, localMembers) {
        if (query.isBlank()) localMembers
        else localMembers.filter { m ->
            m.user.email.contains(query, ignoreCase = true) ||
                    m.user.username.contains(query, ignoreCase = true)
        }
    }

    val activeCount = remember(localMembers) {
        localMembers.count { it.businessMember.statusLabel.contains("activ", ignoreCase = true) }
    }
    val roleCount = remember(localMembers) {
        localMembers.count { it.businessMember.roleLabel.contains("admin", ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        if (localMembers.isEmpty() && !hasQuery) {
            EmptyMembersState()
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {

                // ── Hero Stats ──
                item {
                    AnimatedVisibility(
                        visible = localMembers.isNotEmpty(),
                        enter   = fadeIn(tween(400)) + expandVertically(tween(400))
                    ) {
                        MemberHeroStats(
                            total  = localMembers.size,
                            active = activeCount,
                            admins = roleCount
                        )
                    }
                }

                // ── Search + View Toggle ──
                item {
                    MemberSearchRow(
                        query            = query,
                        onQueryChange    = { viewModel.onSearchQueryChanged(it) },
                        hasQuery         = hasQuery,
                        totalCount       = localMembers.size,
                        filteredCount    = filteredMembers.size,
                        viewType         = viewType,
                        onViewTypeChange = { viewType = it }
                    )
                    Spacer(Modifier.height(10.dp))
                }

                // ── Empty search ──
                if (hasQuery && filteredMembers.isEmpty()) {
                    item { MemberSearchEmptyState(query = query) }
                } else {
                    when (viewType) {

                        MemberViewType.LIST -> {
                            items(items = filteredMembers, key = { it.user.id }) { member ->
                                BusinessMemberCard(
                                    member        = member,
                                    navController = navController,
                                    viewModel     = viewModel,
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

                        MemberViewType.GRID -> {
                            val chunked = filteredMembers.chunked(2)
                            items(items = chunked, key = { it.first().user.id }) { pair ->
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
                                    pair.forEach { member ->
                                        MemberGridCard(
                                            member        = member,
                                            modifier      = Modifier.weight(1f),
                                            navController = navController,
                                            viewModel     = viewModel
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
    }
}

// ─── Hero Stats strip ────────────────────────────────────────────────────────────
@Composable
private fun MemberHeroStats(
    total  : Int,
    active : Int,
    admins : Int
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
            MemberStatItem(
                icon       = Icons.Outlined.Group,
                value      = "$total",
                label      = "Total",
                iconBg     = Blue50,
                iconTint   = Blue600
            )
            Box(Modifier.width(1.dp).height(32.dp).background(Slate200))
            MemberStatItem(
                icon       = Icons.Outlined.CheckCircle,
                value      = "$active",
                label      = "Activos",
                iconBg     = Color(0xFFF0FDF4),
                iconTint   = Color(0xFF059669)
            )
            Box(Modifier.width(1.dp).height(32.dp).background(Slate200))
            MemberStatItem(
                icon       = Icons.Outlined.AdminPanelSettings,
                value      = "$admins",
                label      = "Admins",
                iconBg     = Color(0xFFFAF5FF),
                iconTint   = Color(0xFF7C3AED)
            )
        }
    }
}

@Composable
private fun MemberStatItem(
    icon     : androidx.compose.ui.graphics.vector.ImageVector,
    value    : String,
    label    : String,
    iconBg   : Color,
    iconTint : Color
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
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(15.dp))
        }
        Text(
            text          = value,
            fontSize      = 18.sp,
            fontWeight    = FontWeight.Black,
            color         = Slate900,
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
private fun MemberSearchRow(
    query            : String,
    onQueryChange    : (String) -> Unit,
    hasQuery         : Boolean,
    totalCount       : Int,
    filteredCount    : Int,
    viewType         : MemberViewType,
    onViewTypeChange : (MemberViewType) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isFocused    by remember { mutableStateOf(false) }
    val badgeText    = if (hasQuery) "$filteredCount / $totalCount" else "$totalCount"

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
                            Text("Buscar colaboradores...", fontSize = 14.sp, color = Slate400)
                        }
                        inner()
                    }
                }
            )
            AnimatedContent(
                targetState    = badgeText,
                transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(130)) },
                label          = "badge"
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
                    Icon(Icons.Outlined.Close, "Limpiar", tint = Slate600, modifier = Modifier.size(11.dp))
                }
            }
        }

        // View toggle
        Row(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .border(1.dp, Slate200, RoundedCornerShape(14.dp))
        ) {
            MemberViewToggle(
                icon       = Icons.Outlined.ViewList,
                isSelected = viewType == MemberViewType.LIST,
                onClick    = { onViewTypeChange(MemberViewType.LIST) },
                isStart    = true
            )
            Box(Modifier.width(1.dp).height(32.dp).background(Slate200).align(Alignment.CenterVertically))
            MemberViewToggle(
                icon       = Icons.Outlined.GridView,
                isSelected = viewType == MemberViewType.GRID,
                onClick    = { onViewTypeChange(MemberViewType.GRID) },
                isStart    = false
            )
        }
    }
}

@Composable
private fun MemberViewToggle(
    icon       : androidx.compose.ui.graphics.vector.ImageVector,
    isSelected : Boolean,
    onClick    : () -> Unit,
    isStart    : Boolean
) {
    val bgColor  by animateColorAsState(
        if (isSelected) Blue600 else Color.Transparent, tween(200), label = "bg"
    )
    val iconTint by animateColorAsState(
        if (isSelected) Color.White else Slate400, tween(200), label = "tint"
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

// ─── Grid Card ───────────────────────────────────────────────────────────────────
@Composable
private fun MemberGridCard(
    member        : UserMemberResponse,
    modifier      : Modifier = Modifier,
    navController : NavHostController,
    viewModel     : BusinessViewModel
) {
    val avatarColor = remember(member.user.id) { getAvatarColor(member.user.id) }
    val initials    = remember(member.user.email) { getInitials(member.user.email) }

    var showMenu by remember { mutableStateOf(false) }
    var visible  by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val isActive = member.businessMember.statusLabel.contains("activ", ignoreCase = true)
    val statusBg   = if (isActive) Color(0xFFF0FDF4) else Color(0xFFFFF7ED)
    val statusTint = if (isActive) Color(0xFF059669) else Color(0xFFEA580C)

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
                .clickable(remember { MutableInteractionSource() }, null) {
                    navController.navigate(
                        ClientScreen.UpdateBusinessMember.createRoute(userId = member.user.id)
                    )
                },
            shape           = RoundedCornerShape(20.dp),
            color           = Color.White,
            shadowElevation = 0.dp
        ) {
            Column {
                // Header
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
                    // MoreVert
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
                            Icon(Icons.Outlined.MoreVert, null, tint = Slate600, modifier = Modifier.size(14.dp))
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
                                    Text("Editar", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Slate900)
                                },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Edit, null, tint = Blue600, modifier = Modifier.size(16.dp))
                                },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(
                                        ClientScreen.UpdateBusinessMember.createRoute(userId = member.user.id)
                                    )
                                }
                            )
                        }
                    }

                    // Avatar centered
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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

                // Content
                Column(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp)
                ) {
                    Text(
                        text          = member.user.username,
                        fontSize      = 13.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = Slate900,
                        maxLines      = 1,
                        overflow      = TextOverflow.Ellipsis,
                        letterSpacing = (-0.1).sp
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text     = member.user.email,
                        fontSize = 11.sp,
                        color    = Slate400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Box(
                            modifier = Modifier
                                .background(Blue50, RoundedCornerShape(7.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                member.businessMember.roleLabel,
                                fontSize      = 9.sp,
                                color         = Blue600,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.3.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(statusBg, RoundedCornerShape(7.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                member.businessMember.statusLabel,
                                fontSize      = 9.sp,
                                color         = statusTint,
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
private fun MemberSearchEmptyState(query: String) {
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
            "No hay colaboradores que coincidan con \"$query\"",
            fontSize   = 13.sp,
            color      = Slate400,
            textAlign  = TextAlign.Center,
            lineHeight = 19.sp
        )
    }
}

// ─── Empty Members State ─────────────────────────────────────────────────────────
@Composable
private fun EmptyMembersState() {
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
                    Icon(Icons.Outlined.Group, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
            Text(
                "Sin colaboradores",
                fontSize      = 20.sp,
                fontWeight    = FontWeight.Bold,
                color         = Slate900,
                letterSpacing = (-0.5).sp
            )
            Text(
                "Aún no hay colaboradores registrados en este negocio.",
                fontSize   = 14.sp,
                color      = Slate400,
                textAlign  = TextAlign.Center,
                lineHeight = 21.sp
            )
        }
    }
}