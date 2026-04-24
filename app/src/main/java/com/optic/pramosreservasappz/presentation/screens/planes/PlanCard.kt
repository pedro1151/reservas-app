package com.optic.pramosreservasappz.presentation.screens.planes

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── STANDARD – Navy / Mint (igual que antes) ─────────────────────────────────
private val StdGradStart  = Color(0xFF0D1B2A)
private val StdGradMid    = Color(0xFF0F2D4A)
private val StdGradEnd    = Color(0xFF183554)
private val StdMint       = Color(0xFF2DD4A0)
private val StdMintDim    = Color(0x302DD4A0)
private val StdTextDim    = Color(0x88FFFFFF)
private val StdTextSoft   = Color(0xCCFFFFFF)

// ─── PRO – Purple / Violet gradient card ─────────────────────────────────────
private val ProGradStart  = Color(0xFF1E0A3C)
private val ProGradMid    = Color(0xFF2D1060)
private val ProGradEnd    = Color(0xFF3B0F82)
private val ProViolet     = Color(0xFFA78BFA)
private val ProVioletBright = Color(0xFFC4B5FD)
private val ProVioletDim  = Color(0x2EA78BFA)
private val ProVioletBorder = Color(0xFF7C3AED)
private val ProTextDim    = Color(0x88FFFFFF)
private val ProTextSoft   = Color(0xCCFFFFFF)
private val ProPopularBg  = Color(0xFF7C3AED)

// ─── GOLD – Deep Amber / Orange gradient card ─────────────────────────────────
private val GoldGradStart = Color(0xFF1C0A00)
private val GoldGradMid   = Color(0xFF3D1400)
private val GoldGradEnd   = Color(0xFF5C2000)
private val GoldAmber     = Color(0xFFFBBF24)
private val GoldOrange    = Color(0xFFF97316)
private val GoldAmberDim  = Color(0x28FBB024)
private val GoldBorder    = Color(0xFFD97706)
private val GoldTextDim   = Color(0x88FFFFFF)
private val GoldTextSoft  = Color(0xCCFFFFFF)

// ─── Feature row (shared) ─────────────────────────────────────────────────────
@Composable
private fun FeatureRow(text: String, checkColor: Color, bubbleColor: Color) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier          = Modifier.padding(vertical = 5.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier         = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
                .background(bubbleColor, CircleShape)
        ) {
            Icon(
                imageVector        = Icons.Default.Check,
                contentDescription = null,
                tint               = checkColor,
                modifier           = Modifier.size(11.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text       = text,
            fontSize   = 13.5.sp,
            color      = Color.White.copy(alpha = 0.80f),
            lineHeight = 20.sp
        )
    }
}

