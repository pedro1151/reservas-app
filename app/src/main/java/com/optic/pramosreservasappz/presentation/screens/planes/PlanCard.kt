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

// ── Shared tokens ─────────────────────────────────────────────────────────────
val PlanNavyStart  = Color(0xFF0D1B2A)
val PlanNavyEnd    = Color(0xFF163352)
val PlanMintAccent = Color(0xFF2DD4A0)

// Pro – indigo
private val ProBorder     = Color(0xFFC7D2FE)
private val ProBackground = Color(0xFFFFFFFF)
private val ProTitleColor = Color(0xFF3730A3)
private val ProAccent     = Color(0xFF6366F1)
private val ProSurface    = Color(0xFFEEF2FF)
private val ProTextColor  = Color(0xFF4338CA)

// Gold – amber
private val GoldBorder     = Color(0xFFFDE68A)
private val GoldBackground = Color(0xFFFFFBF0)
private val GoldTitleColor = Color(0xFF92400E)
private val GoldAccent     = Color(0xFFF59E0B)
private val GoldAccentEnd  = Color(0xFFFBBF24)
private val GoldSurface    = Color(0xFFFEF3C7)
private val GoldTextColor  = Color(0xFF92400E)
private val GoldPriceColor = Color(0xFFB45309)


// ─────────────────────────────────────────────────────────────────────────────
//  Featured  ·  Estándar
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FeaturedPlanCard(
    title: String,
    subtitle: String,
    price: String,
    period: String,
    features: List<String>
) {
    val gradient = Brush.linearGradient(
        colorStops = arrayOf(0.0f to PlanNavyStart, 1.0f to PlanNavyEnd)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(28.dp, RoundedCornerShape(26.dp),
                spotColor = PlanNavyStart.copy(alpha = 0.35f),
                ambientColor = PlanNavyStart.copy(alpha = 0.12f))
            .clip(RoundedCornerShape(26.dp))
            .background(brush = gradient)
            .padding(horizontal = 26.dp, vertical = 28.dp)
    ) {
        Column {
            Text(title,    fontSize = 22.sp, fontWeight = FontWeight.Bold,  color = Color.White, letterSpacing = (-0.3).sp)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, fontSize = 13.sp, color = Color.White.copy(alpha = 0.55f), lineHeight = 18.sp)
            Spacer(Modifier.height(26.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(price,  fontSize = 52.sp, fontWeight = FontWeight.ExtraBold,
                    color = Color.White, letterSpacing = (-2).sp, lineHeight = 52.sp)
                Spacer(Modifier.width(6.dp))
                Text(period, fontSize = 14.sp, color = Color.White.copy(alpha = 0.55f),
                    modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f), thickness = 1.dp)
            Spacer(Modifier.height(20.dp))

            features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 5.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(20.dp)
                            .background(PlanMintAccent.copy(alpha = 0.18f), CircleShape)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null,
                            tint = PlanMintAccent, modifier = Modifier.size(12.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(feature, fontSize = 14.sp, color = Color.White.copy(alpha = 0.82f), lineHeight = 20.sp)
                }
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick   = { },
                modifier  = Modifier.fillMaxWidth().height(54.dp),
                shape     = RoundedCornerShape(17.dp),
                colors    = ButtonDefaults.buttonColors(containerColor = PlanMintAccent),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Comenzar ahora", color = PlanNavyStart, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  Pro Plan Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ProPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    features: List<String>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = ProAccent.copy(alpha = 0.1f))
            .clip(RoundedCornerShape(22.dp))
            .background(ProBackground)
            .border(1.5.dp, ProBorder, RoundedCornerShape(22.dp))
            .padding(horizontal = 22.dp, vertical = 20.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = ProTitleColor)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(price,  fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        color = ProTitleColor, letterSpacing = (-0.4).sp)
                    Text(" $period", fontSize = 11.sp, color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(bottom = 2.dp))
                }
            }

            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = ProAccent, lineHeight = 17.sp)
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = ProSurface, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))

            features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 3.5.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(16.dp)
                            .background(ProAccent.copy(alpha = 0.12f), CircleShape)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null,
                            tint = ProAccent, modifier = Modifier.size(9.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(feature, fontSize = 12.5.sp, color = ProTextColor, lineHeight = 18.sp)
                }
            }

            Spacer(Modifier.height(18.dp))

            Button(
                onClick   = { },
                modifier  = Modifier.fillMaxWidth().height(44.dp),
                shape     = RoundedCornerShape(13.dp),
                colors    = ButtonDefaults.buttonColors(containerColor = ProSurface),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Elegir Pro", color = ProTextColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  Gold Plan Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun GoldPlanCard(
    title: String,
    tagline: String,
    price: String,
    period: String,
    features: List<String>
) {
    val goldGradient = Brush.horizontalGradient(listOf(GoldAccent, GoldAccentEnd))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), spotColor = GoldAccent.copy(alpha = 0.18f))
            .clip(RoundedCornerShape(22.dp))
            .background(GoldBackground)
            .border(1.5.dp, GoldBorder, RoundedCornerShape(22.dp))
            .padding(horizontal = 22.dp, vertical = 20.dp)
    ) {
        Column {
            // Gold badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(brush = goldGradient)
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text("✦ GOLD", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                    color = Color.White, letterSpacing = 0.5.sp)
            }

            Spacer(Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = GoldTitleColor)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(price,  fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        color = GoldPriceColor, letterSpacing = (-0.4).sp)
                    Text(" $period", fontSize = 11.sp, color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(bottom = 2.dp))
                }
            }

            Spacer(Modifier.height(4.dp))
            Text(tagline, fontSize = 12.sp, color = GoldAccent, lineHeight = 17.sp)
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = GoldSurface, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))

            features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 3.5.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(16.dp)
                            .background(GoldAccent.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null,
                            tint = GoldAccent, modifier = Modifier.size(9.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(feature, fontSize = 12.5.sp, color = GoldTextColor, lineHeight = 18.sp)
                }
            }

            Spacer(Modifier.height(18.dp))

            // Gradient CTA button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(brush = goldGradient),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick   = { },
                    modifier  = Modifier.fillMaxSize(),
                    shape     = RoundedCornerShape(13.dp),
                    colors    = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Elegir Gold", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
