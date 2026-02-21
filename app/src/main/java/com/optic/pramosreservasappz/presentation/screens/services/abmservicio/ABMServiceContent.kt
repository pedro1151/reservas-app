package com.optic.pramosreservasappz.presentation.screens.services.abmservicio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ABMServiceContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    serviceId: Int? = null,
    editable: Boolean = false,
    title: String = "",
    onTitleChange: (String) -> Unit = {},
    description: String = "",
    onDescriptionChange: (String) -> Unit = {},
    duration: String = "",
    onDurationChange: (String) -> Unit = {},
    bufferTime: String = "",
    onBufferTimeChange: (String) -> Unit = {},
    price: String = "",
    onPriceChange: (String) -> Unit = {},
    category: String = "",
    onCategoryChange: (String) -> Unit = {},
    isHidden: Boolean = false,
    onHiddenChange: (Boolean) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .verticalScroll(scrollState)
    ) {
        Spacer(Modifier.height(4.dp))

        // Sección "Detalles del servicio"
        SectionLabel("Detalles del servicio")

        // Título
        LabeledField(label = "Título", required = true) {
            ServiceOutlinedField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = "Título del servicio"
            )
        }

        // Descripción
        LabeledField(label = "Descripción") {
            ServiceOutlinedField(
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = "Describe tu servicio a los visitantes de la página de reservas",
                singleLine = false,
                minLines = 4,
                maxLines = 6,
                modifier = Modifier.heightIn(min = 100.dp)
            )
        }

        // Duración
        LabeledField(label = "Duración", required = true) {
            ServiceOutlinedField(
                value = duration,
                onValueChange = onDurationChange,
                placeholder = "",
                keyboardType = KeyboardType.Number,
                suffix = {
                    Text("min", fontSize = 14.sp, color = Color(0xFF888888))
                }
            )
        }

        // Tiempo de buffer
        LabeledField(
            label = "Tiempo de buffer",
            infoIcon = true
        ) {
            ServiceOutlinedField(
                value = bufferTime,
                onValueChange = onBufferTimeChange,
                placeholder = "0",
                keyboardType = KeyboardType.Number,
                suffix = {
                    Text("min", fontSize = 14.sp, color = Color(0xFF888888))
                }
            )
        }

        // Coste
        LabeledField(label = "Coste") {
            ServiceOutlinedField(
                value = price,
                onValueChange = onPriceChange,
                placeholder = "0",
                keyboardType = KeyboardType.Decimal,
                prefix = {
                    Text("Bs. ", fontSize = 14.sp, color = Color(0xFF555555), fontWeight = FontWeight.Medium)
                }
            )
        }

        Spacer(Modifier.height(4.dp))

        // Equipo (dropdown)
        LabeledField(label = "Equipo", required = true) {
            DropdownField(
                value = "Jonathan Ticona Pérez",
                onClick = { /* TODO */ }
            )
        }

        // Ubicación (dropdown)
        LabeledField(label = "Ubicación") {
            DropdownField(
                value = "",
                placeholder = "Seleccionar ubicación",
                onClick = { /* TODO */ }
            )
        }

        // Categoría (dropdown)
        LabeledField(label = "Categoría") {
            DropdownField(
                value = category,
                placeholder = "Selecciona una o más categorías",
                onClick = { /* TODO */ }
            )
        }

        // Establecer como oculto
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Establecer como oculto",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Cuando se configura como oculto, un servicio no está visible en tu página de reservas.",
                        fontSize = 12.sp,
                        color = Color(0xFF888888),
                        lineHeight = 17.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Switch(
                    checked = isHidden,
                    onCheckedChange = onHiddenChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Black,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCCCCCC)
                    )
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
private fun LabeledField(
    label: String,
    required: Boolean = false,
    infoIcon: Boolean = false,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            if (required) {
                Text(" *", fontSize = 14.sp, color = Color.Black)
            }
            if (infoIcon) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.Info,
                    null,
                    tint = Color(0xFFBBBBBB),
                    modifier = Modifier.size(15.dp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        content()
    }
}

@Composable
private fun ServiceOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, fontSize = 14.sp, color = Color(0xFFCCCCCC))
        },
        prefix = prefix,
        suffix = suffix,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFBBBBBB),
            unfocusedBorderColor = Color(0xFFDDDDDD),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun DropdownField(
    value: String,
    placeholder: String = "",
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(0.5.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value.isNotBlank()) {
                // Chip con el valor seleccionado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF0F0F0))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(value, fontSize = 13.sp, color = Color(0xFF333333))
                }
            } else {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
            Icon(
                Icons.Outlined.KeyboardArrowDown,
                null,
                tint = Color(0xFFAAAAAA),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
