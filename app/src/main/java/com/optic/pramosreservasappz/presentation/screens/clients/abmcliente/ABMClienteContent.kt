package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.AlternativeTextField
import com.optic.pramosreservasappz.presentation.components.DefaultButton
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABMClienteContent(
    navController: NavHostController,
    clientId: Int?,
    editable: Boolean,
    vm: ClientViewModel
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // Validation states
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 🔹 Cargar datos si es edición
    LaunchedEffect(clientId, editable) {
        if (editable && clientId != null) {
            vm.getClientById(clientId)
        }
    }

    // 🔹 Observar el estado de carga del cliente
    val oneClientState by vm.oneClientState.collectAsState()
    LaunchedEffect(oneClientState) {
        when (val state = oneClientState) {
            is Resource.Success -> {
                fullName = state.data.fullName
                email = state.data.email ?: ""
                phone = state.data.phone ?: ""
                city = state.data.city ?: ""
                country = state.data.country ?: ""
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Error al cargar cliente: ${state.message}",
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> {}
        }
    }

    // 🔹 Observar estado de creación
    val createState by vm.createClientState.collectAsState()
    LaunchedEffect(createState) {
        when (val state = createState) {
            is Resource.Loading -> {
                isLoading = true
            }
            is Resource.Success -> {
                isLoading = false
                showSuccessDialog = true
                delay(1200)
                navController.popBackStack()
            }
            is Resource.Failure -> {
                isLoading = false
                errorMessage = state.message
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = state.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
            else -> {
                isLoading = false
            }
        }
    }

    // 🔹 Observar estado de actualización
    val updateState by vm.updateClientState.collectAsState()
    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is Resource.Loading -> {
                isLoading = true
            }
            is Resource.Success -> {
                isLoading = false
                showSuccessDialog = true
                delay(1200)
                navController.popBackStack()
            }
            is Resource.Failure -> {
                isLoading = false
                errorMessage = state.message
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = state.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
            else -> {
                isLoading = false
            }
        }
    }

    // Validation functions
    fun validateFullName(): Boolean {
        return when {
            fullName.isBlank() -> {
                fullNameError = "El nombre es requerido"
                false
            }
            fullName.length < 3 -> {
                fullNameError = "El nombre debe tener al menos 3 caracteres"
                false
            }
            else -> {
                fullNameError = null
                true
            }
        }
    }

    fun validateEmail(): Boolean {
        return when {
            email.isBlank() -> {
                emailError = "El email es requerido"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailError = "Email inválido"
                false
            }
            else -> {
                emailError = null
                true
            }
        }
    }

    fun validatePhone(): Boolean {
        return when {
            phone.isBlank() -> {
                phoneError = "El teléfono es requerido"
                false
            }
            phone.length < 7 -> {
                phoneError = "Teléfono debe tener al menos 7 dígitos"
                false
            }
            !phone.all { it.isDigit() || it == '+' || it == ' ' } -> {
                phoneError = "Teléfono inválido"
                false
            }
            else -> {
                phoneError = null
                true
            }
        }
    }

    fun validateAll(): Boolean {
        val isNameValid = validateFullName()
        val isEmailValid = validateEmail()
        val isPhoneValid = validatePhone()
        return isNameValid && isEmailValid && isPhoneValid
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (editable) "Editar Cliente" else "Nuevo Cliente",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 🎯 Header con ícono colorido - CORREGIDO
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 🎨 AVATAR COLORIDO en lugar de recuadro negro
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = if (editable) "Actualiza la información del cliente" else "Completa los datos del nuevo cliente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF616161)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // 📝 Formulario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        // Nombre completo
                        EnhancedTextField(
                            value = fullName,
                            onValueChange = {
                                fullName = it
                                if (fullNameError != null) validateFullName()
                            },
                            label = "Nombre completo",
                            placeholder = "Ej: Juan Pérez",
                            icon = Icons.Outlined.Person,
                            error = fullNameError,
                            onValidate = { validateFullName() }
                        )

                        // Email
                        EnhancedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (emailError != null) validateEmail()
                            },
                            label = "Correo electrónico",
                            placeholder = "correo@ejemplo.com",
                            icon = Icons.Outlined.Email,
                            keyboardType = KeyboardType.Email,
                            error = emailError,
                            onValidate = { validateEmail() }
                        )

                        // Teléfono
                        EnhancedTextField(
                            value = phone,
                            onValueChange = {
                                phone = it
                                if (phoneError != null) validatePhone()
                            },
                            label = "Teléfono",
                            placeholder = "70000000",
                            icon = Icons.Outlined.Phone,
                            keyboardType = KeyboardType.Phone,
                            error = phoneError,
                            onValidate = { validatePhone() }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        // Ciudad
                        EnhancedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = "Ciudad",
                            placeholder = "La Paz",
                            icon = Icons.Outlined.LocationOn,
                            isOptional = true
                        )

                        // País
                        EnhancedTextField(
                            value = country,
                            onValueChange = { country = it },
                            label = "País",
                            placeholder = "Bolivia",
                            icon = Icons.Outlined.Public,
                            isOptional = true
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // 🔘 Botón de acción
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (editable) "Actualizar Cliente" else "Crear Cliente",
                    enabled = !isLoading,
                    onClick = {
                        if (validateAll()) {
                            if (!editable) {
                                vm.createClient(
                                    ClientCreateRequest(
                                        providerId = 1,
                                        fullName = fullName.trim(),
                                        email = email.trim(),
                                        phone = phone.trim(),
                                        city = city.trim().ifBlank { null },
                                        country = country.trim().ifBlank { null }
                                    )
                                )
                            } else {
                                clientId?.let {
                                    vm.updateClient(
                                        clientId = it,
                                        request = ClientUpdateRequest(
                                            fullName = fullName.trim(),
                                            email = email.trim(),
                                            phone = phone.trim(),
                                            city = city.trim().ifBlank { null },
                                            country = country.trim().ifBlank { null }
                                        )
                                    )
                                }
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Por favor, corrige los errores en el formulario",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                )

                Spacer(Modifier.height(32.dp))
            }

            // 🎉 Success Dialog
            if (showSuccessDialog) {
                SuccessAnimation()
            }

            // ⏳ Loading overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = MaterialTheme.shapes.large,
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                strokeWidth = 3.dp
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Guardando...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF424242)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 🎨 Enhanced TextField Component
@Composable
private fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null,
    isOptional: Boolean = false,
    onValidate: (() -> Boolean)? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF212121)
                )
            }
            if (isOptional) {
                Text(
                    text = "Opcional",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9E9E9E)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        AlternativeTextField(
            value = value,
            onValueChange = onValueChange,
            label = "",
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = error ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// 🎉 Success Animation Component - CORREGIDO
@Composable
private fun SuccessAnimation() {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(300)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .size(140.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    this.alpha = alpha.value
                },
            shape = MaterialTheme.shapes.extraLarge,
            color = Color.White,  // 🔹 CORREGIDO: Fondo blanco
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),  // 🔹 Verde para éxito
                    modifier = Modifier.size(56.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "¡Éxito!",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4CAF50)  // 🔹 Verde para éxito
                )
            }
        }
    }
}
