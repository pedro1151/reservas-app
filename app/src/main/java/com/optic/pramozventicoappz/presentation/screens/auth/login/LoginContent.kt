package com.optic.pramozventicoappz.presentation.screens.auth.login

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
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
import com.optic.pramozventicoappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

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

    var visible by remember { mutableStateOf(false) }

    val cardAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "cardAlpha"
    )

    val cardOffsetY by animateFloatAsState(
        targetValue = if (visible) 0f else 48f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "cardOffsetY"
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

        Image(
            painter = painterResource(id = R.drawable.fondo_def),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = 0.72f
                },
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to MaterialTheme.colorScheme.primary.copy(alpha = 0.46f),
                            0.28f to MaterialTheme.colorScheme.primary.copy(alpha = 0.30f),
                            0.58f to Color.White.copy(alpha = 0.72f),
                            1.00f to MaterialTheme.colorScheme.background.copy(alpha = 0.97f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AmarrilloSuave.copy(alpha = 0.34f),
                                Color.Transparent
                            ),
                            radius = size.width * 0.68f
                        ),
                        center = center.copy(
                            x = size.width * 0.08f,
                            y = size.height * 0.10f
                        )
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFE91E63),
                                Color.Transparent
                            ),
                            radius = size.width * 0.82f
                        ),
                        center = center.copy(
                            x = size.width * 1.05f,
                            y = size.height * 0.34f
                        )
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.32f),
                                Color.Transparent
                            ),
                            radius = size.width * 0.90f
                        ),
                        center = center.copy(
                            x = size.width * 0.50f,
                            y = size.height * 0.96f
                        )
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = cardAlpha
                    translationY = cardOffsetY
                }
                .padding(horizontal = 20.dp)
                .padding(top = 96.dp, bottom = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppBrandHeader()

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.96f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.78f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                        ),
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Text(
                            text = "GESTIONA TU NEGOCIO",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    Text(
                        text = "Empieza ya!",
                        color = TextPrimary,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.4).sp,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )

                    Text(
                        text = "Ventas rápidas. Todo tu negocio, en segundos",
                        color = TextSecondary,
                        fontSize = 15.5.sp,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    GoogleSignInButton(
                        onClick = { onGoogleSignInClick() },
                        enabled = !isGoogleLoading
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = BorderGray.copy(alpha = 0.90f)
                        )

                        Text(
                            text = "  o  ",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = BorderGray.copy(alpha = 0.90f)
                        )
                    }

                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        text = "Continuar con email",
                        onClick = { navController.navigate(ClientScreen.BasicLogin.route) },
                        color = MaterialTheme.colorScheme.primary,
                        icon = Icons.Default.Email,
                        textColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = BorderGray.copy(alpha = 0.55f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = Config.APP_NAME,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "· 2026",
                            color = BorderGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    CustomProgressBar(isLoading = isGoogleLoading)
}

@Composable
private fun AppBrandHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.18f),
            border = BorderStroke(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.28f)
            )
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    ) {
                        append("Sales")
                    }

                    withStyle(
                        SpanStyle(
                            color = AmarrilloSuave,
                            fontWeight = FontWeight.Black
                        )
                    ) {
                        append("Gow")
                    }
                },
                fontSize = 43.sp,
                letterSpacing = (-1.6).sp,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Ventas rápidas · Control inteligente de tu negocio",
            color = Color.White.copy(alpha = 0.92f),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp
        )
    }
}