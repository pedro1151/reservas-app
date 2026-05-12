package com.optic.pramozventicoappz.presentation.screens.auth.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.optic.pramozventicoappz.core.Config
import com.optic.pramozventicoappz.presentation.ui.theme.EcommerceAppMVVMTheme

@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Config.CLIENT_ID_GOOGLE)
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
            } else {
                Log.e("LoginScreen", "Google Sign-In failed: idToken is null or empty")
            }

        } catch (e: ApiException) {
            Log.e("LoginScreen", "Google Sign-In failed. StatusCode: ${e.statusCode}", e)
        }
    }

    Scaffold { paddingValues ->
        LoginContent(
            navController = navController,
            paddingValues = paddingValues,
            onGoogleSignInClick = {
                googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            },
            vm = vm
        )
    }
}
