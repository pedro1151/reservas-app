package com.optic.pramozventicoappz.presentation.screens.clients.abmcliente

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.presentation.screens.clients.ClientViewModel
import com.optic.pramozventicoappz.presentation.screens.clients.abmcliente.components.ClientDefaultField

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Pink700  = Color(0xFFC2185B)
private val Pink600  = Color(0xFFE91E63)
private val Pink500  = Color(0xFFEC407A)
private val Pink400  = Color(0xFFF06292)
private val Pink50   = Color(0xFFFFF0F3)
private val Slate900 = Color(0xFF0F172A)
private val Slate500 = Color(0xFF64748B)
private val Slate400 = Color(0xFF94A3B8)
private val Slate300 = Color(0xFFCBD5E1)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate100 = Color(0xFFF1F5F9)
private val PageBg   = Color(0xFFF8F4F6)

private val avatarColors = listOf(
    Color(0xFFE91E63), Color(0xFFC2185B), Color(0xFF9C27B0),
    Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF009688),
    Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFFFF5722),
    Color(0xFF795548), Color(0xFF607D8B), Color(0xFFAD1457)
)

@Composable
fun ABMClienteContent(
    paddingValues : PaddingValues,
    navController : NavHostController,
    clientId      : Int? = null,
    editable      : Boolean = false,
    viewModel     : ClientViewModel
) {
    val scrollState = rememberScrollState()
    val formState   by viewModel.formState.collectAsState()

    val initials = remember(formState.fullName) {
        val parts = formState.fullName.trim().split(" ").filter { it.isNotEmpty() }
        when {
            parts.isEmpty() -> ""
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
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ── Hero card blanca ────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Color.White)
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            // Strip de gradiente rosa
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Pink500.copy(alpha = 0f), Pink600, Pink700, Pink600, Pink500.copy(alpha = 0f))
                        )
                    )
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Avatar ──────────────────────────────────────────────────────
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .shadow(
                                elevation    = if (hasName) 10.dp else 2.dp,
                                shape        = CircleShape,
                                ambientColor = avatarColor.copy(alpha = 0.28f),
                                spotColor    = avatarColor.copy(alpha = 0.36f)
                            )
                            .clip(CircleShape)
                            .background(
                                if (hasName)
                                    Brush.linearGradient(listOf(avatarColor, avatarColor.copy(0.72f)))
                                else
                                    Brush.linearGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState  = hasName,
                            transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
                            label        = "avatar"
                        ) { entered ->
                            if (entered) {
                                Text(initials, fontSize = 30.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = (-0.5).sp)
                            } else {
                                Icon(Icons.Outlined.Person, null, tint = Color.White.copy(0.55f), modifier = Modifier.size(36.dp))
                            }
                        }
                    }
                    // Camera badge
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.5.dp, Pink400.copy(alpha = 0.30f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.CameraAlt, null, tint = Pink600, modifier = Modifier.size(13.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Nombre animado o hint
                AnimatedContent(
                    targetState  = hasName,
                    transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(180)) },
                    label        = "nameHint"
                ) { entered ->
                    if (entered) {
                        Text(
                            formState.fullName,
                            fontSize      = 19.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = Slate900,
                            letterSpacing = (-0.4).sp,
                            textAlign     = TextAlign.Center,
                            modifier      = Modifier.padding(horizontal = 32.dp)
                        )
                    } else {
                        Text(
                            "Escribe el nombre para ver el avatar",
                            fontSize  = 12.sp,
                            color     = Slate400,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── DATOS OBLIGATORIOS ──────────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Etiqueta de sección obligatoria
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            ) {
                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Pink600))
                Text(
                    "DATOS OBLIGATORIOS",
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = Pink600.copy(alpha = 0.85f),
                    letterSpacing = 1.4.sp
                )
            }

            // Card obligatoria — borde rosa más pronunciado, fondo levemente rosado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp), ambientColor = Pink600.copy(0.08f))
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .border(1.5.dp, Pink400.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Nombre completo
                ClientDefaultField(
                    value         = formState.fullName,
                    onValueChange = { viewModel.onFormChange(formState.copy(fullName = it)) },
                    placeholder   = "Nombre completo",
                    leadingIcon   = Icons.Outlined.Person
                )

                SectionDivider()

                // Email
                ClientDefaultField(
                    value         = formState.email,
                    onValueChange = { viewModel.onFormChange(formState.copy(email = it)) },
                    placeholder   = "Correo electrónico",
                    keyboardType  = KeyboardType.Email,
                    leadingIcon   = Icons.Outlined.Mail
                )
            }

            // Nota de ayuda
            Text(
                "Nombre y correo son requeridos para crear el cliente.",
                fontSize  = 11.sp,
                color     = Slate400,
                modifier  = Modifier.padding(start = 6.dp, top = 4.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── DATOS OPCIONALES ────────────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Etiqueta opcional
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Slate300))
                Text(
                    "INFORMACIÓN OPCIONAL",
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = Slate400,
                    letterSpacing = 1.4.sp
                )
            }

            // Teléfono (card opcional — borde slate, sin énfasis)
            OptionalSection {
                ClientDefaultField(
                    value         = formState.phone,
                    onValueChange = { viewModel.onFormChange(formState.copy(phone = it)) },
                    placeholder   = "Número de teléfono",
                    keyboardType  = KeyboardType.Phone,
                    prefix        = {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier              = Modifier.padding(end = 4.dp)
                        ) {
                            Text("🇧🇴", fontSize = 16.sp)
                            Text("+591", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                            Box(modifier = Modifier.width(1.dp).height(16.dp).background(Slate200))
                        }
                    }
                )
            }

            // Empresa
            OptionalSection {
                ClientDefaultField(
                    value         = formState.enterprise,
                    onValueChange = { viewModel.onFormChange(formState.copy(enterprise = it)) },
                    placeholder   = "Empresa",
                    leadingIcon   = Icons.Outlined.Business
                )
            }

            // Ubicación — todo junto en una sola card
            OptionalSection {
                ClientDefaultField(
                    value         = formState.country,
                    onValueChange = { viewModel.onFormChange(formState.copy(country = it)) },
                    placeholder   = "País",
                    leadingIcon   = Icons.Outlined.Language
                )
                SectionDivider()
                ClientDefaultField(
                    value         = formState.address,
                    onValueChange = { viewModel.onFormChange(formState.copy(address = it)) },
                    placeholder   = "Dirección",
                    leadingIcon   = Icons.Outlined.LocationOn
                )
                SectionDivider()
                ClientDefaultField(
                    value         = formState.city,
                    onValueChange = { viewModel.onFormChange(formState.copy(city = it)) },
                    placeholder   = "Ciudad",
                    leadingIcon   = Icons.Outlined.LocationCity
                )
                SectionDivider()
                ClientDefaultField(
                    value         = formState.state,
                    onValueChange = { viewModel.onFormChange(formState.copy(state = it)) },
                    placeholder   = "Estado / Provincia",
                    leadingIcon   = Icons.Outlined.Map
                )
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

// ─── Card para campos opcionales ─────────────────────────────────────────────────
// Borde slate suave, sin énfasis rosa — visualmente secundaria vs la obligatoria
@Composable
private fun OptionalSection(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black.copy(0.03f))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Slate200, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content             = content
    )
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(color = Slate200, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 4.dp))
}