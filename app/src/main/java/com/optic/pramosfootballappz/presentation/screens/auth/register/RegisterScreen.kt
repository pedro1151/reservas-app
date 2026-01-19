package com.optic.pramosfootballappz.presentation.screens.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.optic.pramosfootballappz.presentation.components.DefaultTopBar
import com.optic.pramosfootballappz.presentation.screens.auth.register.components.Register
import com.optic.pramosfootballappz.presentation.screens.auth.register.components.RegisterContent
import com.optic.pramosfootballappz.presentation.ui.theme.EcommerceAppMVVMTheme

@Composable
fun RegisterScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Registro",
                upAvailable = true,
                navController = navController
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {
            RegisterContent(paddingValues = paddingValues)
        }
    }

    // Este se ejecuta siempre, si no lo controlás con un estado, puede causar bugs
    Register(navController)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    EcommerceAppMVVMTheme {
        RegisterScreen(rememberNavController())
    }
}
