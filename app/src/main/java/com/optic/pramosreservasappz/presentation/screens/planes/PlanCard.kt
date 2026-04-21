package com.optic.pramosreservasappz.presentation.screens.planes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Standard – Navy / Mint ────────────────────────────────────────────────────
private val StdNavyStart  = Color(0xFF0D1B2A)
private val StdNavyEnd    = Color(0xFF183554)
private val StdMint       = Color(0xFF2DD4A0)
private val StdMintDim    = Color(0x302DD4A0)
private val StdTextDim    = Color(0x82FFFFFF)
private val StdTextSoft   = Color(0xD1FFFFFF)

// ── Pro – Deep Indigo ─────────────────────────────────────────────────────────
private val ProBg         = Color(0xFF1A1040)
private val ProBorder     = Color(0xFF4338CA)
private val ProAccent     = Color(0xFF818CF8)
private val ProAccentDim  = Color(0x30818CF8)
private val ProTitle      = Color(0xFFE0E7FF)
private val ProTextDim    = Color(0xB3A5B4FC)
private val ProTextSoft   = Color(0xD9C7D2FE)
private val ProButton     = Color(0xFF3730A3)
private val ProButtonBorder = Color(0xFF4338CA)

// ── Gold – Deep Amber ─────────────────────────────────────────────────────────
private val GoldBg        = Color(0xFF1A1200)
private val GoldBorder    = Color(0xFFD97706)
private val GoldAccent    = Color(0xFFFCD34D)
private val GoldAccentDim = Color(0x26F59E0B)
private val GoldTitle     = Color(0xFFFEF3C7)
private val GoldTextDim   = Color(0x99FCD34D)
private val GoldTextSoft  = Color(0xD9FDE68A)
private val GoldCTAStart  = Color(0xFFD97706)
private val GoldCTAEnd    = Color(0xFFF59E0B)
private val GoldCTAText   = Color(0xFF1A1200)
private val GoldBadgeColor = Color(0xFFFCD34D)


