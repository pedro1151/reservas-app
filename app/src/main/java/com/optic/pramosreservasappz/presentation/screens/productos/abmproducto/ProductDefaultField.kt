package com.optic.pramosreservasappz.presentation.screens.productos.abmproducto


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

@Composable
fun ProductDefaultField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null
) {

    val shape = RoundedCornerShape(14.dp)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape)
            .border(1.dp, BorderGray, shape),
        placeholder = {
            Text(
                text = placeholder,
                color = TextSecondary.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        },
        textStyle = LocalTextStyle.current.copy(
            color = TextPrimary,
            fontSize = 15.sp
        ),
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        shape = shape,
        prefix = prefix,
        suffix = suffix,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = BorderGray,
            disabledBorderColor = BorderGray,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}