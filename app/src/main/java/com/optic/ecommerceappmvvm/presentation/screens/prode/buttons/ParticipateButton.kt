package com.optic.ecommerceappmvvm.presentation.screens.prode.buttons

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.optic.ecommerceappmvvm.presentation.ui.theme.followButtonBackground
import com.optic.ecommerceappmvvm.presentation.ui.theme.followTextColor
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenColorFixture

@Composable
fun ParticipateButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    var pressed by remember  { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = ""
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {


            Text(
                text = "Participar",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                   // color =  Color(0xFFFFC857)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            // CÍRCULO PREMIUM PARA EL PLAY
            Box(
                modifier = Modifier
                    .size(22.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    //tint = Color(0xFFFFC857)
                )
            }
        }
    }
}
