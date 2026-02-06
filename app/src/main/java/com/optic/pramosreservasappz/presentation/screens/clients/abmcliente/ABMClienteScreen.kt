package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel

@Composable
fun ABMClienteScreen(
    navController: NavHostController,
    clientId: Int?,
    editable: Boolean,
    vm: ClientViewModel = hiltViewModel()
) {
    ABMClienteContent(
        navController = navController,
        clientId = clientId,
        editable = editable,
        vm = vm
    )
}
