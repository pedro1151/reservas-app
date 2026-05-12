package com.optic.pramozventicoappz.presentation.components.emptystate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.presentation.ui.theme.AccentText
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGrisSoftCard
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

private val Blue600  = Color(0xFF2563EB)
private val Slate900 = Color(0xFF0F172A)
private val Slate400 = Color(0xFF94A3B8)
// ─── Empty Search State ──────────────────────────────────────────────────────────
@Composable
fun DefaultSearchEmptyState(
    query: String,
    label: String = "items"
) {
    val primary =  MaterialTheme.colorScheme.primary
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(92.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(30.dp),
                    ambientColor = Color.Black.copy(alpha = 0.04f),
                    spotColor = primary.copy(alpha = 0.10f),
                    clip = false
                )
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = BorderGrisSoftCard,
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {


            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(com.optic.pramozventicoappz.presentation.ui.theme.AccentSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.SearchOff,
                    null,
                    tint = AccentText,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
            Text(
                "Sin resultados",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                letterSpacing = (-0.1).sp
            )
            Text(
                "No hay \"$label\"que coincidan con \"$query\"",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }

}