// ─── Price block (shared) ─────────────────────────────────────────────────────
@Composable
private fun PriceBlock(price: String, period: String, color: Color) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text          = price,
            fontSize      = 58.sp,
            fontWeight    = FontWeight.Black,
            color         = Color.White,
            letterSpacing = (-3).sp,
            lineHeight    = 58.sp
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text     = period,
            fontSize = 13.sp,
            color    = color,
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  STANDARD  –  Navy / Mint  (same vibe as before, refined)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun StandardPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    badge: String,
    label: String,
    features: List<String>,
    onSelect: () -> Unit = {},
    animDelay: Int = 80
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        if (visible) 1f else 0f,
        tween(520, animDelay, FastOutSlowInEasing), label = "sA"
    )
    val slide by animateFloatAsState(
        if (visible) 0f else 28f,
        tween(520, animDelay, FastOutSlowInEasing), label = "sS"
    )
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { this.alpha = alpha; translationY = slide }
            .shadow(
                elevation    = 28.dp,
                shape        = RoundedCornerShape(26.dp),
                spotColor    = StdMint.copy(alpha = 0.18f),
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
            // Subtle mint border
            .border(1.dp, StdMint.copy(alpha = 0.18f), RoundedCornerShape(26.dp))
    ) {
        // Top mint glow strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, StdMint, StdMint.copy(0.4f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 26.dp)
        ) {
            // Label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Star,
                    contentDescription = null,
                    tint               = StdMint,
                    modifier           = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text          = label,
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = StdMint,
                    letterSpacing = 1.4.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                title,
                fontSize      = 26.sp,
                fontWeight    = FontWeight.ExtraBold,
                color         = Color.White,
                letterSpacing = (-0.8).sp
            )
            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = StdTextDim, lineHeight = 18.sp)

            Spacer(Modifier.height(22.dp))

            PriceBlock(price = price, period = period, color = StdTextDim)

            Spacer(Modifier.height(12.dp))

            // Badge pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(StdMintDim)
                    .border(1.dp, StdMint.copy(0.35f), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(badge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = StdMint)
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(0.10f), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            features.forEach { FeatureRow(it, StdMint, StdMintDim) }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick   = onSelect,
                modifier  = Modifier.fillMaxWidth().height(54.dp),
                shape     = RoundedCornerShape(16.dp),
                colors    = ButtonDefaults.buttonColors(containerColor = StdMint),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    "Comenzar ahora",
                    color      = StdGradStart,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 15.sp
                )
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  PRO  –  Deep Purple / Violet
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ProPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    badge: String,
    label: String,
    features: List<String>,
    onSelect: () -> Unit = {},
    animDelay: Int = 180
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        if (visible) 1f else 0f,
        tween(520, animDelay, FastOutSlowInEasing), label = "pA"
    )
    val slide by animateFloatAsState(
        if (visible) 0f else 28f,
        tween(520, animDelay, FastOutSlowInEasing), label = "pS"
    )
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { this.alpha = alpha; translationY = slide }
            .shadow(
                elevation    = 28.dp,
                shape        = RoundedCornerShape(26.dp),
                spotColor    = ProViolet.copy(alpha = 0.30f),
                ambientColor = ProGradStart.copy(alpha = 0.20f)
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
            .border(1.5.dp, ProVioletBorder.copy(0.5f), RoundedCornerShape(26.dp))
    ) {
        // Top violet glow strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, ProVioletBright, ProViolet.copy(0.4f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 26.dp)
        ) {
            // Label + "Más popular" chip
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Bolt,
                        contentDescription = null,
                        tint               = ProVioletBright,
                        modifier           = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        label,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = ProVioletBright,
                        letterSpacing = 1.4.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(listOf(ProViolet, ProVioletBorder))
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        "Más popular",
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = Color.White,
                        letterSpacing = 0.2.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                title,
                fontSize      = 26.sp,
                fontWeight    = FontWeight.ExtraBold,
                color         = Color.White,
                letterSpacing = (-0.8).sp
            )
            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = ProTextDim, lineHeight = 18.sp)

            Spacer(Modifier.height(22.dp))

            PriceBlock(price = price, period = period, color = ProTextDim)

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(ProVioletDim)
                    .border(1.dp, ProViolet.copy(0.4f), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(badge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ProVioletBright)
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(0.10f), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            features.forEach { FeatureRow(it, ProVioletBright, ProVioletDim) }

            Spacer(Modifier.height(30.dp))

            // Gradient CTA
            Box(
                modifier         = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(listOf(ProViolet, ProVioletBorder))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick   = onSelect,
                    modifier  = Modifier.fillMaxSize(),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        "Elegir Pro",
                        color      = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 15.sp
                    )
                }
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  GOLD  –  Deep Amber / Orange
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun GoldPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    badge: String,
    label: String,
    features: List<String>,
    onSelect: () -> Unit = {},
    animDelay: Int = 280
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        if (visible) 1f else 0f,
        tween(520, animDelay, FastOutSlowInEasing), label = "gA"
    )
    val slide by animateFloatAsState(
        if (visible) 0f else 28f,
        tween(520, animDelay, FastOutSlowInEasing), label = "gS"
    )
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { this.alpha = alpha; translationY = slide }
            .shadow(
                elevation    = 28.dp,
                shape        = RoundedCornerShape(26.dp),
                spotColor    = GoldAmber.copy(alpha = 0.28f),
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
            .border(1.5.dp, GoldBorder.copy(0.55f), RoundedCornerShape(26.dp))
    ) {
        // Top amber glow strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, GoldAmber, GoldOrange.copy(0.5f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 26.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.WorkspacePremium,
                    contentDescription = null,
                    tint               = GoldAmber,
                    modifier           = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    label,
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = GoldAmber,
                    letterSpacing = 1.4.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                title,
                fontSize      = 26.sp,
                fontWeight    = FontWeight.ExtraBold,
                color         = Color.White,
                letterSpacing = (-0.8).sp
            )
            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = GoldTextDim, lineHeight = 18.sp)

            Spacer(Modifier.height(22.dp))

            PriceBlock(price = price, period = period, color = GoldTextDim)

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(GoldAmberDim)
                    .border(1.dp, GoldAmber.copy(0.4f), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(badge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = GoldAmber)
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(0.10f), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            features.forEach { FeatureRow(it, GoldAmber, GoldAmberDim) }

            Spacer(Modifier.height(30.dp))

            // Gradient CTA amber → orange
            Box(
                modifier         = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(listOf(GoldAmber, GoldOrange))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick   = onSelect,
                    modifier  = Modifier.fillMaxSize(),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        "Elegir Gold",
                        color      = GoldGradStart,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 15.sp
                    )
                }
            }
        }
    }
}
