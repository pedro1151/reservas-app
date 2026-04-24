package com.optic.pramosreservasappz.presentation.screens.auth.login

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

// ─── Paleta refinada ──────────────────────────────────────────────────────────
private val Ink         = Color(0xFF0D0D0D)
private val InkSoft     = Color(0xFF6B7280)
private val InkXSoft    = Color(0xFFD1D5DB)
private val PureWhite   = Color(0xFFFFFFFF)
private val GlassWhite  = Color(0xF5FFFFFF)
private val AccentCyan  = Color(0xFF06B6D4)   // mismo Cyan del proyecto
private val AccentRing  = Color(0xFFE0F7FA)

@Composable
fun LoginContent(
    navController: NavHostController,
    paddingValues: PaddingValues,
    vm: LoginViewModel,
    onGoogleSignInClick: () -> Unit
) {
    val state            = vm.state
    val context          = LocalContext.current
    val sendCodeSuccess  by vm.sendCodeSuccess
    val sendCodeState    by vm.sendCodeState.collectAsState()
    val loginState       = vm.loginResponse
    val isGoogleLoading  = loginState is Resource.Loading

    // ── Animación de entrada sutil ──────────────────────────────────────────
    var visible by remember { mutableStateOf(false) }
    val cardAlpha   by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label         = "cardAlpha"
    )
    val cardOffsetY by animateFloatAsState(
        targetValue   = if (visible) 0f else 48f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label         = "cardOffsetY"
    )
    LaunchedEffect(Unit) { visible = true }

    // ── Acciones existentes (sin cambios) ───────────────────────────────────
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

    // ── Root ────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        // 1 · Foto de fondo
        Image(
            painter        = painterResource(id = R.drawable.fondo_def),
            contentDescription = null,
            modifier       = Modifier.fillMaxSize(),
            contentScale   = ContentScale.Crop
        )

        // 2 · Overlay en capas: blur suave + degradado blanco desde abajo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.00f to Color.Black.copy(alpha = 0.08f),
                        0.30f to Color.White.copy(alpha = 0.45f),
                        0.60f to Color.White.copy(alpha = 0.82f),
                        1.00f to Color.White.copy(alpha = 0.97f)
                    )
                )
        )

        // 3 · Contenido centrado con animación
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter)   // sube desde abajo — sensación de "sheet"
                .graphicsLayer {
                    alpha        = cardAlpha
                    translationY = cardOffsetY
                }
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp, top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Wordmark sobre la card ──────────────────────────────────────
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Ink, fontWeight = FontWeight.Black)) {
                        append("Sales")
                    }
                    withStyle(SpanStyle(color = AccentCyan, fontWeight = FontWeight.Black)) {
                        append("Gow")
                    }
                },
                fontSize      = 42.sp,
                letterSpacing = (-1.5).sp,
                modifier      = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text          = "Ventas rápidas · Control inteligente",
                color         = InkSoft,
                fontSize      = 13.sp,
                letterSpacing = 0.3.sp,
                modifier      = Modifier.padding(bottom = 28.dp)
            )

            // ── Card glassmorphism ──────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(24.dp),
                colors   = CardDefaults.cardColors(containerColor = GlassWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border   = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = InkXSoft.copy(alpha = 0.6f)
                )
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Chip / etiqueta de acceso
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = AccentRing,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Text(
                            text     = "Acceso seguro",
                            color    = AccentCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                        )
                    }

                    Text(
                        text       = "Bienvenido de vuelta",
                        color      = Ink,
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier   = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text     = "Elige cómo quieres acceder",
                        color    = InkSoft,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // ── Botón Google ───────────────────────────────────────
                    GoogleSignInButton(
                        onClick  = { onGoogleSignInClick() },
                        enabled  = !isGoogleLoading
                    )

                    // ── Separador "o" ──────────────────────────────────────
                    Row(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment   = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier  = Modifier.weight(1f),
                            thickness = 1.dp,
                            color     = InkXSoft
                        )
                        Text(
                            text     = "  o  ",
                            color    = InkSoft,
                            fontSize = 12.sp
                        )
                        HorizontalDivider(
                            modifier  = Modifier.weight(1f),
                            thickness = 1.dp,
                            color     = InkXSoft
                        )
                    }

                    // ── Botón Email ────────────────────────────────────────
                    DefaultButton(
                        modifier  = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        text      = "Continuar con email",
                        onClick   = { navController.navigate(ClientScreen.BasicLogin.route) },
                        color     = AccentCyan,
                        icon      = Icons.Default.Email,
                        textColor = PureWhite
                    )

                    // ── Footer ─────────────────────────────────────────────
                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color     = InkXSoft.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(AccentCyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text       = Config.APP_NAME,
                            color      = InkSoft,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text  = "· 2026",
                            color = InkXSoft,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    // Loading overlay (sin cambios)
    CustomProgressBar(isLoading = isGoogleLoading)
}
