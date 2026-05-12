package com.optic.pramozventicoappz.presentation.screens.clients.abmcliente

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.presentation.screens.clients.ClientViewModel
import com.optic.pramozventicoappz.presentation.screens.clients.abmcliente.components.ClientDefaultField
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

private val PageBg = Color(0xFFF9FAFB)

private val avatarColors = listOf(
    Color(0xFFE91E63), Color(0xFFC2185B), Color(0xFF9C27B0),
    Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF009688),
    Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFFFF5722),
    Color(0xFF795548), Color(0xFF607D8B), Color(0xFFAD1457)
)

@Composable
fun ABMClienteContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    clientId: Int? = null,
    editable: Boolean = false,
    viewModel: ClientViewModel,
    errorMessage: String? = null
) {
    val scrollState = rememberScrollState()
    val primary     = MaterialTheme.colorScheme.primary
    val formState   by viewModel.formState.collectAsState()

    val initials = remember(formState.fullName) {
        val parts = formState.fullName.trim().split(" ").filter { it.isNotEmpty() }
        when {
            parts.isEmpty() -> "?"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else            -> "${parts.first().first().uppercaseChar()}${parts.last().first().uppercaseChar()}"
        }
    }
    val avatarColor = remember(formState.fullName) {
        if (formState.fullName.isBlank()) Color(0xFFCBD5E1)
        else avatarColors[formState.fullName.length % avatarColors.size]
    }
    val hasName = formState.fullName.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .padding(paddingValues)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 18.dp, bottom = 30.dp)
    ) {

        // ── Hero compacto con avatar dinámico ────────────────────────────────
        HeroHeader(
            isEdit      = editable,
            avatarColor = avatarColor,
            initials    = initials,
            hasName     = hasName
        )

        Spacer(Modifier.height(26.dp))

        // ── Información básica ──────────────────────────────────────────────
        SectionTitle("Información básica")

        FormSection {
            FieldLabel("Nombre completo", required = true)
            ClientDefaultField(
                value         = formState.fullName,
                onValueChange = { viewModel.onFormChange(formState.copy(fullName = it)) },
                placeholder   = "Ej. Juan Pérez",
                leadingIcon   = Icons.Outlined.Person
            )

            Spacer(Modifier.height(20.dp))

            FieldLabel("Correo electrónico", required = true)
            ClientDefaultField(
                value         = formState.email,
                onValueChange = { viewModel.onFormChange(formState.copy(email = it)) },
                placeholder   = "ejemplo@correo.com",
                keyboardType  = KeyboardType.Email,
                leadingIcon   = Icons.Outlined.Mail
            )

            Spacer(Modifier.height(20.dp))

            FieldLabel("Teléfono")
            ClientDefaultField(
                value         = formState.phone,
                onValueChange = { viewModel.onFormChange(formState.copy(phone = it)) },
                placeholder   = "Número de teléfono",
                keyboardType  = KeyboardType.Phone,
                prefix = {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier              = Modifier.padding(end = 4.dp)
                    ) {
                        Text("🇧🇴", fontSize = 16.sp)
                        Text(
                            "+591",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = TextPrimary
                        )
                        Box(modifier = Modifier.width(1.dp).height(16.dp).background(BorderGray))
                    }
                }
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Información adicional ───────────────────────────────────────────
        SectionTitle("Información adicional")

        FormSection {
            FieldLabel("Empresa")
            ClientDefaultField(
                value         = formState.enterprise,
                onValueChange = { viewModel.onFormChange(formState.copy(enterprise = it)) },
                placeholder   = "Empresa o negocio",
                leadingIcon   = Icons.Outlined.Business
            )

            Spacer(Modifier.height(20.dp))

            FieldLabel("País")
            ClientDefaultField(
                value         = formState.country,
                onValueChange = { viewModel.onFormChange(formState.copy(country = it)) },
                placeholder   = "País",
                leadingIcon   = Icons.Outlined.Language
            )

            Spacer(Modifier.height(20.dp))

            FieldLabel("Dirección")
            ClientDefaultField(
                value         = formState.address,
                onValueChange = { viewModel.onFormChange(formState.copy(address = it)) },
                placeholder   = "Dirección",
                leadingIcon   = Icons.Outlined.LocationOn
            )

            Spacer(Modifier.height(20.dp))

            FieldLabel("Ciudad")
            ClientDefaultField(
                value         = formState.city,
                onValueChange = { viewModel.onFormChange(formState.copy(city = it)) },
                placeholder   = "Ciudad",
                leadingIcon   = Icons.Outlined.LocationCity
            )

            Spacer(Modifier.height(20.dp))

            FieldLabel("Provincia / Estado")
            ClientDefaultField(
                value         = formState.state,
                onValueChange = { viewModel.onFormChange(formState.copy(state = it)) },
                placeholder   = "Provincia / Estado",
                leadingIcon   = Icons.Outlined.Map
            )
        }

        // ── Banner de error ─────────────────────────────────────────────────
        AnimatedVisibility(
            visible = errorMessage != null,
            enter   = fadeIn(),
            exit    = fadeOut()
        ) {
            Column {
                Spacer(Modifier.height(16.dp))
                ErrorBanner(message = errorMessage ?: "")
            }
        }
    }
}

