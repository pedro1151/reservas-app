package com.optic.ecommerceappmvvm.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.google.android.gms.ads.MobileAds
import com.optic.ecommerceappmvvm.ads.RewardedAdManager
import com.optic.ecommerceappmvvm.presentation.authstate.AuthStateVM
import com.optic.ecommerceappmvvm.presentation.navigation.graph.client.ClientNavGraph
import com.optic.ecommerceappmvvm.presentation.navigation.graph.root.RootNavGraph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.home.components.ClientBottomBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.MatchesScreen
import com.optic.ecommerceappmvvm.presentation.ui.theme.AppThemeMode
import com.optic.ecommerceappmvvm.presentation.ui.theme.EcommerceAppMVVMTheme
import com.optic.ecommerceappmvvm.presentation.ui.theme.LocalAppTheme
import com.optic.ecommerceappmvvm.presentation.util.logCoilCacheUsage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainVM: MainViewModel by viewModels() // ✅ CORRECTO

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val loader = ImageLoader.Builder(this)
            .crossfade(true)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(200L * 1024 * 1024) // 200 MB
                    .build()
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.30) // usa 30% de la RAM
                    .build()
            }
            .build()

        Coil.setImageLoader(loader)
        // LOguear el uso de cache
        logCoilCacheUsage(loader)
        // ✅ Inicializa Google AdMob
        MobileAds.initialize(this)

        // ✅ Carga el anuncio recompensado una vez
        val rewardedManager = RewardedAdManager(this)
        rewardedManager.loadAd()

        setContent {
            EcommerceAppMVVMTheme {

                val navController = rememberNavController()
                // ACTIVA EL MAIN VM
                val mainVM = this@MainActivity.mainVM

                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination

                //val mainVM: MainViewModel = hiltViewModel()  // ✅ Aquí instancias MainViewModel
                val authStateVM: AuthStateVM = hiltViewModel()
                val isAuthenticated by authStateVM.isAuthenticated.collectAsState()

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

                    // 🚀 PASO CLAVE
                    // Pasas una función a tu NavGraph para mostrar Rewarded Ads desde cualquier pantalla
                    ClientNavGraph(
                        navController = navController,
                        isAuthenticated = isAuthenticated,
                        onShowRewardAd = {
                            rewardedManager.showAd(this) { reward ->
                                println("Usuario ganó $reward puntos")
                            }
                        }
                    )
                }
            }
        }
    }
}