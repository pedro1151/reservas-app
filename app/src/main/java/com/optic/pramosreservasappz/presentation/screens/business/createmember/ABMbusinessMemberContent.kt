package com.optic.pramosreservasappz.presentation.screens.business.createmember

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel

// ─── Design Tokens ────────────────────────────────────────────────────────────
private val Magenta700  = Color(0xFFAD1457)
private val Magenta600  = Color(0xFFD81B60)
private val Magenta500  = Color(0xFFE91E63)
private val Magenta50   = Color(0xFFFCE4EC)
private val MagentaSoft = Color(0xFFFFF0F5)
private val Slate900    = Color(0xFF0F172A)
private val Slate600    = Color(0xFF475569)
private val Slate400    = Color(0xFF94A3B8)
private val Slate200    = Color(0xFFE2E8F0)
private val PageBg      = Color(0xFFF8FAFC)

@Composable
fun ABMBusinessMemberContent(
    paddingValues : PaddingValues,
    viewModel     : BusinessViewModel,
    navController : NavHostController
) {
    var email        by remember { mutableStateOf("") }
    var username     by remember { mutableStateOf("") }
    var password     by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    val createState by viewModel.createMemberState
    val isLoading   = createState is Resource.Loading
    val isEnabled   =
        email.isNotBlank() &&
                username.isNotBlank() &&
                password.isNotBlank() &&
                selectedRole != null &&
                !isLoading

    val roles = listOf("collaborator", "admin")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier       = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Section header ──
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Información del miembro",
                    fontSize      = 12.sp,
                    fontWeight    = FontWeight.SemiBold,
                    color         = Slate400,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(10.dp))
            }

            // ── Fields card ──
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation    = 3.dp,
                            shape        = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                            spotColor    = Color.Black.copy(alpha = 0.07f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White
                ) {
                    Column {
                        // Email
                        MemberFormField(
                            icon          = Icons.Outlined.Email,
                            label         = "Email",
                            value         = email,
                            onValueChange = { email = it },
                            placeholder   = "nombre@empresa.com",
                            keyboardType  = KeyboardType.Email,
                            showDivider   = true
                        )
                        // Username
                        MemberFormField(
                            icon          = Icons.Outlined.Person,
                            label         = "Usuario",
                            value         = username,
                            onValueChange = { username = it },
                            placeholder   = "nombredeusuario",
                            showDivider   = true
                        )
                        // Password
                        MemberFormField(
                            icon              = Icons.Outlined.Lock,
                            label             = "Contraseña",
                            value             = password,
                            onValueChange     = { password = it },
                            placeholder       = "Mínimo 6 caracteres",
                            keyboardType      = KeyboardType.Password,
                            isPassword        = true,
                            showPassword      = showPassword,
                            onTogglePassword  = { showPassword = !showPassword },
                            showDivider       = false
                        )
                    }
                }
            }

            // ── Roles section header ──
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    "Rol del miembro",
                    fontSize      = 12.sp,
                    fontWeight    = FontWeight.SemiBold,
                    color         = Slate400,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(10.dp))
            }

            // ── Role selector ──
            item {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    roles.forEach { role ->
                        RoleCard(
                            role         = role,
                            isSelected   = selectedRole == role,
                            onSelect     = { selectedRole = role },
                            modifier     = Modifier.weight(1f)
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }

        // ── CTA Button ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = {
                    viewModel.createMember(
                        UserCollabCreateRequest(
                            email      = email,
                            password   = password,
                            username   = username,
                            businessId = 1,
                            role       = selectedRole!!
                        )
                    )
                },
                enabled  = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = Magenta600,
                    disabledContainerColor = Magenta600.copy(alpha = 0.38f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation  = 4.dp,
                    pressedElevation  = 8.dp,
                    disabledElevation = 0.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color       = Color.White,
                        strokeWidth = 2.dp,
                        modifier    = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        Icons.Outlined.PersonAdd, null,
                        tint     = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Crear miembro",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color.White
                    )
                }
            }
        }
    }

    // ── Success / Error handling ──
    LaunchedEffect(createState) {
        when (createState) {
            is Resource.Success -> {
                viewModel.resetCreateState()
                navController.popBackStack()
            }
            is Resource.Failure -> {
                viewModel.resetCreateState()
            }
            else -> {}
        }
    }
}