// ─── Hero compacto ───────────────────────────────────────────────────────────
@Composable
private fun HeroHeader(
    isEdit: Boolean,
    avatarColor: Color,
    initials: String,
    hasName: Boolean
) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .shadow(
                    elevation    = if (hasName) 14.dp else 4.dp,
                    shape        = CircleShape,
                    ambientColor = avatarColor.copy(alpha = 0.25f),
                    spotColor    = avatarColor.copy(alpha = 0.40f)
                )
                .clip(CircleShape)
                .background(
                    if (hasName)
                        Brush.linearGradient(listOf(avatarColor, avatarColor.copy(alpha = 0.72f)))
                    else
                        Brush.linearGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState    = hasName,
                transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
                label          = "avatar"
            ) { entered ->
                if (entered) {
                    Text(
                        text          = initials,
                        fontSize      = 20.sp,
                        fontWeight    = FontWeight.Black,
                        color         = Color.White,
                        letterSpacing = (-0.3).sp
                    )
                } else {
                    Icon(
                        Icons.Outlined.Person, null,
                        tint     = Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text          = if (isEdit) "Editar cliente" else "Nuevo cliente",
                fontSize      = 22.sp,
                fontWeight    = FontWeight.ExtraBold,
                color         = TextPrimary,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text       = if (isEdit) "Actualiza los datos del cliente"
                else "Agrégalo a tu cartera de contactos",
                fontSize   = 13.sp,
                color      = TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

// ─── Banner de error ─────────────────────────────────────────────────────────
@Composable
private fun ErrorBanner(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFEF2F2))
            .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint     = Color(0xFFEF4444),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text       = message,
            fontSize   = 13.sp,
            color      = Color(0xFFB91C1C),
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            modifier   = Modifier.weight(1f)
        )
    }
}

// ─── Helpers de formulario ───────────────────────────────────────────────────
@Composable
private fun SectionTitle(text: String) {
    Text(
        text          = text,
        fontSize      = 13.sp,
        fontWeight    = FontWeight.Bold,
        color         = TextSecondary,
        letterSpacing = 0.2.sp,
        modifier      = Modifier.padding(bottom = 10.dp, start = 4.dp)
    )
}

@Composable
private fun FormSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 6.dp,
                shape        = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor    = Color.Black.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(1.dp, BorderGray.copy(alpha = 0.55f), RoundedCornerShape(22.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
        content = content
    )
}

@Composable
private fun FieldLabel(text: String, required: Boolean = false) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text       = text,
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color      = TextPrimary
        )
        if (required) {
            Text(
                text       = " *",
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary
            )
        }
    }
}