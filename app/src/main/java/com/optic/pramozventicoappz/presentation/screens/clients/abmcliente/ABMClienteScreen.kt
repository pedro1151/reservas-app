package com.optic.pramozventicoappz.presentation.screens.clients.abmcliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

private val PageBg = Color(0xFFF9FAFB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABMClienteScreen(
    navController: NavHostController,
    clientId: Int?,
    editable: Boolean,
) {
    val viewModel: ClientViewModel = hiltViewModel()
    val formState      by viewModel.formState.collectAsState()
    val createState    by viewModel.createClientState.collectAsState()
    val updateState    by viewModel.updateClientState.collectAsState()
    val oneClientState by viewModel.oneClientState.collectAsState()

    val primary  = MaterialTheme.colorScheme.primary
    val isSaving = createState is Resource.Loading || updateState is Resource.Loading
    val isFormValid = formState.fullName.isNotBlank() && formState.email.isNotBlank()

    val errorMessage = when {
        createState is Resource.Failure -> (createState as Resource.Failure).message
        updateState is Resource.Failure -> (updateState as Resource.Failure).message
        else -> null
    }

    LaunchedEffect(clientId) {
        if (editable && clientId != null) viewModel.getClientById(clientId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Outlined.ArrowBack, null,
                            tint     = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text          = if (editable) "Editar cliente" else "Nuevo cliente",
                        fontSize      = 16.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = TextPrimary,
                        letterSpacing = (-0.3).sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            SaveBottomBar(
                isLoading = isSaving,
                isEnabled = isFormValid && !isSaving,
                isEdit    = editable,
                primary   = primary,
                onSave    = {
                    // ⚠️ MISMA lógica que tenías en el topbar — sin cambios
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
                }
            )
        },
        containerColor = PageBg
    ) { paddingValues ->
        ABMClienteContent(
            paddingValues = paddingValues,
            navController = navController,
            clientId      = clientId,
            editable      = editable,
            viewModel     = viewModel,
            errorMessage  = errorMessage
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

@Composable
private fun SaveBottomBar(
    isLoading: Boolean,
    isEnabled: Boolean,
    isEdit: Boolean,
    primary: Color,
    onSave: () -> Unit
) {
    Surface(
        color           = Color.White,
        shadowElevation = 14.dp,
        modifier        = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            val buttonBg = if (isEnabled) {
                Brush.horizontalGradient(listOf(primary, primary.copy(alpha = 0.88f)))
            } else {
                Brush.horizontalGradient(listOf(Color(0xFFCBD5E1), Color(0xFFCBD5E1)))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .then(
                        if (isEnabled) Modifier.shadow(
                            elevation    = 12.dp,
                            shape        = RoundedCornerShape(16.dp),
                            ambientColor = primary.copy(alpha = 0.30f),
                            spotColor    = primary.copy(alpha = 0.40f)
                        ) else Modifier
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(buttonBg)
                    .clickable(enabled = isEnabled, onClick = onSave),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color       = Color.White,
                        strokeWidth = 2.5.dp,
                        modifier    = Modifier.size(22.dp)
                    )
                } else {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isEdit) Icons.Outlined.Check else Icons.Outlined.Add,
                            contentDescription = null,
                            tint     = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text          = if (isEdit) "Guardar cambios" else "Crear cliente",
                            color         = Color.White,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }
        }
    }
}