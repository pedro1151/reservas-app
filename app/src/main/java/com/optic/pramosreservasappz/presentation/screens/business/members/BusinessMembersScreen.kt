package com.optic.pramosreservasappz.presentation.screens.business.members

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun BusinessMembersScreen(
    navController: NavHostController
){

    val viewModel        : BusinessViewModel= hiltViewModel()
    val clientResource by viewModel.membersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMembers(
            businessId = 1,
            email = "",
            username = ""
        )
    }

    val localizedContext = LocalizedContext.current

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Colaboradores"
            )
        },

        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) //
        ) {

            when (val result = clientResource) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                is Resource.Success -> {
                    BusinessMembersContent(
                        members = result.data,
                        paddingValues = paddingValues,
                        navController = navController,
                        viewModel = viewModel

                    )
                }

                is Resource.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "No se pudo cargar los clientes",
                                color = Color(0xFF9E9E9E),
                                fontSize = 15.sp
                            )
                            TextButton(onClick = { viewModel.loadMembers(1, "", "") }) {
                                Text("Reintentar", color = Color.Black)
                            }
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

