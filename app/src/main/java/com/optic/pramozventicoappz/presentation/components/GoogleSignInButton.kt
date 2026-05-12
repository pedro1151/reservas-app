package com.optic.pramozventicoappz.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.R
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(17.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGray.copy(alpha = 0.75f)
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = TextPrimary,
            disabledContainerColor = Color.White,
            disabledContentColor = TextPrimary.copy(alpha = 0.55f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(
                        width = 1.dp,
                        color = BorderGray.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(11.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_icon),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Continuar con Google",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.05.sp
            )
        }
    }
}