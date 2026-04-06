package com.optic.pramosreservasappz.presentation.navigation.graph.client

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

import com.optic.pramosreservasappz.presentation.navigation.Graph
import com.optic.pramosreservasappz.presentation.navigation.graph.profile.ProfileNavGraph
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.auth.login.LoginScreen
import com.optic.pramosreservasappz.presentation.screens.auth.login.LoginViewModel
import com.optic.pramosreservasappz.presentation.screens.auth.login.components.LoginContentPless
import com.optic.pramosreservasappz.presentation.screens.mas.MasScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.optic.pramosreservasappz.presentation.screens.salestats.SalesStatsScreen

import com.optic.pramosreservasappz.presentation.screens.sales.SalesScreen
import com.optic.pramosreservasappz.presentation.screens.auth.login.basiclogin.BasicLoginScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steofour.CreateReservationStepFourScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.stepone.CreateResevationStepOneScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptree.CreateCalendarStepThreeScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptwo.CreateReservationStepTwoScreen
import com.optic.pramosreservasappz.presentation.screens.clients.ClientDetailScreen
import com.optic.pramosreservasappz.presentation.screens.clients.ClientPrincipalScreen
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.ABMClienteScreen
import com.optic.pramosreservasappz.presentation.screens.historial.HistorialScreen
import com.optic.pramosreservasappz.presentation.screens.productos.ProductScreen
import com.optic.pramosreservasappz.presentation.screens.sales.detail.SaleDetailScreen
import com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.RapidSaleScreen
import com.optic.pramosreservasappz.presentation.screens.services.ServiceDetailScreen
import com.optic.pramosreservasappz.presentation.screens.services.ServiceScreen
import com.optic.pramosreservasappz.presentation.screens.services.abmservicio.ABMServiceScreen

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClientNavGraph(
    navController: NavHostController,
    isAuthenticated: Boolean,
    onShowRewardAd: () -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        route = Graph.CLIENT,
        startDestination =
        if (isAuthenticated) {
            ClientScreen.Sales.route
        }
        else{
            ClientScreen.Login.route
        },
        modifier = Modifier.fillMaxSize()
    ) {

        composable(
            route = ClientScreen.Sales.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            SalesScreen( navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Clientes.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            ClientPrincipalScreen(navController, isAuthenticated)
        }



        composable(
            route = ClientScreen.Sales.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            SalesScreen( navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Historial.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            HistorialScreen(navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Productos.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            ProductScreen(navController, isAuthenticated)
        }

        // Detalle de una venta
        composable(
            route = ClientScreen.SaleDetail.route,
            arguments = listOf(
                navArgument("saleId") {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) { backStackEntry ->
            val saleId = backStackEntry.arguments?.getInt("saleId") ?: return@composable
            SaleDetailScreen(
                navController = navController,
               saleId = saleId,
                isAuthenticated = isAuthenticated
            )
        }


        composable(
            route = ClientScreen.RapidSale.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            RapidSaleScreen(
                navController = navController,
                isAuthenticated = isAuthenticated
            )
        }



        composable(
            route = ClientScreen.Sales.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            SalesScreen( navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Historial.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            HistorialScreen(navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Estadisticas.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            SalesStatsScreen(navController = navController)
        }

        composable(
            route = ClientScreen.Servicios.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) {
            ServiceScreen(navController, isAuthenticated)
        }


        navigation(
            route = Graph.CREATE_RESERVATION_GRAPH,
            startDestination = ClientScreen.CreateReservationStepOne.route
        ) {

            composable(
                route = ClientScreen.CreateReservationStepOne.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(350)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(350)
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(350)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(350)
                    )
                }
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Graph.CREATE_RESERVATION_GRAPH)
                }

                val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

                CreateResevationStepOneScreen(
                    navController,
                    viewModel
                )
            }

            composable(
                route = ClientScreen.CreateReservationStepTwo.route
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Graph.CREATE_RESERVATION_GRAPH)
                }

                val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

                CreateReservationStepTwoScreen(
                    navController,
                    viewModel
                )
            }

            composable(
                route = ClientScreen.CreateReservationStepThree.route
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Graph.CREATE_RESERVATION_GRAPH)
                }

                val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

                CreateCalendarStepThreeScreen(
                    navController,
                    viewModel
                )
            }

            composable(
                route = ClientScreen.CreateReservationStepFour.route
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Graph.CREATE_RESERVATION_GRAPH)
                }

                val viewModel: CalendarViewModel = hiltViewModel(parentEntry)

                CreateReservationStepFourScreen(
                    navController,
                    viewModel
                )
            }
        }


        composable(
            route = ClientScreen.ABMServicio.route,
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
                navArgument("editable") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()
            val editable = backStackEntry.arguments?.getBoolean("editable") ?: false
            ABMServiceScreen(
                navController = navController,
                serviceId = serviceId,
                editable = editable
            )
        }

        composable(
            route = ClientScreen.ABMCliente.route,
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
                navArgument("editable") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId")?.toIntOrNull()
            val editable = backStackEntry.arguments?.getBoolean("editable") ?: false
            ABMClienteScreen(
                navController = navController,
                clientId = clientId,
                editable = editable
            )
        }

        // Detalle del cliente
        composable(
            route = ClientScreen.ClientDetail.route,
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getInt("clientId") ?: return@composable
            ClientDetailScreen(
                navController = navController,
                clientId = clientId
            )
        }

        // Detalle del servicio
        composable(
            route = ClientScreen.ServiceDetail.route,
            arguments = listOf(
                navArgument("serviceId") {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getInt("serviceId") ?: return@composable
            ServiceDetailScreen(
                navController = navController,
                serviceId = serviceId
            )
        }

        composable(
            route = ClientScreen.Login.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 350)
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 350)
                )
            },
            popEnterTransition = {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(durationMillis = 350)
                )
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 350)
                )
            }
        ) {
            LoginScreen(navController)
        }

        composable(Graph.CLIENT + "/{email}") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(ClientScreen.Login.route)
            }
            val viewModel: LoginViewModel = hiltViewModel(parentEntry)
            val email = backStackEntry.arguments?.getString("email") ?: ""
            LoginContentPless(navController, viewModel, email)
        }

        composable(route = ClientScreen.Profile.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }) {
            MasScreen(navController, isAuthenticated)
        }

        composable(route = ClientScreen.BasicLogin.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }) {
            BasicLoginScreen(navController = navController)
        }

        composable(route = ClientScreen.Mas.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(350)
                )
            }) {
            MasScreen(navController, isAuthenticated)
        }

        ProfileNavGraph(navController)
    }
}
