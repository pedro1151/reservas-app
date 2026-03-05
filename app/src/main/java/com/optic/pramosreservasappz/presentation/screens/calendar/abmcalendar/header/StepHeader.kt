package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.header



import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ServiceReservationStep
import java.util.*


// ── Step header ────────────────────────────────────────────────────────────────
@Composable
fun StepHeader(currentStep: ServiceReservationStep ){
    Column {
        /*
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 10.dp)
        ) {
            IconButton(onClick = { }, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    imageVector = if (currentStep == ServiceReservationStep.SELECT_SERVICE)
                        Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint     = Color(0xFF1A1A1A),
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text          = currentStep.title,
                fontSize      = 16.sp,
                fontWeight    = FontWeight.W600,
                color         = Color(0xFF111111),
                letterSpacing = (-0.3).sp,
                modifier      = Modifier.align(Alignment.Center)
            )
        }

         */
        val progress by animateFloatAsState(
            targetValue   = (currentStep.index + 1).toFloat() / 4f,
            animationSpec = tween(350, easing = FastOutSlowInEasing),
            label         = "progress"
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color(0xFFF0F0F0))) {
            Box(
                Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(Color(0xFF111111), RoundedCornerShape(1.dp)))
        }
    }
}

