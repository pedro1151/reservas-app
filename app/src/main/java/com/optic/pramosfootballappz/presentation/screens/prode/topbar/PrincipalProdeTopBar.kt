package com.optic.pramosfootballappz.presentation.screens.prode.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignVerticalBottom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.optic.pramosfootballappz.presentation.authstate.AuthStateVM
import com.optic.pramosfootballappz.presentation.components.DefaultButtonLink
import com.optic.pramosfootballappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosfootballappz.presentation.screens.prode.ProdeViewModel
import com.optic.pramosfootballappz.presentation.screens.prode.components.LeagueSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalProdeTopBar(
    title: String = "",
    navController: NavController,
    showTitle: Boolean = true,
    vm: AuthStateVM = hiltViewModel(),
    prodeViewModel: ProdeViewModel
) {
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

            // 🔥 CONTENEDOR ÚNICO
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(end = 8.dp)
            ) {

                // 🏆 Rank
                DefaultButtonLink(
                    text = "Rank",
                    onClick = {
                        navController.navigate(
                            ClientScreen.ProdeRanking.route
                        ) {
                            popUpTo(0)
                            // elimina TODA la pila
                            launchSingleTop = true
                        }
                    },
                    icon = Icons.Default.AlignVerticalBottom,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 🔍 Search (se expande a la izquierda)
                LeagueSearchBar(
                    query = query,
                    onQueryChange = {
                        prodeViewModel.onSearchQueryChanged(it)
                    }
                )



            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
