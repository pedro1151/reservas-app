package com.optic.pramozventicoappz.presentation.screens.planes.seleccionarplanmode.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.planes.BillingMode
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

@Composable
fun BillingOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    price: String,
    period: String,
    badge: String?,
    helper: String? = null,
    isSelected: Boolean,
    primary: Color,
    accent: Color,
    accentSoft: Color,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) accent.copy(alpha = 0.85f) else BorderGray.copy(alpha = 0.75f),
        label = "billingBorder"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.015f else 1f,
        animationSpec = spring(),
        label = "billingScale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isSelected) 7.dp else 3.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = if (isSelected) 0.05f else 0.025f),
                spotColor = Color.Black.copy(alpha = if (isSelected) 0.08f else 0.04f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = if (isSelected) 1.4.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(13.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 16.sp,
                        maxLines = 2
                    )
                }

                Spacer(Modifier.width(6.dp))

                SelectionCircle(
                    selected = isSelected,
                    primary = primary
                )
            }

            if (badge != null || helper != null) {
                Spacer(Modifier.height(10.dp))

                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(accentSoft)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8A6100),
                            maxLines = 1
                        )
                    }
                }

                if (helper != null) {
                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = helper,
                        fontSize = 11.sp,
                        color = Color(0xFF8A6100),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = price,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                letterSpacing = (-0.8).sp,
                maxLines = 1
            )

            Spacer(Modifier.height(1.dp))

            Text(
                text = period,
                fontSize = 11.5.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SelectionCircle(
    selected: Boolean,
    primary: Color
) {
    val bg by animateColorAsState(
        targetValue = if (selected) primary else Color.Transparent,
        label = "selectBg"
    )

    val border by animateColorAsState(
        targetValue = if (selected) primary else BorderGray,
        label = "selectBorder"
    )

    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(bg)
            .border(
                width = 1.5.dp,
                color = border,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun PlanIncludesCard(
    features: List<String>,
    accentSoft: Color,
    accentText: Color,
    surface: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(surface)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.65f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Incluye",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(Modifier.height(12.dp))

        features.forEach { feature ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(23.dp)
                        .clip(CircleShape)
                        .background(accentSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = accentText,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = feature,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun SelectPlanBottomBar(
    selectedMode: BillingMode,
    monthlyPrice: Double,
    annualPrice: Double,
    annualMonthlyEquivalent: Double,
    primary: Color,
    surface: Color,
    onContinue: () -> Unit
) {
    val amountText = when (selectedMode) {
        BillingMode.MONTHLY -> "$${"%.0f".format(monthlyPrice)}"
        BillingMode.YEARLY -> "$${"%.0f".format(annualPrice)}"
    }

    val periodText = when (selectedMode) {
        BillingMode.MONTHLY -> "usd / mes"
        BillingMode.YEARLY -> "usd / año"
    }

    val helperText = when (selectedMode) {
        BillingMode.MONTHLY -> "Plan mensual PRO"
        BillingMode.YEARLY -> "30% menos · $${"%.2f".format(annualMonthlyEquivalent)} usd / mes"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            )
            .background(
                color = surface,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
            )
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = amountText,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = periodText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Spacer(Modifier.height(2.dp))

                Text(
                    text = helperText,
                    fontSize = 12.5.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = {
                        onContinue()
                    // TODO: lanzar Google Play Billing
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 13.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}