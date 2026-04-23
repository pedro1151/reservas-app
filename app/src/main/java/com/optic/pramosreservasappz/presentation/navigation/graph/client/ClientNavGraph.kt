package com.optic.pramosreservasappz.presentation.navigation.graph.client

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument

import com.optic.pramosreservasappz.presentation.navigation.Graph
import com.optic.pramosreservasappz.presentation.navigation.graph.profile.ProfileNavGraph
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen

import com.optic.pramosreservasappz.presentation.screens.auth.login.LoginScreen
import com.optic.pramosreservasappz.presentation.screens.auth.login.LoginViewModel
import com.optic.pramosreservasappz.presentation.screens.auth.login.components.LoginContentPless
import com.optic.pramosreservasappz.presentation.screens.auth.login.basiclogin.BasicLoginScreen

import com.optic.pramosreservasappz.presentation.screens.inicio.SalesScreen
import com.optic.pramosreservasappz.presentation.screens.inicio.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.tusventas.detail.SaleDetailScreen

import com.optic.pramosreservasappz.presentation.screens.newsale.selecclient.SelectClientScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.stepone.CompleteSaleStepOneScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.steptree.CompleteSaleStepTreeScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.steptwo.CompleteSaleStepTwoScreen

import com.optic.pramosreservasappz.presentation.screens.clients.ClientPrincipalScreen
import com.optic.pramosreservasappz.presentation.screens.clients.ClientDetailScreen
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.ABMClienteScreen

import com.optic.pramosreservasappz.presentation.screens.services.ServiceScreen
import com.optic.pramosreservasappz.presentation.screens.services.ServiceDetailScreen
import com.optic.pramosreservasappz.presentation.screens.services.abmservicio.ABMServiceScreen

import com.optic.pramosreservasappz.presentation.screens.tusventas.HistorialScreen
import com.optic.pramosreservasappz.presentation.screens.productos.ProductScreen
import com.optic.pramosreservasappz.presentation.screens.configuracion.MasScreen
import com.optic.pramosreservasappz.presentation.screens.planes.PlansScreen

import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.stepone.CreateResevationStepOneScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptwo.CreateReservationStepTwoScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steofour.CreateReservationStepFourScreen

