package com.optic.pramosreservasappz.presentation.screens.calendar

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.staff.StaffResponse
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ClientCard
import com.optic.pramosreservasappz.presentation.screens.calendar.components.StaffCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    staff: List<StaffResponse>,
    clients: List<ClientResponse>,
    paddingValues: PaddingValues,
    viewModel: CalendarViewModel,
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    localizedContext: Context
) {

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

                // 🔹 Lista de clients
                items(
                    items = clients,
                    key = { it.id }
                ) { client ->
                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        ClientCard(
                            client = client,
                            onClick = {
                                // Puedes navegar a detalle si quieres
                                // navController.navigate("staff/${staffMember.id}")
                            }
                        )
                    }
                }
            }
        }

}
