package com.optic.pramosfootballappz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.optic.pramosfootballappz.presentation.authstate.AuthStateVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopBar(
    title: String = "",
    navController: NavController,
    onCalendarClick: (() -> Unit)? = null,   // 👈 Agregamos callback opcional
    showTitle: Boolean = true ,
    vm: AuthStateVM = hiltViewModel()
) {
    val isAuthenticated by vm.isAuthenticated.collectAsState()
    val userEmail by vm.userEmail.collectAsState()

    TopAppBar(
        title = {
            if (showTitle) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }
        },
        actions = {


            // 👉 ICONO DE CALENDARIO (si el callback existe)
            if (onCalendarClick != null) {
                IconButton(
                    onClick = { onCalendarClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Elegir fecha",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            /*
            if (isAuthenticated) {
                Text(
                    text = userEmail,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 16.dp)
                )
            } else {
                Text(
                    text = "Iniciar sesión",
                    color = Color(0xFF64B5F6), // Azul claro moderno (similar al de los botones)
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            navController.navigate(ClientScreen.Login.route) // 🔹 Ajusta la ruta según tu AuthScreen
                        }
                )
            }

             */
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}