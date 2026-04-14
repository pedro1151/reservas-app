package com.optic.pramosreservasappz.presentation.screens.planes


import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno


@Composable
fun PlanCard(
    title: String,
    price: String,
    features: List<String>,
    color: Color,
    highlight: Boolean = false,
    badge: String? = null
) {

    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF64748B)
    val BorderSoft = Color(0xFFE2E8F0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (highlight) 16.dp else 10.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = if (highlight) 2.dp else 1.dp,
                color = if (highlight) color else BorderSoft,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {

        if (badge != null) {
            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    badge,
                    color = color,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))
        }

        Text(
            title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(Modifier.height(4.dp))

        Text(
            price,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )

        Spacer(Modifier.height(12.dp))

        features.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(color, CircleShape)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    it,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color
            )
        ) {
            Text("Elegir plan", color = Color.White)
        }
    }
}