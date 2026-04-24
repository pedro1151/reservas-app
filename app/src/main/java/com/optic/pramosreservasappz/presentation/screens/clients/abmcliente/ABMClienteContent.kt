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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components.ClientDefaultField

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue700  = Color(0xFF1D4ED8)
private val Blue600  = Color(0xFF2563EB)
private val Blue500  = Color(0xFF3B82F6)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate600 = Color(0xFF475569)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val PageBg   = Color(0xFFF8FAFC)

// Avatar color palette (matches ClientCard)
private val avatarColors = listOf(
    Color(0xFF1D4ED8), Color(0xFF2563EB), Color(0xFF7C3AED),
    Color(0xFF059669), Color(0xFFDC2626), Color(0xFFD97706),
    Color(0xFF0891B2), Color(0xFFDB2777), Color(0xFF65A30D),
    Color(0xFF9333EA), Color(0xFF0F766E), Color(0xFFEA580C)
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

    // Dynamic avatar from name being typed
    val initials = remember(formState.fullName) {
        val parts = formState.fullName.trim().split(" ").filter { it.isNotEmpty() }
        when {
            parts.isEmpty()  -> ""
            parts.size == 1  -> parts[0].take(2).uppercase()
            else             -> "${parts.first().first().uppercaseChar()}${parts.last().first().uppercaseChar()}"
        }
    }
    val avatarColor = remember(formState.fullName) {
        if (formState.fullName.isBlank()) Blue600
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
        Spacer(Modifier.height(24.dp))

        // ── Dynamic Avatar ──────────────────────────────────────────────────────
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .shadow(
                        elevation    = if (hasName) 10.dp else 2.dp,
                        shape        = CircleShape,
                        ambientColor = avatarColor.copy(alpha = 0.25f),
                        spotColor    = avatarColor.copy(alpha = 0.35f)
                    )
                    .clip(CircleShape)
                    .background(
                        if (hasName)
                            Brush.linearGradient(listOf(avatarColor, avatarColor.copy(alpha = 0.65f)))
                        else
                            Brush.linearGradient(listOf(Slate200, Color(0xFFCBD5E1)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState  = hasName,
                    transitionSpec = {
                        fadeIn(tween(220)) togetherWith fadeOut(tween(160))
                    },
                    label = "avatar"
                ) { nameEntered ->
                    if (nameEntered) {
                        Text(
                            text          = initials,
                            fontSize      = 28.sp,
                            fontWeight    = FontWeight.Black,
                            color         = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Person, null,
                            tint     = Color.White.copy(alpha = 0.70f),
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }

            // Camera badge
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.5.dp, Slate200, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.CameraAlt, null,
                    tint     = Slate600,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        // Hint below avatar
        Spacer(Modifier.height(10.dp))
        AnimatedVisibility(
            visible = !hasName,
            enter   = fadeIn(tween(250)),
            exit    = fadeOut(tween(180))
        ) {
            Text(
                "Escribe el nombre para ver el avatar",
                fontSize  = 11.sp,
                color     = Slate400,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── Form sections ────────────────────────────────────────────────────────
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Section: Identidad ──
            FormSection(title = "Identidad") {
                ClientDefaultField(
                    value         = formState.fullName,
                    onValueChange = { viewModel.onFormChange(formState.copy(fullName = it)) },
                    placeholder   = "Nombre completo",
                    leadingIcon   = Icons.Outlined.Person
                )
            }

            // ── Section: Contacto ──
            FormSection(title = "Contacto") {
                // Teléfono (keeps existing flag prefix)
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
                            Text(
                                "+591",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = Slate900
                            )
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(16.dp)
                                    .background(Slate200)
                            )
                        }
                    }
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

            // ── Section: Empresa ──
            FormSection(title = "Empresa") {
                ClientDefaultField(
                    value         = formState.enterprise,
                    onValueChange = { viewModel.onFormChange(formState.copy(enterprise = it)) },
                    placeholder   = "Nombre de la empresa",
                    leadingIcon   = Icons.Outlined.Business
                )
            }

            // ── Section: Ubicación ──
            FormSection(title = "Ubicación") {
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

        Spacer(Modifier.height(32.dp))
    }
}

// ─── Section card container ──────────────────────────────────────────────────────
@Composable
private fun FormSection(
    title   : String,
    content : @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Section label
        Text(
            text          = title.uppercase(),
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            color         = Slate400,
            letterSpacing = 1.2.sp,
            modifier      = Modifier.padding(start = 4.dp)
        )

        // White card containing all fields in this section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = 2.dp,
                    shape        = RoundedCornerShape(18.dp),
                    ambientColor = Color.Black.copy(alpha = 0.04f)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .border(1.dp, Slate200, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content             = content
        )
    }
}

// ─── Hairline divider between fields in a section ────────────────────────────────
@Composable
private fun SectionDivider() {
    HorizontalDivider(
        color     = Slate200,
        thickness = 0.5.dp,
        modifier  = Modifier.padding(horizontal = 4.dp)
    )
}