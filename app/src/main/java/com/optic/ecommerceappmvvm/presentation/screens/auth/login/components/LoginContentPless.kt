package com.optic.ecommerceappmvvm.presentation.screens.auth.login.components

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.presentation.components.DefaultButton
import com.optic.ecommerceappmvvm.presentation.components.DefaultTextField
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.auth.login.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginContentPless(
    navController: NavHostController,
    vm: LoginViewModel = hiltViewModel(),
    email: String
) {
    val state = vm.state
    val context = LocalContext.current
    vm.onEmailInput(email)

    val navigateToHome by vm.navigateToHome.collectAsState()

    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            navController.navigate(Graph.CLIENT) {
                popUpTo(ClientScreen.LoginPless.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(vm.errorMessage) {
        if (vm.errorMessage.isNotEmpty()) {
            Toast.makeText(context, vm.errorMessage, Toast.LENGTH_LONG).show()
            vm.errorMessage = ""
        }
    }

    // 🔄 Control de contador y botón de reenviar
    var showResendButton by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(30) }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
        showResendButton = true
    }

    // ✨ Animación general de aparición
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800),
        label = "screen-fade"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(horizontal = 24.dp)
            .alpha(alpha)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔙 Botón de volver
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
                    .clickable {
                        navController.navigate(ClientScreen.Login.route) {
                            popUpTo(ClientScreen.LoginPless.route) { inclusive = true }
                        }

                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "ALLFOOT",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 34.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Verifica tu código de acceso",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(60.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ingresa el código enviado a tu correo",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.code,
                        onValueChange = { text -> vm.onCodeInput(text) },
                        label = "Código de verificación",
                        icon = Icons.Default.EmojiFoodBeverage,
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        text = "Verificar",
                        onClick = { vm.loginPless(email = email) },
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // ⏱️ Contador visible antes de mostrar el botón
                    AnimatedVisibility(
                        visible = !showResendButton,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Podrás reenviar el código en ${remainingTime}s",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    // ⏳ Botón de reenviar código animado
                    AnimatedVisibility(
                        visible = showResendButton,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
                    ) {
                        DefaultButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            text = "Reenviar código",
                            onClick = {
                                vm.loginSendCode()
                                remainingTime = 30
                                showResendButton = false
                            },
                        )
                    }
                }
            }
        }
    }
}
