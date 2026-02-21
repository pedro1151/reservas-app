package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.KeyboardArrowDown
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
fun ABMClienteContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    clientId: Int? = null,
    editable: Boolean = false,
    fullName: String = "",
    onFullNameChange: (String) -> Unit = {},
    phone: String = "",
    onPhoneChange: (String) -> Unit = {},
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    company: String = "",
    onCompanyChange: (String) -> Unit = {},
    country: String = "",
    onCountryChange: (String) -> Unit = {},
    address: String = "",
    onAddressChange: (String) -> Unit = {},
    city: String = "",
    onCityChange: (String) -> Unit = {},
    state: String = "",
    onStateChange: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        // ── Avatar circular con badge de cámara ──
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Círculo principal
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint = Color(0xFFBBBBBB),
                    modifier = Modifier.size(32.dp)
                )
            }
            // Badge pequeño con ícono cámara
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDDDDD))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Campos con fondo blanco y borde negro (COMO SERVICIOS) ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nombre completo
            WhiteFieldWithBorder(
                value = fullName,
                onValueChange = onFullNameChange,
                placeholder = "Nombre completo"
            )

            // Teléfono con bandera Bolivia
            WhiteFieldWithBorder(
                value = phone,
                onValueChange = onPhoneChange,
                placeholder = "Número de teléfono",
                keyboardType = KeyboardType.Phone,
                prefix = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text("🇧🇴", fontSize = 18.sp)
                        Text(
                            "+591",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                },
                suffix = {
                    Text(
                        "PRIMARY",
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            )

            // Correo electrónico
            WhiteFieldWithBorder(
                value = email,
                onValueChange = onEmailChange,
                placeholder = "Correo electrónico",
                keyboardType = KeyboardType.Email,
                suffix = {
                    Text(
                        "PRIMARY",
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            )

            // Empresa
            WhiteFieldWithBorder(
                value = company,
                onValueChange = onCompanyChange,
                placeholder = "Nombre de la empresa"
            )

            // País (dropdown visual)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (country.isBlank()) "País" else country,
                        fontSize = 15.sp,
                        color = if (country.isBlank()) Color(0xFFAAAAAA) else Color.Black
                    )
                    Icon(
                        Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFFAAAAAA),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Dirección
            WhiteFieldWithBorder(
                value = address,
                onValueChange = onAddressChange,
                placeholder = "Dirección"
            )

            // Ciudad
            WhiteFieldWithBorder(
                value = city,
                onValueChange = onCityChange,
                placeholder = "Ciudad"
            )

            // Estado / Provincia
            WhiteFieldWithBorder(
                value = state,
                onValueChange = onStateChange,
                placeholder = "Estado"
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun WhiteFieldWithBorder(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, fontSize = 15.sp, color = Color(0xFFAAAAAA))
        },
        prefix = prefix,
        suffix = suffix,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
    )
}
