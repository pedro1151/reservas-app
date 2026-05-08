package com.optic.pramozventicoappz.presentation.screens.clients.abmcliente

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.clients.ClientCreateRequest
import com.optic.pramozventicoappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.screens.clients.ClientViewModel

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Pink700  = Color(0xFFC2185B)
private val Pink600  = Color(0xFFE91E63)
private val Slate900 = Color(0xFF0F172A)
private val Slate700 = Color(0xFF334155)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val PageBg   = Color(0xFFF8F4F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABMClienteScreen(
    navController : NavHostController,
    clientId      : Int?,
    editable      : Boolean,
) {
    val viewModel      : ClientViewModel = hiltViewModel()
    val formState      by viewModel.formState.collectAsState()
    val createState    by viewModel.createClientState.collectAsState()
    val updateState    by viewModel.updateClientState.collectAsState()
    val oneClientState by viewModel.oneClientState.collectAsState()

    val isSaving = createState is Resource.Loading || updateState is Resource.Loading

    LaunchedEffect(clientId) {
        if (editable && clientId != null) viewModel.getClientById(clientId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Outlined.Close, null,
                            tint     = Slate700,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text          = if (editable) "Editar cliente" else "Nuevo cliente",
                        fontSize      = 16.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = Slate900,
                        letterSpacing = (-0.3).sp
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .shadow(
                                elevation    = if (isSaving) 0.dp else 6.dp,
                                shape        = RoundedCornerShape(12.dp),
                                ambientColor = Pink600.copy(alpha = 0.20f),
                                spotColor    = Pink700.copy(alpha = 0.26f)
                            )
                    ) {
                        Button(
                            onClick = {
                                val state = viewModel.formState.value
                                if (editable && clientId != null) {
                                    viewModel.updateClient(
                                        clientId,
                                        ClientUpdateRequest(
                                            fullName       = state.fullName,
                                            email          = state.email,
                                            phone          = state.phone,
                                            enterpriseName = state.enterprise,
                                            country        = state.country,
                                            address        = state.address,
                                            city           = state.city,
                                            state          = state.state
                                        )
                                    )
                                } else {
                                    viewModel.createClient(
                                        ClientCreateRequest(
                                            fullName       = state.fullName,
                                            email          = state.email,
                                            phone          = state.phone,
                                            enterpriseName = state.enterprise,
                                            country        = state.country,
                                            address        = state.address,
                                            city           = state.city,
                                            state          = state.state,
                                            businessId     = 1
                                        )
                                    )
                                }
                            },
                            enabled        = !isSaving && formState.fullName.isNotBlank(),
                            shape          = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
                            colors         = ButtonDefaults.buttonColors(
                                containerColor         = Pink600,
                                contentColor           = Color.White,
                                disabledContainerColor = Slate200,
                                disabledContentColor   = Slate400
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            AnimatedVisibility(visible = isSaving, enter = fadeIn(), exit = fadeOut()) {
                                CircularProgressIndicator(
                                    color       = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier    = Modifier.size(14.dp)
                                )
                            }
                            AnimatedVisibility(visible = !isSaving, enter = fadeIn(), exit = fadeOut()) {
                                Text(
                                    text          = if (editable) "Guardar" else "Crear",
                                    fontSize      = 14.sp,
                                    fontWeight    = FontWeight.SemiBold,
                                    letterSpacing = 0.1.sp
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = PageBg
    ) { paddingValues ->
        ABMClienteContent(
            paddingValues = paddingValues,
            navController = navController,
            clientId      = clientId,
            editable      = editable,
            viewModel     = viewModel
        )
    }

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {
            viewModel.resetCreateState()
            viewModel.resetForm()
            navController.popBackStack()
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is Resource.Success) {
            viewModel.resetUpdateState()
            viewModel.resetForm()
            navController.popBackStack()
        }
    }
}