// ─────────────────────────────────────────────────────────────────────────────
//  Shared composable – feature row (reused by all three cards)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun FeatureRow(text: String, checkColor: Color, bubbleColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.5.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
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
        Text(text, fontSize = 13.sp, color = Color.White.copy(alpha = 0.82f), lineHeight = 20.sp)
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  STANDARD  –  hero card
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
    onSelect: () -> Unit = {}
) {
    val gradient = Brush.linearGradient(
        colorStops = arrayOf(0f to StdNavyStart, 1f to StdNavyEnd)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 32.dp,
                shape        = RoundedCornerShape(26.dp),
                spotColor    = StdNavyStart.copy(alpha = 0.4f),
                ambientColor = StdNavyStart.copy(alpha = 0.15f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(gradient)
            .padding(horizontal = 26.dp, vertical = 26.dp)
    ) {
        Column {
            // Label
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = StdMint, letterSpacing = 1.2.sp)
            Spacer(Modifier.height(10.dp))

            // Title + tagline
            Text(title,   fontSize = 24.sp, fontWeight = FontWeight.ExtraBold,
                color = Color.White, letterSpacing = (-0.5).sp)
            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = StdTextDim, lineHeight = 17.sp)
            Spacer(Modifier.height(22.dp))

            // Price
            Row(verticalAlignment = Alignment.Bottom) {
                Text(price,  fontSize = 52.sp, fontWeight = FontWeight.Black,
                    color = Color.White, letterSpacing = (-3).sp, lineHeight = 52.sp)
                Spacer(Modifier.width(6.dp))
                Text(period, fontSize = 13.sp, color = StdTextDim,
                    modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(Modifier.height(8.dp))

            // Badge pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(StdMintDim)
                    .border(1.dp, StdMint.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(badge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = StdMint)
            }

            Spacer(Modifier.height(22.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
            Spacer(Modifier.height(18.dp))

            // Features
            features.forEach {
                FeatureRow(text = it, checkColor = StdMint, bubbleColor = StdMintDim)
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick   = onSelect,
                modifier  = Modifier.fillMaxWidth().height(54.dp),
                shape     = RoundedCornerShape(17.dp),
                colors    = ButtonDefaults.buttonColors(containerColor = StdMint),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Comenzar ahora", color = StdNavyStart,
                    fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  PRO  –  indigo dark card
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
    onSelect: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 18.dp,
                shape        = RoundedCornerShape(26.dp),
                spotColor    = ProAccent.copy(alpha = 0.18f),
                ambientColor = ProAccent.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(ProBg)
            .border(1.5.dp, ProBorder, RoundedCornerShape(26.dp))
            .padding(horizontal = 26.dp, vertical = 26.dp)
    ) {
        Column {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = ProAccent, letterSpacing = 1.2.sp)
            Spacer(Modifier.height(10.dp))

            Text(title,   fontSize = 24.sp, fontWeight = FontWeight.ExtraBold,
                color = ProTitle, letterSpacing = (-0.5).sp)
            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = ProTextDim, lineHeight = 17.sp)
            Spacer(Modifier.height(22.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(price,  fontSize = 52.sp, fontWeight = FontWeight.Black,
                    color = ProTextSoft, letterSpacing = (-3).sp, lineHeight = 52.sp)
                Spacer(Modifier.width(6.dp))
                Text(period, fontSize = 13.sp, color = ProTextDim,
                    modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(ProAccentDim)
                    .border(1.dp, ProAccent.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(badge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ProAccent)
            }

            Spacer(Modifier.height(22.dp))
            HorizontalDivider(color = ProAccent.copy(alpha = 0.15f), thickness = 1.dp)
            Spacer(Modifier.height(18.dp))

            features.forEach {
                FeatureRow(text = it, checkColor = ProAccent, bubbleColor = ProAccentDim)
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick   = onSelect,
                modifier  = Modifier.fillMaxWidth().height(52.dp),
                shape     = RoundedCornerShape(17.dp),
                colors    = ButtonDefaults.buttonColors(containerColor = ProButton),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Elegir Pro", color = ProTitle,
                    fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  GOLD  –  amber dark card
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
    onSelect: () -> Unit = {}
) {
    val ctaGradient = Brush.horizontalGradient(listOf(GoldCTAStart, GoldCTAEnd))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 18.dp,
                shape        = RoundedCornerShape(26.dp),
                spotColor    = GoldAccent.copy(alpha = 0.2f),
                ambientColor = GoldAccent.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(GoldBg)
            .border(1.5.dp, GoldBorder, RoundedCornerShape(26.dp))
            .padding(horizontal = 26.dp, vertical = 26.dp)
    ) {
        Column {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = GoldBadgeColor, letterSpacing = 1.2.sp)
            Spacer(Modifier.height(10.dp))

            Text(title,   fontSize = 24.sp, fontWeight = FontWeight.ExtraBold,
                color = GoldTitle, letterSpacing = (-0.5).sp)
            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = GoldTextDim, lineHeight = 17.sp)
            Spacer(Modifier.height(22.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(price,  fontSize = 52.sp, fontWeight = FontWeight.Black,
                    color = GoldTextSoft, letterSpacing = (-3).sp, lineHeight = 52.sp)
                Spacer(Modifier.width(6.dp))
                Text(period, fontSize = 13.sp, color = GoldTextDim,
                    modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(GoldAccentDim)
                    .border(1.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(badge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = GoldBadgeColor)
            }

            Spacer(Modifier.height(22.dp))
            HorizontalDivider(color = GoldAccent.copy(alpha = 0.15f), thickness = 1.dp)
            Spacer(Modifier.height(18.dp))

            features.forEach {
                FeatureRow(text = it, checkColor = GoldBadgeColor, bubbleColor = GoldAccentDim)
            }

            Spacer(Modifier.height(28.dp))

            // Gradient CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(brush = ctaGradient),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick   = onSelect,
                    modifier  = Modifier.fillMaxSize(),
                    shape     = RoundedCornerShape(17.dp),
                    colors    = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Elegir Gold", color = GoldCTAText,
                        fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                }
            }
        }
    }
}
