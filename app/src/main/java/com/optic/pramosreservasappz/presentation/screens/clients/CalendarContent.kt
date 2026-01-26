package com.optic.pramosreservasappz.presentation.screens.clients

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientContent(
    modifier: Modifier = Modifier,
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
            contentPadding = PaddingValues(vertical = 1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),

        ) {

        item {
            Text(
                text = "Completar pantalla"
            )
        }

        }
    }
}

