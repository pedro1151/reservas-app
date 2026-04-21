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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.business.createmember.components.MemberInputCard
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
@Composable
fun ABMBusinessMemberContent(
    paddingValues: PaddingValues,
    viewModel: BusinessViewModel,
    navController: NavHostController
) {

    val Cyan = Color(0xFF22C1C3)
    val CyanSoft = Cyan.copy(alpha = 0.12f)

    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF475569)
    val BorderSoft = Color(0xFFE2E8F0)

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<String?>(null) }

    val createState by viewModel.createMemberState

    val isLoading = createState is Resource.Loading

    val isEnabled =
        email.isNotBlank() &&
                username.isNotBlank() &&
                password.isNotBlank() &&
                selectedRole != null &&
                !isLoading

    val roles = listOf(
        "collaborator",
        "admin"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCoolBackground)
            .padding(paddingValues)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔹 EMAIL
            item {
               MemberInputCard (
                    label = "Email",
                    value = email,
                    onValueChange = { email = it }
                )
            }

            // 🔹 USERNAME
            item {
                MemberInputCard(
                    label = "Username",
                    value = username,
                    onValueChange = { username = it }
                )
            }

            // 🔹 PASSWORD
            item {
                MemberInputCard(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it }
                )
            }

            // 🔹 ROLES TITLE
            item {
                Text(
                    "Selecciona el rol",
                    color = TextSecondary
                )
            }

            // 🔹 ROLES GRID (igual que payment)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .shadow(
                            12.dp,
                            RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                            spotColor = Color.Black.copy(alpha = 0.08f)
                        )
                        .padding(16.dp)
                ) {

                    roles.chunked(2).forEach { rowRoles ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            rowRoles.forEach { role ->

                                val isSelected = selectedRole == role

                                val interaction = remember { MutableInteractionSource() }
                                val pressed by interaction.collectIsPressedAsState()

                                val scale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.05f else if (pressed) 0.97f else 1f,
                                    animationSpec = tween(180),
                                    label = ""
                                )

                                val checkScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1f else 0f,
                                    animationSpec = tween(220),
                                    label = ""
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            if (isSelected) CyanSoft else Color.Transparent
                                        )
                                        .border(
                                            if (isSelected) 1.4.dp else 1.dp,
                                            if (isSelected) CyanSoft else BorderSoft,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable(
                                            interactionSource = interaction,
                                            indication = rememberRipple(color = Cyan)
                                        ) {
                                            selectedRole = role
                                        }
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Box {

                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = if (isSelected) Cyan else TextSecondary
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .size(16.dp)
                                                    .graphicsLayer {
                                                        scaleX = checkScale
                                                        scaleY = checkScale
                                                    }
                                                    .background(Cyan, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(10.dp)
                                                )
                                            }
                                        }

                                        Spacer(Modifier.height(6.dp))

                                        Text(
                                            text = if (role == "collaborator") "Colaborador" else "Admin",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isSelected) Cyan else TextPrimary
                                        )
                                    }
                                }
                            }

                            if (rowRoles.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }

        // 🔥 BOTÓN CREAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    viewModel.createMember(
                        UserCollabCreateRequest(
                            email = email,
                            password = password,
                            username = username,
                            businessId = 1, // 🔥 luego dinámico
                            role = selectedRole!!
                        )
                    )
                },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Cyan,
                    disabledContainerColor = Cyan.copy(alpha = 0.4f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        "Crear miembro",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // 🔥 SUCCESS / ERROR HANDLING
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