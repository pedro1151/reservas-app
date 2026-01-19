package com.optic.pramosfootballappz.presentation.screens.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.optic.pramosfootballappz.presentation.screens.auth.login.components.LoginContent
import com.optic.pramosfootballappz.presentation.ui.theme.EcommerceAppMVVMTheme

/* Google Auth */
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.optic.pramosfootballappz.core.Config

@Composable
fun LoginScreen(navController: NavHostController
                ,vm: LoginViewModel = hiltViewModel()) {

    // 1. Configurar GoogleSignInClient -- GoogleAUth
    val context = LocalContext.current
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Config.CLIENT_ID_GOOGLE) // CLIENT_ID desde la consola de Google
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (!idToken.isNullOrEmpty()) {
                vm.loginWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google Sign-In failed", e)
        }
    }
    
    Scaffold(
    ) { paddingValues ->
        LoginContent(
              navController = navController
            , paddingValues
            , onGoogleSignInClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            vm = vm

        )
    }
/*
    // ✅ Si el usuario se loguea correctamente:
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            // Si vino con redirect, volver ahí
            if (!redirect.isNullOrBlank()) {
                navController.navigate(redirect) {
                    popUpTo(Graph.AUTH) { inclusive = true }
                }
            } else {
                navController.navigate(Graph.CLIENT) {
                    popUpTo(Graph.AUTH) { inclusive = true }
                }
            }
        }
    }

 */


   // Login(navController = navController)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    EcommerceAppMVVMTheme {
        LoginScreen(rememberNavController())
    }
}