import com.optic.pramosreservasappz.presentation.screens.business.members.BusinessMembersScreen
import com.optic.pramosreservasappz.presentation.screens.business.createmember.ABMbusinessMemberScreen
import com.optic.pramosreservasappz.presentation.screens.business.mybusiness.MyBusinessScreen
import com.optic.pramosreservasappz.presentation.screens.business.updatemember.UpdateMemberScreen
import com.optic.pramosreservasappz.presentation.screens.estadisticas.SalesStatsScreen
import com.optic.pramosreservasappz.presentation.screens.recibos.ReciboScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClientNavGraph(
    navController: NavHostController,
    isAuthenticated: Boolean,
    onShowRewardAd: () -> Unit
) {

    val salesViewModel: SalesViewModel = hiltViewModel()

    val enterAnim: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(320)
        ) + fadeIn(tween(320))
    }

    val exitAnim: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(320)
        ) + fadeOut(tween(320))
    }

    val popEnterAnim: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(320)
        ) + fadeIn(tween(320))
    }

    val popExitAnim: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(320)
        ) + fadeOut(tween(320))
    }

    NavHost(
        navController = navController,
        route = Graph.CLIENT,
        startDestination = if (isAuthenticated) {
            ClientScreen.Sales.route
        } else {
            ClientScreen.Login.route
        },
        modifier = Modifier.fillMaxSize(),
        enterTransition = enterAnim,
        exitTransition = exitAnim,
        popEnterTransition = popEnterAnim,
        popExitTransition = popExitAnim
    ) {

        composable(ClientScreen.Sales.route) {
            SalesScreen(navController, isAuthenticated)
        }

        composable(ClientScreen.Clientes.route) {
            ClientPrincipalScreen(navController, isAuthenticated)
        }

        composable(ClientScreen.Historial.route) {
            HistorialScreen(navController, isAuthenticated)
        }

        composable(ClientScreen.Productos.route) {
            ProductScreen(navController, isAuthenticated)
        }

        composable(ClientScreen.Servicios.route) {
            ServiceScreen(navController, isAuthenticated)
        }

        composable(ClientScreen.Login.route) {
            LoginScreen(navController)
        }

        composable(ClientScreen.BasicLogin.route) {
            BasicLoginScreen(navController)
        }

        composable(ClientScreen.Mas.route) {
            MasScreen(navController, isAuthenticated)
        }

        composable(ClientScreen.Profile.route) {
            MasScreen(navController, isAuthenticated)
        }

        composable(ClientScreen.Planes.route) {
            PlansScreen(navController)
        }

        composable(ClientScreen.SaleStats.route) {
            SalesStatsScreen(navController, isAuthenticated)
        }


        composable(ClientScreen.CompleteSaleStepOne.route) {
            CompleteSaleStepOneScreen(
                navController,
                isAuthenticated,
                salesViewModel
            )
        }

        composable(ClientScreen.CompleteSaleStepTwo.route) {
            CompleteSaleStepTwoScreen(
                navController,
                isAuthenticated,
                salesViewModel
            )
        }

        composable(ClientScreen.CompleteSaleStepTree.route) {
            CompleteSaleStepTreeScreen(
                navController,
                isAuthenticated,
                salesViewModel
            )
        }

        composable(ClientScreen.SelecClient.route) {
            SelectClientScreen(
                navController,
                salesViewModel
            )
        }

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
                navController,
                saleId,
                isAuthenticated
            )
        }

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
                navController,
                clientId
            )
        }

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
                navController,
                serviceId
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

            val clientId =
                backStackEntry.arguments?.getString("clientId")?.toIntOrNull()

            val editable =
                backStackEntry.arguments?.getBoolean("editable") ?: false

            ABMClienteScreen(
                navController,
                clientId,
                editable
            )
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

            val serviceId =
                backStackEntry.arguments?.getString("serviceId")?.toIntOrNull()

            val editable =
                backStackEntry.arguments?.getBoolean("editable") ?: false

            ABMServiceScreen(
                navController,
                serviceId,
                editable
            )
        }

        navigation(
            route = Graph.CREATE_RESERVATION_GRAPH,
            startDestination = ClientScreen.CreateReservationStepOne.route
        ) {

            composable(ClientScreen.CreateReservationStepOne.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Graph.CREATE_RESERVATION_GRAPH)
                }

                val vm: CalendarViewModel = hiltViewModel(parentEntry)

                CreateResevationStepOneScreen(navController, vm)
            }

            composable(ClientScreen.CreateReservationStepTwo.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Graph.CREATE_RESERVATION_GRAPH)
                }

                val vm: CalendarViewModel = hiltViewModel(parentEntry)

                CreateReservationStepTwoScreen(navController, vm)
            }

            composable(ClientScreen.CreateReservationStepFour.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Graph.CREATE_RESERVATION_GRAPH)
                }

                val vm: CalendarViewModel = hiltViewModel(parentEntry)

                CreateReservationStepFourScreen(navController, vm)
            }
        }

        composable(
            route = ClientScreen.BusinessMembers.route
        ) {
            BusinessMembersScreen(navController)
        }

        composable(
            route = ClientScreen.ABMBusinessMember.route
        ) {
            ABMbusinessMemberScreen(navController)
        }

        composable(
            route = ClientScreen.MyBusiness.route
        ) {
            MyBusinessScreen(navController)
        }

        composable(
            route = ClientScreen.UpdateBusinessMember.route,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val userId =
                backStackEntry.arguments?.getInt("userId")
                    ?: return@composable

            UpdateMemberScreen(
                navController,
                userId
            )
        }


        composable(
            route = ClientScreen.ReciboDetail.route,
            arguments = listOf(
                navArgument("saleId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val saleId =
                backStackEntry.arguments?.getInt("saleId")
                    ?: return@composable

            ReciboScreen(
                navController = navController,
                saleId =  saleId
            )
        }

        composable(Graph.CLIENT + "/{email}") { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(ClientScreen.Login.route)
            }

            val vm: LoginViewModel = hiltViewModel(parentEntry)

            val email =
                backStackEntry.arguments?.getString("email") ?: ""

            LoginContentPless(
                navController,
                vm,
                email
            )
        }

        ProfileNavGraph(navController)
    }
}