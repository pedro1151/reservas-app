package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue600  = Color(0xFF2563EB)
private val Blue500  = Color(0xFF3B82F6)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)

@Composable
fun ClientDefaultField(
    value         : String,
    onValueChange : (String) -> Unit,
    placeholder   : String,
    keyboardType  : KeyboardType = KeyboardType.Text,
    leadingIcon   : ImageVector? = null,
    prefix        : @Composable (() -> Unit)? = null,
    suffix        : @Composable (() -> Unit)? = null,
    modifier      : Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue   = if (isFocused) Blue500.copy(alpha = 0.60f) else Slate200,
        animationSpec = tween(180),
        label         = "border"
    )
    val labelColor by animateColorAsState(
        targetValue   = if (isFocused) Blue600 else Slate400,
        animationSpec = tween(180),
        label         = "label"
    )
    val iconTint by animateColorAsState(
        targetValue   = if (isFocused) Blue600 else Slate400,
        animationSpec = tween(180),
        label         = "icon"
    )

    Column(
        modifier            = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // ── Floating label (appears when focused or has content) ──
        val showLabel = isFocused || value.isNotEmpty()
        if (showLabel) {
            Text(
                text          = placeholder,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.SemiBold,
                color         = labelColor,
                letterSpacing = 0.3.sp
            )
        }

        // ── Input box ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = if (isFocused) 4.dp else 0.dp,
                    shape        = RoundedCornerShape(14.dp),
                    ambientColor = Blue500.copy(alpha = 0.10f)
                )
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .border(
                    width = if (isFocused) 1.5.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Leading icon
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isFocused) Blue50 else Color(0xFFF8FAFC)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        leadingIcon, null,
                        tint     = iconTint,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Prefix (e.g. flag + country code)
            if (prefix != null) {
                prefix()
            }

            // Text input
            BasicTextField(
                value           = value,
                onValueChange   = onValueChange,
                modifier        = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle       = TextStyle(
                    fontSize   = 15.sp,
                    color      = Slate900,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush     = SolidColor(Blue600),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                decorationBox   = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text      = placeholder,
                                fontSize  = 15.sp,
                                color     = Slate400,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        inner()
                    }
                }
            )

            // Suffix (e.g. "PRIMARY" label)
            if (suffix != null) {
                suffix()
            }
        }
    }
}