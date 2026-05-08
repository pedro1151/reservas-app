package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

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
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components.ClientDefaultField

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Pink700  = Color(0xFFC2185B)
private val Pink600  = Color(0xFFE91E63)
private val Pink500  = Color(0xFFEC407A)
private val Pink400  = Color(0xFFF06292)
private val Pink50   = Color(0xFFFFF0F3)
private val Slate900 = Color(0xFF0F172A)
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
            modifier         = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Color.White)
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            // Franja rosa superior
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
                modifier            = Modifier.fillMaxWidth().padding(top = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(
                            if (hasName)
                                Brush.radialGradient(listOf(avatarColor.copy(alpha = 0.14f), Color.Transparent))
                            else
                                Brush.radialGradient(listOf(Color.Transparent, Color.Transparent))
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .shadow(
                                elevation    = if (hasName) 10.dp else 2.dp,
                                shape        = CircleShape,
                                ambientColor = avatarColor.copy(alpha = 0.26f),
                                spotColor    = avatarColor.copy(alpha = 0.34f)
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
                            targetState  = hasName,
                            transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
                            label        = "avatar"
                        ) { entered ->
                            if (entered) {
                                Text(
                                    text          = initials,
                                    fontSize      = 30.sp,
                                    fontWeight    = FontWeight.Black,
                                    color         = Color.White,
                                    letterSpacing = (-0.5).sp
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.Person, null,
                                    tint     = Color.White.copy(alpha = 0.55f),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                    // Camera badge
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.5.dp, Pink400.copy(alpha = 0.32f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.CameraAlt, null, tint = Pink600, modifier = Modifier.size(13.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Nombre o hint
                AnimatedContent(
                    targetState  = hasName,
                    transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(180)) },
                    label        = "nameHint"
                ) { entered ->
                    if (entered) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text          = formState.fullName,
                                fontSize      = 19.sp,
                                fontWeight    = FontWeight.Bold,
                                color         = Slate900,
                                letterSpacing = (-0.4).sp,
                                textAlign     = TextAlign.Center,
                                modifier      = Modifier.padding(horizontal = 32.dp)
                            )
                            Spacer(Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(Pink50, RoundedCornerShape(8.dp))
                                    .border(1.dp, Pink400.copy(alpha = 0.22f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    if (editable) "Editando perfil" else "Nuevo cliente",
                                    fontSize      = 11.sp,
                                    color         = Pink600,
                                    fontWeight    = FontWeight.SemiBold,
                                    letterSpacing = 0.2.sp
                                )
                            }
                        }
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

        Spacer(Modifier.height(20.dp))

        Column(
            modifier            = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ══════════════════════════════════════════════════════════════════
            // DATOS OBLIGATORIOS
            // ══════════════════════════════════════════════════════════════════
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // Encabezado sección obligatoria
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier              = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Pink600))
                        Text(
                            "DATOS PRINCIPALES",
                            fontSize      = 10.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = Pink600.copy(alpha = 0.80f),
                            letterSpacing = 1.4.sp
                        )
                    }
                    // Etiqueta "Obligatorio"
                    Box(
                        modifier = Modifier
                            .background(Pink50, RoundedCornerShape(6.dp))
                            .border(1.dp, Pink400.copy(alpha = 0.20f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "Obligatorio",
                            fontSize      = 9.sp,
                            color         = Pink600,
                            fontWeight    = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                // Card obligatoria — borde rosa más pronunciado, mayor padding
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(18.dp), ambientColor = Pink600.copy(alpha = 0.08f), spotColor = Pink600.copy(alpha = 0.12f))
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .border(1.5.dp, Pink400.copy(alpha = 0.28f), RoundedCornerShape(18.dp))
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Nombre completo — campo principal
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Nombre completo",
                                fontSize      = 11.sp,
                                fontWeight    = FontWeight.SemiBold,
                                color         = Slate900,
                                letterSpacing = 0.1.sp
                            )
                            Text("*", fontSize = 12.sp, color = Pink600, fontWeight = FontWeight.Bold)
                        }
                        ClientDefaultField(
                            value         = formState.fullName,
                            onValueChange = { viewModel.onFormChange(formState.copy(fullName = it)) },
                            placeholder   = "Ej. Juan Pérez",
                            leadingIcon   = Icons.Outlined.Person
                        )
                    }

                    HorizontalDivider(color = Slate200, thickness = 0.5.dp)

                    // Correo electrónico — campo principal
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Correo electrónico",
                                fontSize      = 11.sp,
                                fontWeight    = FontWeight.SemiBold,
                                color         = Slate900,
                                letterSpacing = 0.1.sp
                            )
                            Text("*", fontSize = 12.sp, color = Pink600, fontWeight = FontWeight.Bold)
                        }
                        ClientDefaultField(
                            value         = formState.email,
                            onValueChange = { viewModel.onFormChange(formState.copy(email = it)) },
                            placeholder   = "ejemplo@correo.com",
                            keyboardType  = KeyboardType.Email,
                            leadingIcon   = Icons.Outlined.Mail
                        )
                    }
                }
            }

            // ══════════════════════════════════════════════════════════════════
            // DATOS OPCIONALES
            // ══════════════════════════════════════════════════════════════════
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // Encabezado sección opcional
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier              = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Slate300))
                        Text(
                            "INFORMACIÓN ADICIONAL",
                            fontSize      = 10.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = Slate400,
                            letterSpacing = 1.4.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Slate100, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "Opcional",
                            fontSize      = 9.sp,
                            color         = Slate400,
                            fontWeight    = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                // Card opcional — estilo más apagado, borde slate ligero
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black.copy(alpha = 0.03f))
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .border(1.dp, Slate200, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Teléfono
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

                    HorizontalDivider(color = Slate200, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 4.dp))

                    // Empresa
                    ClientDefaultField(
                        value         = formState.enterprise,
                        onValueChange = { viewModel.onFormChange(formState.copy(enterprise = it)) },
                        placeholder   = "Empresa",
                        leadingIcon   = Icons.Outlined.Business
                    )

                    HorizontalDivider(color = Slate200, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 4.dp))

                    // País
                    ClientDefaultField(
                        value         = formState.country,
                        onValueChange = { viewModel.onFormChange(formState.copy(country = it)) },
                        placeholder   = "País",
                        leadingIcon   = Icons.Outlined.Language
                    )

                    HorizontalDivider(color = Slate200, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 4.dp))

                    // Dirección
                    ClientDefaultField(
                        value         = formState.address,
                        onValueChange = { viewModel.onFormChange(formState.copy(address = it)) },
                        placeholder   = "Dirección",
                        leadingIcon   = Icons.Outlined.LocationOn
                    )

                    HorizontalDivider(color = Slate200, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 4.dp))

                    // Ciudad
                    ClientDefaultField(
                        value         = formState.city,
                        onValueChange = { viewModel.onFormChange(formState.copy(city = it)) },
                        placeholder   = "Ciudad",
                        leadingIcon   = Icons.Outlined.LocationCity
                    )

                    HorizontalDivider(color = Slate200, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 4.dp))

                    // Estado / Provincia
                    ClientDefaultField(
                        value         = formState.state,
                        onValueChange = { viewModel.onFormChange(formState.copy(state = it)) },
                        placeholder   = "Estado / Provincia",
                        leadingIcon   = Icons.Outlined.Map
                    )
                }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}
