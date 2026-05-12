package com.optic.pramozventicoappz.presentation.screens.business.members

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
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.components.BackTopBar
import com.optic.pramozventicoappz.presentation.screens.business.BusinessViewModel
import com.optic.pramozventicoappz.presentation.settings.idiomas.LocalizedContext

// ─── Design Tokens ────────────────────────────────────────────────────────────
private val Magenta600 = Color(0xFFD81B60)
private val PageBg     = Color(0xFFF8F9FA)
private val Slate400   = Color(0xFF94A3B8)
private val Slate900   = Color(0xFF0F172A)

@Composable
fun BusinessMembersScreen(
    navController: NavHostController
) {
    val viewModel      : BusinessViewModel = hiltViewModel()
    val clientResource by viewModel.membersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMembers(
            businessId = 1,
            email      = "",
            username   = ""
        )
    }

    val localizedContext = LocalizedContext.current

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title         = "Colaboradores"
            )
        },
        containerColor = PageBg
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = clientResource) {

                is Resource.Loading -> {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color       = Magenta600,
                            strokeWidth = 2.5.dp,
                            modifier    = Modifier.size(30.dp)
                        )
                    }
                }

                is Resource.Success -> {
                    BusinessMembersContent(
                        members       = result.data,
                        paddingValues = paddingValues,
                        navController = navController,
                        viewModel     = viewModel
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
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text     = "No se pudo cargar los colaboradores",
                                color    = Slate400,
                                fontSize = 15.sp
                            )
                            Button(
                                onClick = { viewModel.loadMembers(1, "", "") },
                                colors  = ButtonDefaults.buttonColors(
                                    containerColor = Magenta600
                                )
                            ) {
                                Text("Reintentar", color = Color.White)
                            }
                        }
                    }
                }

                else -> {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color       = Magenta600,
                            strokeWidth = 2.5.dp,
                            modifier    = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}