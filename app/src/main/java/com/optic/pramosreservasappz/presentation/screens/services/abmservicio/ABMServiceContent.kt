package com.optic.pramosreservasappz.presentation.screens.services.abmservicio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel
import com.optic.pramosreservasappz.presentation.screens.services.abmservicio.components.ServiceOutlinedField

@Composable
fun ABMServiceContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    serviceId: Int? = null,
    editable: Boolean = false,
    viewModel: ServiceViewModel
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val formState by viewModel.formState.collectAsState()
    var isButtonEnabled by remember { mutableStateOf(true) }
    val serviceResource by viewModel.serviceState.collectAsState()

    val createState by viewModel.createServiceState.collectAsState()
    val updateState by viewModel.updateServiceState.collectAsState()

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {
            viewModel.resetCreateState()
            viewModel.resetForm()
            navController.popBackStack()
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is Resource.Success) {
            viewModel.resetUpdateState()
            navController.popBackStack()
        }
    }

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
                value =  formState.name,
                onValueChange =  { viewModel.onFormChange(formState.copy(name = it)) },
                placeholder = "Ejemplo: Servicio de consultoria"
            )
        }

        // Descripción
        LabeledField(label = "Descripción") {
            ServiceOutlinedField(
                value = formState.description,
                onValueChange =  { viewModel.onFormChange(formState.copy(description = it)) },
                placeholder = "Describe tu servicio a los visitantes de la página de reservas",
                singleLine = false,
                keyboardType = KeyboardType.Text,
                minLines = 4,
                maxLines = 6,
                modifier = Modifier.heightIn(min = 100.dp)
            )
        }

        // Duración
        LabeledField(label = "Duración", required = true) {
            ServiceOutlinedField(
                value = formState.durationMinutes,
                onValueChange =  { viewModel.onFormChange(formState.copy(durationMinutes = it)) },
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

                value = formState.bufferTime,
                onValueChange =  { viewModel.onFormChange(formState.copy(bufferTime = it)) },
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
                value = formState.price,
                onValueChange = { viewModel.onFormChange(formState.copy(price = it)) },
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
            ServiceOutlinedField(
                value = formState.category,
                onValueChange = { viewModel.onFormChange(formState.copy(category = it)) },
                placeholder = "Selecciona una o más categorías"
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
                    checked = formState.hidden,
                    onCheckedChange =   { viewModel.onFormChange(formState.copy(hidden = it)) },
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
