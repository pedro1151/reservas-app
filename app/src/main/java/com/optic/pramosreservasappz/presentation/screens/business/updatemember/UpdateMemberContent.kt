package com.optic.pramosreservasappz.presentation.screens.business.updatemember

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.screens.business.createmember.components.MemberInputCard
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground

@Composable
fun UpdateMemberContent(
    paddingValues: PaddingValues,
    viewModel: BusinessViewModel,
    navController: NavHostController,
    userId: Int
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
    var isActive by remember { mutableStateOf(true) }
    var businessMemberId by remember { mutableStateOf(0) }
    var loaded by remember { mutableStateOf(false) }

    val memberState by viewModel.oneMemberState
    val updateState by viewModel.updateMemberState

    // LOAD INITIAL
    LaunchedEffect(Unit) {
        viewModel.getMember(userId)
    }

    // FILL FORM
    LaunchedEffect(memberState) {
        if (memberState is Resource.Success && !loaded) {

            val data = (memberState as Resource.Success).data

            email = data.user.email
            username = data.user.username
            selectedRole = data.businessMember.role
            isActive = data.businessMember.isActive
            businessMemberId = data.businessMember.id

            loaded = true
        }
    }

    val isLoading = updateState is Resource.Loading

    val isEnabled =
        email.isNotBlank() &&
                username.isNotBlank() &&
                selectedRole != null &&
                !isLoading

    val roles = listOf(
        "collaborator",
        "admin",
        "owner"
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

            item {
                MemberInputCard(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it }
                )
            }

            item {
                MemberInputCard(
                    label = "Nombre completo",
                    value = username,
                    onValueChange = { username = it }
                )
            }

            item {
                MemberInputCard(
                    label = "Nuevo password (opcional)",
                    value = password,
                    onValueChange = { password = it }
                )
            }

            // ACTIVE SWITCH
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Estado",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isActive) "ACTIVO" else "BAJA",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Switch(
                        checked = isActive,
                        onCheckedChange = {
                            isActive = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Cyan
                        )
                    )
                }
            }

            item {
                Text(
                    "Selecciona el rol",
                    color = TextSecondary
                )
            }

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {

                    roles.chunked(2).forEach { rowRoles ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            rowRoles.forEach { role ->

                                val isSelected = selectedRole == role

                                val interaction =
                                    remember { MutableInteractionSource() }

                                val pressed by interaction.collectIsPressedAsState()

                                val scale by animateFloatAsState(
                                    targetValue = when {
                                        isSelected -> 1.04f
                                        pressed -> 0.97f
                                        else -> 1f
                                    },
                                    animationSpec = tween(180),
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
                                            if (isSelected) CyanSoft
                                            else Color.Transparent
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) Cyan
                                            else BorderSoft,
                                            shape = RoundedCornerShape(14.dp)
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

                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .size(16.dp)
                                                        .background(
                                                            Cyan,
                                                            CircleShape
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(10.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = if (role == "collaborator")
                                                "Vendedor"
                                            else
                                                "Administrador",
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

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // BUTTON UPDATE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Button(
                onClick = {

                    viewModel.updateMember(
                        UserCollabUpdateRequest(
                            userId = userId,
                            email = email,
                            username = username,
                            password = password,
                            businessMemberId = businessMemberId,
                            isActive = isActive,
                            updatedBy = "system",
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
                    disabledContainerColor = Cyan.copy(alpha = 0.40f)
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
                        text = "Guardar cambios",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    // SUCCESS
    LaunchedEffect(updateState) {
        when (updateState) {

            is Resource.Success -> {
                navController.navigate(ClientScreen.BusinessMembers.route)
            }

            is Resource.Failure -> {
                // aquí luego snackbar si deseas
            }

            else -> {}
        }
    }
}