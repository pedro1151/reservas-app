package com.optic.pramosreservasappz.presentation.screens.business.members


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.screens.business.members.components.BusinessMemberCard
import com.optic.pramosreservasappz.presentation.screens.salecomplete.selecclient.components.SelectClientSearchBar

import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground

@Composable
fun BusinessMembersContent(
    modifier: Modifier = Modifier,
    members: List<UserMemberResponse>,
    paddingValues: PaddingValues,   // ← mantenido para no romper el caller
    navController: NavHostController,
    viewModel: BusinessViewModel
) {
    val query        by viewModel.searchQuery.collectAsState()
    val localMembers by viewModel.localMembersList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var isDeleting        by remember { mutableStateOf(false) }

    // ── lógica sin cambios ─────────────────────────────────────────────────────

    val hasQuery = query.isNotBlank()
    val filteredMembers = remember(query, localMembers) {
        if (query.isBlank()) localMembers
        else localMembers.filter { c ->
                    c.user.email?.contains(query, ignoreCase = true) == true ||
                    c.user.username?.contains(query, ignoreCase = true) == true
        }
    }

    // FIX: NO aplicamos paddingValues aquí — el padre (Scaffold Column) ya lo aplicó
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SoftCoolBackground)
    ) {
        if (localMembers.isEmpty() && !hasQuery) {
            EmptyClientsState(
                onAddClient = {
                    navController.navigate(ClientScreen.ABMCliente.createRoute(clientId = null, editable = false))
                }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Search bar + contador ──────────────────────────────────────
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),   // vertical reducido
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SelectClientSearchBar(
                        query         = query,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        modifier      = Modifier.weight(1f)
                    )
                    if (!hasQuery && localMembers.isNotEmpty()) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text       = "${localMembers.size}",
                            fontSize   = 12.sp,
                            color      = Color(0xFFBBBBBB),
                            fontWeight = FontWeight.Medium,
                            modifier   = Modifier.padding(end = 4.dp)
                        )
                    }
                }

                // ── Lista ─────────────────────────────────────────────────────
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (hasQuery && filteredMembers.isEmpty()) {
                        item {
                            Box(
                                modifier         = Modifier.fillMaxWidth().padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Outlined.Search, null, tint = Color(0xFFDDDDDD), modifier = Modifier.size(36.dp))
                                    Text("Sin resultados para \"$query\"", fontSize = 13.sp, color = Color(0xFFBBBBBB))
                                }
                            }
                        }
                    } else {
                        items(
                            items = filteredMembers,
                            key   = { it.user.id }
                        ) { member ->
                            BusinessMemberCard(
                                member           = member,
                                navController     = navController,
                                modifier =  Modifier,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData   = data,
                containerColor = Color(0xFF1A1A1A),
                contentColor   = Color.White,
                shape          = RoundedCornerShape(12.dp)
            )
        }

        AnimatedVisibility(
            visible  = isDeleting,
            enter    = fadeIn(), exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun EmptyClientsState(onAddClient: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier            = Modifier.padding(horizontal = 40.dp)
        ) {
            Text("No hay clientes para mostrar.", fontSize = 17.sp, fontWeight = FontWeight.Normal, color = Color.Black, letterSpacing = (-0.2).sp)
            Spacer(Modifier.height(4.dp))
            Button(
                onClick  = onAddClient,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(25.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Añadir nuevo cliente", fontSize = 15.sp, color = Color.White)
            }
            OutlinedButton(
                onClick  = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(25.dp)
            ) {
                Text("Importar desde contactos", fontSize = 15.sp, color = Color.Black)
            }
        }
    }
}
