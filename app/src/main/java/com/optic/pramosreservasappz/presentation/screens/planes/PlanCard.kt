package com.optic.pramosreservasappz.presentation.screens.planes

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val StdGradStart = Color(0xFF0D1B2A)
private val StdGradMid = Color(0xFF0F2D4A)
private val StdGradEnd = Color(0xFF183554)
private val StdMint = Color(0xFF2DD4A0)
private val StdMintDim = Color(0x302DD4A0)
private val StdTextDim = Color.White.copy(alpha = 0.72f)
private val StdTextSoft = Color.White.copy(alpha = 0.86f)

private val ProGradStart = Color(0xFF1E0A3C)
private val ProGradMid = Color(0xFF2D1060)
private val ProGradEnd = Color(0xFF3B0F82)
private val ProViolet = Color(0xFFA78BFA)
private val ProVioletBright = Color(0xFFC4B5FD)
private val ProVioletDim = Color(0x2EA78BFA)
private val ProVioletBorder = Color(0xFF7C3AED)
private val ProTextDim = Color.White.copy(alpha = 0.72f)
private val ProTextSoft = Color.White.copy(alpha = 0.86f)

private val GoldGradStart = Color(0xFF1C0A00)
private val GoldGradMid = Color(0xFF3D1400)
private val GoldGradEnd = Color(0xFF5C2000)
private val GoldAmber = Color(0xFFFBBF24)
private val GoldOrange = Color(0xFFF97316)
private val GoldAmberDim = Color(0x28FBB024)
private val GoldBorder = Color(0xFFD97706)
private val GoldTextDim = Color.White.copy(alpha = 0.72f)
private val GoldTextSoft = Color.White.copy(alpha = 0.86f)

@Composable
private fun FeatureRow(
    text: String,
    checkColor: Color,
    bubbleColor: Color
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
                .background(bubbleColor, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier.size(11.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 13.5.sp,
            color = Color.White.copy(alpha = 0.86f),
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun PriceBlock(
    price: String,
    period: String,
    color: Color,
    summary: String
) {
    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = price,
                fontSize = 52.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-2.5).sp,
                lineHeight = 54.sp
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = period,
                fontSize = 13.sp,
                color = color,
                modifier = Modifier.padding(bottom = 9.dp)
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = summary,
            fontSize = 12.5.sp,
            color = Color.White.copy(alpha = 0.78f),
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FeaturesBlock(
    features: List<String>,
    checkColor: Color,
    bubbleColor: Color
) {
    Text(
        text = "Incluye:",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White.copy(alpha = 0.92f),
        letterSpacing = 0.3.sp
    )

    Spacer(Modifier.height(8.dp))

    features.forEach {
        FeatureRow(
            text = it,
            checkColor = checkColor,
            bubbleColor = bubbleColor
        )
    }
}

@Composable
private fun CurrentPlanChip(
    color: Color,
    contentColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.20f))
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.45f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = "Plan actual",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            letterSpacing = 0.2.sp
        )
    }
}

@Composable
private fun AnimatedPlanButton(
    text: String,
    enabled: Boolean,
    textColor: Color,
    background: Brush,
    disabledColor: Color = Color.White.copy(alpha = 0.12f),
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = tween(120),
        label = "btnScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (enabled) background
                else Brush.horizontalGradient(listOf(disabledColor, disabledColor))
            ),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Text(
                text = text,
                color = if (enabled) textColor else Color.White.copy(alpha = 0.62f),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun StandardPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    badge: String,
    label: String,
    features: List<String>,
    currentPlanCode: String = "",
    planCode: String = "standard",
    summary: String = "Ideal para 1 negocio · hasta 5 colaboradores",
    onSelect: () -> Unit = {},
    animDelay: Int = 80
) {
    val isCurrentPlan = currentPlanCode.equals(planCode, ignoreCase = true)

    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(520, animDelay, FastOutSlowInEasing),
        label = "sA"
    )

    val slide by animateFloatAsState(
        targetValue = if (visible) 0f else 28f,
        animationSpec = tween(520, animDelay, FastOutSlowInEasing),
        label = "sS"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
                translationY = slide
            }
            .shadow(
                elevation = 28.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = StdMint.copy(alpha = 0.18f),
                ambientColor = StdGradStart.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to StdGradStart,
                        0.5f to StdGradMid,
                        1.0f to StdGradEnd
                    )
                )
            )
            .border(
                width = if (isCurrentPlan) 1.6.dp else 1.dp,
                color = if (isCurrentPlan) StdMint.copy(alpha = 0.65f) else StdMint.copy(alpha = 0.18f),
                shape = RoundedCornerShape(26.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            StdMint,
                            StdMint.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 26.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = StdMint,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = StdMint,
                        letterSpacing = 1.4.sp
                    )
                }

                if (isCurrentPlan) {
                    CurrentPlanChip(
                        color = StdMint,
                        contentColor = StdMint
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = (-0.8).sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = tagline,
                fontSize = 12.sp,
                color = StdTextDim,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(22.dp))

            PriceBlock(
                price = price,
                period = period,
                color = StdTextDim,
                summary = summary
            )

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(StdMintDim)
                    .border(1.dp, StdMint.copy(alpha = 0.35f), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (isCurrentPlan) "Tu suscripción activa" else badge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = StdMint
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.10f), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            FeaturesBlock(
                features = features,
                checkColor = StdMint,
                bubbleColor = StdMintDim
            )

            Spacer(Modifier.height(30.dp))

            AnimatedPlanButton(
                text = if (isCurrentPlan) "Plan actual" else "Comenzar ahora",
                enabled = !isCurrentPlan,
                textColor = StdGradStart,
                background = Brush.horizontalGradient(listOf(StdMint, StdMint.copy(alpha = 0.85f))),
                onClick = onSelect
            )
        }
    }
}

