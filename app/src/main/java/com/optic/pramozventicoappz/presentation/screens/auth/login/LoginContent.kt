package com.optic.pramozventicoappz.presentation.screens.auth.login

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
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
import com.optic.pramozventicoappz.R
import com.optic.pramozventicoappz.core.Config
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.components.DefaultButton
import com.optic.pramozventicoappz.presentation.components.GoogleSignInButton
import com.optic.pramozventicoappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramozventicoappz.presentation.navigation.Graph
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.ui.theme.AccentText
import com.optic.pramozventicoappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import com.optic.pramozventicoappz.presentation.ui.theme.AccentSecondary
import com.optic.pramozventicoappz.presentation.ui.theme.Grafito

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
    val loginState = vm.loginResponse
    val isGoogleLoading = loginState is Resource.Loading

    val primary = MaterialTheme.colorScheme.primary

    var visible by remember { mutableStateOf(false) }

    val cardAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "loginBoxAlpha"
    )

    val cardOffsetY by animateFloatAsState(
        targetValue = if (visible) 0f else 48f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "loginBoxOffsetY"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(sendCodeSuccess) {
        if (sendCodeSuccess) {
            navController.navigate("${Graph.CLIENT}/${state.email}") {
                popUpTo(ClientScreen.Login.route) {
                    inclusive = false
                }
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
// ── Fondo imagen premium gris moderno ───────────────────────────────


        Image(
            painter = painterResource(id = R.drawable.fondo_claro),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


// ── Glow blanco inferior premium ────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.22f),
                                Color.Transparent
                            ),
                            radius = size.width * 0.90f
                        ),
                        center = center.copy(
                            x = size.width * 0.50f,
                            y = size.height * 1.02f
                        )
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = cardAlpha
                    translationY = cardOffsetY
                }
                .padding(horizontal = 20.dp)
                .padding(top = 80.dp, bottom = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 28.dp,
                        shape = RoundedCornerShape(34.dp),
                        ambientColor = Color.Black.copy(alpha = 0.07f),
                        spotColor = primary.copy(alpha = 0.10f),
                        clip = false
                    )
                    .graphicsLayer {
                        shadowElevation = 10f
                        shape = RoundedCornerShape(34.dp)
                        clip = false
                    }
                    .clip(RoundedCornerShape(34.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.White,
                                Color(0xFFFFFCFD)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.78f),
                        shape = RoundedCornerShape(34.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 30.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppBrandHeaderInsideCard()

                    Spacer(modifier = Modifier.height(22.dp))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "VENTAS RÁPIDAS",
                            color = primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Tu negocio, más rápido",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.55).sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ventas rápidas y control diario ágil",
                        color = TextSecondary,
                        fontSize = 15.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GoogleSignInButton(
                        onClick = { onGoogleSignInClick() },
                        enabled = !isGoogleLoading
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        text = "Continuar con email",
                        onClick = { navController.navigate(ClientScreen.BasicLogin.route) },
                        color = Grafito,
                        icon = Icons.Default.Email,
                        textColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = BorderGray.copy(alpha = 0.48f)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    color = AmarrilloSuave,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Seguro y rápido",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "· ${Config.APP_NAME}",
                            color = TextSecondary.copy(alpha = 0.58f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    CustomProgressBar(isLoading = isGoogleLoading)
}

@Composable
private fun AppBrandHeaderInsideCard() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = TextPrimary,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append("Sales")
                }

                withStyle(
                    SpanStyle(
                        color = AccentSecondary,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append("Gow")
                }
            },
            fontSize = 42.sp,
            letterSpacing = (-1.6).sp
        )
    }
}