// ─── Form Field ───────────────────────────────────────────────────────────────
@Composable
private fun MemberFormField(
    icon             : ImageVector,
    label            : String,
    value            : String,
    onValueChange    : (String) -> Unit,
    placeholder      : String        = "",
    keyboardType     : KeyboardType  = KeyboardType.Text,
    isPassword       : Boolean       = false,
    showPassword     : Boolean       = false,
    onTogglePassword : (() -> Unit)? = null,
    showDivider      : Boolean       = true
) {
    val visualTrans = if (isPassword && !showPassword)
        PasswordVisualTransformation() else VisualTransformation.None

    Column {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Magenta50),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, null,
                    tint     = Magenta600,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    label,
                    fontSize      = 11.sp,
                    fontWeight    = FontWeight.SemiBold,
                    color         = Slate400,
                    letterSpacing = 0.3.sp
                )
                Spacer(Modifier.height(3.dp))
                OutlinedTextField(
                    value         = value,
                    onValueChange = onValueChange,
                    modifier      = Modifier.fillMaxWidth(),
                    placeholder   = {
                        Text(placeholder, fontSize = 14.sp, color = Slate400.copy(alpha = 0.6f))
                    },
                    singleLine              = true,
                    visualTransformation    = visualTrans,
                    keyboardOptions         = KeyboardOptions(keyboardType = keyboardType),
                    trailingIcon            = if (isPassword && onTogglePassword != null) ({
                        IconButton(onClick = onTogglePassword) {
                            Icon(
                                if (showPassword) Icons.Outlined.VisibilityOff
                                else Icons.Outlined.Visibility,
                                null,
                                tint     = Slate400,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }) else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Magenta500.copy(alpha = 0.60f),
                        unfocusedBorderColor = Slate200,
                        focusedTextColor     = Slate900,
                        unfocusedTextColor   = Slate900,
                        cursorColor          = Magenta600,
                        focusedContainerColor   = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape     = RoundedCornerShape(10.dp),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                modifier  = Modifier.padding(start = 64.dp),
                thickness = 0.8.dp,
                color     = Slate200
            )
        }
    }
}

// ─── Role Card ────────────────────────────────────────────────────────────────
@Composable
private fun RoleCard(
    role       : String,
    isSelected : Boolean,
    onSelect   : () -> Unit,
    modifier   : Modifier = Modifier
) {
    val interaction  = remember { MutableInteractionSource() }
    val pressed      by interaction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue  = if (isSelected) 1.03f else if (pressed) 0.97f else 1f,
        animationSpec = tween(180),
        label         = "scale"
    )

    val icon = if (role == "collaborator") Icons.Outlined.Groups else Icons.Outlined.AdminPanelSettings
    val label = if (role == "collaborator") "Colaborador" else "Admin"
    val description = if (role == "collaborator") "Acceso básico" else "Acceso total"

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(
                elevation    = if (isSelected) 6.dp else 1.dp,
                shape        = RoundedCornerShape(18.dp),
                ambientColor = if (isSelected) Magenta600.copy(alpha = 0.20f) else Color.Black.copy(alpha = 0.04f),
                spotColor    = if (isSelected) Magenta600.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) Magenta600 else Color.White)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = Slate200,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = interaction,
                indication        = rememberRipple(
                    color = if (isSelected) Color.White else Magenta500
                )
            ) { onSelect() }
            .padding(vertical = 20.dp, horizontal = 12.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color.White.copy(alpha = 0.22f)
                        else Magenta50
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, null,
                    tint     = if (isSelected) Color.White else Magenta600,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text       = label,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = if (isSelected) Color.White else Slate900
            )
            Text(
                text     = description,
                fontSize = 11.sp,
                color    = if (isSelected) Color.White.copy(alpha = 0.75f) else Slate400
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check, null,
                        tint     = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}