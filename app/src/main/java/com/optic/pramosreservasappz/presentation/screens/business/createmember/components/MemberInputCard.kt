package com.optic.pramosreservasappz.presentation.screens.business.createmember.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MemberInputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val Cyan = Color(0xFF22C1C3)
    val BorderSoft = Color(0xFFE2E8F0)
    val TextSecondary = Color(0xFF475569)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .shadow(
                12.dp,
                RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .padding(16.dp)
    ) {

        Text(label, color = TextSecondary)

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Cyan,
                unfocusedBorderColor = BorderSoft,
                cursorColor = Cyan
            )
        )
    }
}