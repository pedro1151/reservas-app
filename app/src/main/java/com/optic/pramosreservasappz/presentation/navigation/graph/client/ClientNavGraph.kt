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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.optic.pramosreservasappz.presentation.screens.sales.SalesScreen
import com.optic.pramosreservasappz.presentation.screens.auth.login.basiclogin.BasicLoginScreen
import com.optic.pramosreservasappz.presentation.screens.business.abmmember.ABMbusinessMemberScreen
import com.optic.pramosreservasappz.presentation.screens.business.members.BusinessMembersScreen
import com.optic.pramosreservasappz.presentation.screens.business.mybusiness.MyBusinessScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steofour.CreateReservationStepFourScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.stepone.CreateResevationStepOneScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptwo.CreateReservationStepTwoScreen
import com.optic.pramosreservasappz.presentation.screens.clients.ClientDetailScreen
import com.optic.pramosreservasappz.presentation.screens.clients.ClientPrincipalScreen
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.ABMClienteScreen
import com.optic.pramosreservasappz.presentation.screens.historial.HistorialScreen
import com.optic.pramosreservasappz.presentation.screens.planes.PlansScreen
import com.optic.pramosreservasappz.presentation.screens.productos.ProductScreen
import com.optic.pramosreservasappz.presentation.screens.sales.SalesStatsScreen
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.sales.detail.SaleDetailScreen
import com.optic.pramosreservasappz.presentation.screens.rapidsale.RapidSaleScreen
import com.optic.pramosreservasappz.presentation.screens.rapidsale.resumen.RapidSaleResumenScreen
import com.optic.pramosreservasappz.presentation.screens.salecomplete.selecclient.SelectClientScreen
import com.optic.pramosreservasappz.presentation.screens.salecomplete.stepone.CompleteSaleStepOneScreen
import com.optic.pramosreservasappz.presentation.screens.salecomplete.steptree.CompleteSaleStepTreeScreen
import com.optic.pramosreservasappz.presentation.screens.salecomplete.steptwo.CompleteSaleStepTwoScreen
import com.optic.pramosreservasappz.presentation.screens.salestats.modefire.SalesStatsFireContent
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
    val saleViewModel1: SalesViewModel = hiltViewModel() // 🔥 UNA SOLA INSTANCIA

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
        modifier = Modifier.fillMaxSize(),
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

        composable(
            route = ClientScreen.Sales.route,
        ) {
            SalesScreen( navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Clientes.route,
        ) {
            ClientPrincipalScreen(navController, isAuthenticated)
        }


        composable(
            route = ClientScreen.Historial.route
        ) {
            HistorialScreen(navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Productos.route
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
            )
        ) { backStackEntry ->
            val saleId = backStackEntry.arguments?.getInt("saleId") ?: return@composable
            SaleDetailScreen(
                navController = navController,
               saleId = saleId,
                isAuthenticated = isAuthenticated
            )
        }


            composable(
                route =  ClientScreen.RapidSale.route
            ) {
                RapidSaleScreen(
                    navController = navController,
                    isAuthenticated = isAuthenticated,
                    viewModel = saleViewModel1
                )
            }


            composable(
                route = ClientScreen.RapidSaleResumen.route
            ) {

                RapidSaleResumenScreen(
                    navController = navController,
                    isAuthenticated = isAuthenticated,
                    viewModel = saleViewModel1
                )
            }


        composable(
            route =  ClientScreen.CompleteSaleStepOne.route,
        ) {
            CompleteSaleStepOneScreen(
                navController = navController,
                isAuthenticated = isAuthenticated,
                viewModel = saleViewModel1
            )
        }

        composable(
            route =  ClientScreen.CompleteSaleStepTree.route
        ) {
            CompleteSaleStepTreeScreen(
                navController = navController,
                isAuthenticated = isAuthenticated,
                viewModel = saleViewModel1
            )
        }


        composable(
            route =  ClientScreen.CompleteSaleStepTwo.route
        ) {
            CompleteSaleStepTwoScreen(
                navController = navController,
                isAuthenticated = isAuthenticated,
                viewModel = saleViewModel1
            )
        }




        composable(
            route = ClientScreen.Sales.route
        ) {
            SalesScreen( navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Historial.route
        ) {
            HistorialScreen(navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.SaleStats.route
        ) {
            SalesStatsScreen(
                navController = navController,
                isAuthenticated = isAuthenticated
            )
        }

        composable(
            route = ClientScreen.SelecClient.route
        ) {
            SelectClientScreen(
                navController = navController,
                viewModel = saleViewModel1
            )
        }



        composable(
            route = ClientScreen.Servicios.route
        ) {
            ServiceScreen(navController, isAuthenticated)
        }


        navigation(
            route = Graph.CREATE_RESERVATION_GRAPH,
            startDestination = ClientScreen.CreateReservationStepOne.route
        ) {

            composable(
                route = ClientScreen.CreateReservationStepOne.route
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
            )
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
            )
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
            )
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
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getInt("serviceId") ?: return@composable
            ServiceDetailScreen(
                navController = navController,
                serviceId = serviceId
            )
        }

        composable(
            route = ClientScreen.Login.route
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

        composable(route = ClientScreen.Profile.route
        )
        {
            MasScreen(navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.BasicLogin.route
        ) {
            BasicLoginScreen(navController = navController)
        }

        composable(route = ClientScreen.Mas.route
        ) {
            MasScreen(navController, isAuthenticated)
        }

        composable(route = ClientScreen.Planes.route
        ) {
            PlansScreen(
                navController = navController
            )

        }

        // busines

        composable(
            route = ClientScreen.BusinessMembers.route
        ) {
            BusinessMembersScreen(
                navController
            )
        }

        composable(
            route = ClientScreen.ABMBusinessMember.route
        ) {
            ABMbusinessMemberScreen(
                navController
            )
        }

        composable(
            route = ClientScreen.MyBusiness.route
        ) {
            MyBusinessScreen(
                navController
            )
        }

        ProfileNavGraph(navController)
    }
}
