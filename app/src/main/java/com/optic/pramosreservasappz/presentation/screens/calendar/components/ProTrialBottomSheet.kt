package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProTrialBottomSheet(
    onDismiss: () -> Unit
) {
    var selectedPlan by remember { mutableStateOf("annual") }
    var selectedUsers by remember { mutableStateOf("2-4") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header con X
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Título
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Comienza tu prueba gratuita de 14 días",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Obtén acceso a funciones exclusivas de Pro.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF757575),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(24.dp))

            // Tabs de usuarios
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                UserTab(
                    label = "1 usuario",
                    isSelected = selectedUsers == "1",
                    onClick = { selectedUsers = "1" }
                )
                UserTab(
                    label = "2-4 usuarios",
                    isSelected = selectedUsers == "2-4",
                    onClick = { selectedUsers = "2-4" }
                )
                UserTab(
                    label = "5-6 usuarios",
                    isSelected = selectedUsers == "5-6",
                    onClick = { selectedUsers = "5-6" }
                )
            }

            Spacer(Modifier.height(20.dp))

            // Planes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Plan anual
                PlanCard(
                    isSelected = selectedPlan == "annual",
                    title = "Annual",
                    price = "USD 239,99 por año",
                    savings = "Guardar 44%",
                    onClick = { selectedPlan = "annual" }
                )

                // Plan mensual
                PlanCard(
                    isSelected = selectedPlan == "monthly",
                    title = "Monthly",
                    price = "USD 35,99 por mes",
                    savings = null,
                    onClick = { selectedPlan = "monthly" }
                )
            }

            Spacer(Modifier.height(24.dp))

            // Resumen de costo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Vence 7 mar 2026",
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    )
                    Text(
                        text = "USD 239,99",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W600
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Vence hoy (14 días gratis)",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W600
                    )
                    Text(
                        text = "USD0",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Botón iniciar prueba
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(
                    text = "Iniciar prueba y suscribirse",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(12.dp))

            // Texto legal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tu suscripción se renueva automáticamente. Cancela en cualquier momento.",
                    fontSize = 12.sp,
                    color = Color(0xFF9E9E9E),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Más detalles",
                        fontSize = 12.sp,
                        color = Color(0xFF757575),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { }
                    )
                    Text(
                        text = "Términos y condiciones",
                        fontSize = 12.sp,
                        color = Color(0xFF757575),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun UserTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontSize = 14.sp,
        fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W400,
        color = if (isSelected) Color.Black else Color(0xFF9E9E9E),
        textDecoration = if (isSelected) TextDecoration.Underline else TextDecoration.None,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp)
    )
}

@Composable
private fun PlanCard(
    isSelected: Boolean,
    title: String,
    price: String,
    savings: String?,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color.Black else Color(0xFFE0E0E0)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio button
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = if (isSelected) Color.Black else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = if (isSelected) 0.dp else 2.dp,
                        color = if (isSelected) Color.Black else Color(0xFFCCCCCC),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color.White, RoundedCornerShape(5.dp))
                    )
                }
            }

            // Texto del plan
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Black
                )
                Text(
                    text = price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF757575)
                )
            }

            Spacer(Modifier.weight(1f))

            // Badge de ahorro
            if (savings != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF9E6)
                ) {
                    Text(
                        text = savings,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFFD97706)
                    )
                }
            }
        }
    }
}