@Composable
fun ProPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    badge: String,
    label: String,
    features: List<String>,
    currentPlanCode: String = "",
    planCode: String = "pro",
    summary: String = "Ideal para crecer · hasta 10 colaboradores",
    onSelect: () -> Unit = {},
    animDelay: Int = 180
) {
    val isCurrentPlan = currentPlanCode.equals(planCode, ignoreCase = true)

    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(520, animDelay, FastOutSlowInEasing),
        label = "pA"
    )

    val slide by animateFloatAsState(
        targetValue = if (visible) 0f else 28f,
        animationSpec = tween(520, animDelay, FastOutSlowInEasing),
        label = "pS"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
                translationY = slide
            }
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = ProViolet.copy(alpha = 0.38f),
                ambientColor = ProGradStart.copy(alpha = 0.22f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to ProGradStart,
                        0.5f to ProGradMid,
                        1.0f to ProGradEnd
                    )
                )
            )
            .border(
                width = if (isCurrentPlan) 2.dp else 1.5.dp,
                color = if (isCurrentPlan) ProVioletBright.copy(alpha = 0.85f) else ProVioletBorder.copy(alpha = 0.5f),
                shape = RoundedCornerShape(26.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            ProVioletBright,
                            ProViolet.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 26.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Bolt,
                        contentDescription = null,
                        tint = ProVioletBright,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProVioletBright,
                        letterSpacing = 1.4.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isCurrentPlan) {
                                Brush.horizontalGradient(listOf(ProVioletDim, ProVioletDim))
                            } else {
                                Brush.horizontalGradient(listOf(ProViolet, ProVioletBorder))
                            }
                        )
                        .border(
                            width = if (isCurrentPlan) 1.dp else 0.dp,
                            color = if (isCurrentPlan) ProVioletBright.copy(alpha = 0.45f) else Color.Transparent,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isCurrentPlan) "Plan actual" else "Más popular",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.2.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = (-0.8).sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = tagline,
                fontSize = 12.sp,
                color = ProTextDim,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(22.dp))

            PriceBlock(
                price = price,
                period = period,
                color = ProTextDim,
                summary = summary
            )

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(ProVioletDim)
                    .border(1.dp, ProViolet.copy(alpha = 0.4f), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (isCurrentPlan) "Tu suscripción activa" else badge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProVioletBright
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.10f), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            FeaturesBlock(
                features = features,
                checkColor = ProVioletBright,
                bubbleColor = ProVioletDim
            )

            Spacer(Modifier.height(30.dp))

            AnimatedPlanButton(
                text = if (isCurrentPlan) "Plan actual" else "Elegir Pro",
                enabled = !isCurrentPlan,
                textColor = Color.White,
                background = Brush.horizontalGradient(listOf(ProViolet, ProVioletBorder)),
                onClick = onSelect
            )
        }
    }
}

@Composable
fun GoldPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    badge: String,
    label: String,
    features: List<String>,
    currentPlanCode: String = "",
    planCode: String = "gold",
    summary: String = "Para alto volumen · hasta 20 colaboradores",
    onSelect: () -> Unit = {},
    animDelay: Int = 280
) {
    val isCurrentPlan = currentPlanCode.equals(planCode, ignoreCase = true)

    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(520, animDelay, FastOutSlowInEasing),
        label = "gA"
    )

    val slide by animateFloatAsState(
        targetValue = if (visible) 0f else 28f,
        animationSpec = tween(520, animDelay, FastOutSlowInEasing),
        label = "gS"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
                translationY = slide
            }
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = GoldAmber.copy(alpha = 0.34f),
                ambientColor = GoldGradStart.copy(alpha = 0.22f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to GoldGradStart,
                        0.5f to GoldGradMid,
                        1.0f to GoldGradEnd
                    )
                )
            )
            .border(
                width = if (isCurrentPlan) 2.dp else 1.5.dp,
                color = if (isCurrentPlan) GoldAmber.copy(alpha = 0.90f) else GoldBorder.copy(alpha = 0.55f),
                shape = RoundedCornerShape(26.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            GoldAmber,
                            GoldOrange.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 26.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.WorkspacePremium,
                        contentDescription = null,
                        tint = GoldAmber,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAmber,
                        letterSpacing = 1.4.sp
                    )
                }

                if (isCurrentPlan) {
                    CurrentPlanChip(
                        color = GoldAmber,
                        contentColor = GoldAmber
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = (-0.8).sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = tagline,
                fontSize = 12.sp,
                color = GoldTextDim,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(22.dp))

            PriceBlock(
                price = price,
                period = period,
                color = GoldTextDim,
                summary = summary
            )

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(GoldAmberDim)
                    .border(1.dp, GoldAmber.copy(alpha = 0.4f), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (isCurrentPlan) "Tu suscripción activa" else badge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldAmber
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.10f), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            FeaturesBlock(
                features = features,
                checkColor = GoldAmber,
                bubbleColor = GoldAmberDim
            )

            Spacer(Modifier.height(30.dp))

            AnimatedPlanButton(
                text = if (isCurrentPlan) "Plan actual" else "Elegir Gold",
                enabled = !isCurrentPlan,
                textColor = GoldGradStart,
                background = Brush.horizontalGradient(listOf(GoldAmber, GoldOrange)),
                onClick = onSelect
            )
        }
    }
}