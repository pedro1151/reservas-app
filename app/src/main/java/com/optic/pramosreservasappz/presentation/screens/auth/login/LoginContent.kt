package com.optic.pramosreservasappz.presentation.screens.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.R
import com.optic.pramosreservasappz.core.Config
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.DefaultButton
import com.optic.pramosreservasappz.presentation.components.GoogleSignInButton
import com.optic.pramosreservasappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosreservasappz.presentation.navigation.Graph
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.salestats.colors.Cyan

@Composable
fun LoginContent(
    navController: NavHostController,
    paddingValues: PaddingValues,
    vm: LoginViewModel,
    onGoogleSignInClick: () -> Unit
) {

    val state = vm.state
    val context = LocalContext.current
    val sendCodeSuccess by vm.sendCodeSuccess
    val sendCodeState by vm.sendCodeState.collectAsState()

    val Graphite = Color(0xFF111827)
    val GraphiteSoft = Color(0xFF475569)

    // google sign loading
    val loginState = vm.loginResponse
    val isGoogleLoading = loginState is Resource.Loading


    LaunchedEffect(sendCodeSuccess) {
        if (sendCodeSuccess) {
            navController.navigate("${Graph.CLIENT}/${state.email}") {
                popUpTo(ClientScreen.Login.route) { inclusive = false }
            }
        }
    }

    LaunchedEffect(vm.errorMessage) {
        if (vm.errorMessage.isNotEmpty()) {
            Toast.makeText(context, vm.errorMessage, Toast.LENGTH_LONG).show()
            vm.errorMessage = ""
        }
    }

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        // 🔥 BACKGROUND IMAGE
        Image(
            painter = painterResource(id = R.drawable.fondo_ventas),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🔥 MODERN OVERLAY
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.22f),
                            Color.White.copy(alpha = 0.76f),
                            Color.White.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔥 CARD PRINCIPAL TODO EN UNO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f) // transparencia moderna
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 16.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 26.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // 🔥 LOGO DENTRO DE CARD
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = Graphite,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) { append("Sales") }

                            withStyle(
                                SpanStyle(
                                    color = Cyan,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) { append("Gow") }
                        },
                        fontSize = 38.sp,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ventas rápidas y control inteligente",
                        color = GraphiteSoft,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.55f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    /*
                    Text(
                        text = "Accede",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Graphite
                    )

                     */

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Aceede comienza a vender!",
                        fontSize = 15.sp,
                        color = GraphiteSoft
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    GoogleSignInButton(
                        onClick = { onGoogleSignInClick() },
                        enabled = !isGoogleLoading
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        text = "Accede con tu email",
                        onClick = {
                            navController.navigate(
                                ClientScreen.BasicLogin.route
                            )
                        },
                        color = Cyan,
                        icon = Icons.Default.Email,
                        textColor = Color.White.copy(alpha = 0.92f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E7EB)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = Config.APP_NAME,
                        color = Graphite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "2026",
                        color = GraphiteSoft,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

    //  PROGRESS BAR MOSTRADO SOLO CUANDO SE ENVÍA EL CÓDIGO
    CustomProgressBar(
        isLoading = isGoogleLoading
    )
}