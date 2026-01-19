package com.optic.pramosfootballappz.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*  // Importa Material3
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.google.android.gms.ads.MobileAds
import com.optic.pramosfootballappz.ads.RewardedAdManager
import com.optic.pramosfootballappz.data.dataSource.local.datastore.AuthDatastore
import com.optic.pramosfootballappz.presentation.authstate.AuthStateVM
import com.optic.pramosfootballappz.presentation.navigation.graph.client.ClientNavGraph
import com.optic.pramosfootballappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosfootballappz.presentation.screens.menubottombar.components.ClientBottomBar
import com.optic.pramosfootballappz.presentation.settings.idiomas.AppLanguage
import com.optic.pramosfootballappz.presentation.settings.idiomas.LanguageViewModel
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalAppLanguage
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.presentation.settings.idiomas.withLocale
import com.optic.pramosfootballappz.presentation.ui.theme.EcommerceAppMVVMTheme
import com.optic.pramosfootballappz.presentation.util.logCoilCacheUsage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authDatastore: AuthDatastore
    /*
    override fun attachBaseContext(newBase: Context) {

        val localizedContext = LocaleManager.attachBaseContext(newBase, authDatastore)
        super.attachBaseContext(localizedContext)
    }

     */
    private val mainVM: MainViewModel by viewModels() // ✅ CORRECTO

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        //LocaleManager.applyLocale(this, authDatastore) // 🔥 AQUÍ

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

            // 🔹 Estado global del idioma
            val languageVM: LanguageViewModel = hiltViewModel()
            val languageCode by languageVM.language.collectAsState()

            val localizedContext = remember(languageCode) {
                applicationContext.withLocale(languageCode)
            }

            val appLanguageState = remember(languageCode) {
                mutableStateOf(
                    AppLanguage.values().first { it.code == languageCode }
                )
            }
            // 🔹 Proveemos idioma + contexto localizado
            CompositionLocalProvider(
                LocalizedContext provides localizedContext,
                LocalAppLanguage provides appLanguageState
            ) {
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
                        ClientScreen.Prode.route
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
}