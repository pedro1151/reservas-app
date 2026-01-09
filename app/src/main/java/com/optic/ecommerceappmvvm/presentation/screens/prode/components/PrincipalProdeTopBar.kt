package com.optic.ecommerceappmvvm.presentation.screens.prode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.optic.ecommerceappmvvm.presentation.authstate.AuthStateVM
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalProdeTopBar(
    title: String = "",
    navController: NavController,
    showTitle: Boolean = true,
    vm: AuthStateVM = hiltViewModel(),
    prodeViewModel: ProdeViewModel
) {
    val isAuthenticated by vm.isAuthenticated.collectAsState()
    val userEmail by vm.userEmail.collectAsState()
    val query by prodeViewModel.searchQuery.collectAsState()

    TopAppBar(
        title = {
            if (showTitle) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            }
        },
        actions = {
            // 🔍 SearchBar a la derecha
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth(0.55f), // 👈 controla cuánto ocupa
                contentAlignment = Alignment.CenterEnd
            ) {
                LeagueSearchBar(
                    query = query,
                    onQueryChange = {
                        prodeViewModel.onSearchQueryChanged(it)
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    )
}
