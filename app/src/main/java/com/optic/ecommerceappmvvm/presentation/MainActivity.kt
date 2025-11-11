package com.optic.ecommerceappmvvm.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*  // Importa Material3
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.optic.ecommerceappmvvm.presentation.authstate.AuthStateVM
import com.optic.ecommerceappmvvm.presentation.navigation.graph.client.ClientNavGraph
import com.optic.ecommerceappmvvm.presentation.navigation.graph.root.RootNavGraph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.home.components.ClientBottomBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.MatchesScreen
import com.optic.ecommerceappmvvm.presentation.ui.theme.AppThemeMode
import com.optic.ecommerceappmvvm.presentation.ui.theme.EcommerceAppMVVMTheme
import com.optic.ecommerceappmvvm.presentation.ui.theme.LocalAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceAppMVVMTheme {
                val navController = rememberNavController()

                // Observamos la ruta actual
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination

                // ✅ Obtenemos el estado global de autenticación
                val authStateVM: AuthStateVM = hiltViewModel()
                val isAuthenticated by authStateVM.isAuthenticated.collectAsState()

                // Define qué pantallas muestran el BottomBar
                val bottomBarRoutes = listOf(
                    ClientScreen.Matches.route,
                    ClientScreen.Leagues.route,
                    ClientScreen.Mas.route,
                    ClientScreen.Profile.route,
                    ClientScreen.Follow.route,
                    ClientScreen.Games.route
                )

                val shouldShowBottomBar = currentDestination?.route in bottomBarRoutes

                Scaffold(
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            ClientBottomBar(navController = navController)
                        }
                    }
                ) { paddingValues ->
                    ClientNavGraph(
                        navController = navController,
                        isAuthenticated = isAuthenticated
                    )
                }
            }
        }
    }
}