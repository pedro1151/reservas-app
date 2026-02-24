package com.optic.pramosreservasappz.presentation.navigation.screen.client

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ClientScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Clientes: ClientScreen(
        route = "client/clientes",
        title = "Clientes",
        icon = Icons.Default.Star
    )

    object CreateReservationStepOne: ClientScreen(
        route = "client/create_reservation_step_one",
        title = "Create Reservation step one",
        icon = Icons.Default.Star
    )

    object CreateReservationStepTwo: ClientScreen(
        route = "client/create_reservation_step_two",
        title = "Create Reservation step two",
        icon = Icons.Default.Star
    )

    object CreateReservationStepThree: ClientScreen(
        route = "client/create_reservation_step_three",
        title = "Create Reservation step three",
        icon = Icons.Default.Star
    )

    object Calendario: ClientScreen(
        route = "client/calendar",
        title = "Calendario",
        icon = Icons.Default.CalendarMonth
    )

    object Servicios: ClientScreen(
        route = "client/servicios",
        title = "Servicios",
        icon = Icons.Default.SpaceDashboard
    )

    object ABMServicio : ClientScreen(
        route = "client/create_service?serviceId={serviceId}&editable={editable}",
        title = "Crear/Editar Servicio",
        icon = Icons.Default.SpaceDashboard
    ) {
        fun createRoute(serviceId: Int? = null, editable: Boolean = false): String {
            return "client/create_service?serviceId=${serviceId ?: ""}&editable=$editable"
        }
    }

    object ABMCliente : ClientScreen(
        route = "create_client?clientId={clientId}&editable={editable}",
        title = "Crear/Editar Cliente",
        icon = Icons.Default.SpaceDashboard
    ) {
        fun createRoute(clientId: Int? = null, editable: Boolean = false): String {
            return "create_client?clientId=${clientId ?: ""}&editable=$editable"
        }
    }

    // Pantalla de detalle del cliente
    object ClientDetail : ClientScreen(
        route = "client/detail/{clientId}",
        title = "Detalle Cliente",
        icon = Icons.Default.Person
    ) {
        fun createRoute(clientId: Int): String = "client/detail/$clientId"
    }

    // Pantalla de detalle del servicio
    object ServiceDetail : ClientScreen(
        route = "client/service_detail/{serviceId}",
        title = "Detalle Servicio",
        icon = Icons.Default.SpaceDashboard
    ) {
        fun createRoute(serviceId: Int): String = "client/service_detail/$serviceId"
    }

    object Games: ClientScreen(
        route = "client/games",
        title = "Games",
        icon = Icons.Default.PlayArrow
    )

    object Profile: ClientScreen(
        route = "client/profile",
        title = "Menu",
        icon = Icons.Default.Menu
    )

    object Estadisticas: ClientScreen(
        route = "client/players/estadisticas",
        title = "Estadisticas",
        icon = Icons.Default.Send
    )

    object Fixture: ClientScreen(
        route = "client/players/fixture",
        title = "Perfil",
        icon = Icons.Default.Info
    )

    object Historial: ClientScreen(
        route = "client/players/historial",
        title = "Historial",
        icon = Icons.Default.DateRange
    )

    object Follow: ClientScreen(
        route = "client/follow",
        title = "Siguiendo",
        icon = Icons.Default.FavoriteBorder
    )

    object Mas: ClientScreen(
        route = "client/mas",
        title = "Menu",
        icon = Icons.Default.Menu
    )

    object Login: ClientScreen(
        route = "client/login",
        title = "Login",
        icon = Icons.Default.Menu
    )

    object LoginPless: ClientScreen(
        route = "client/loginpless",
        title = "Loginpless",
        icon = Icons.Default.Menu
    )

    object BasicLogin: ClientScreen(
        route = "client/basiclogin",
        title = "Basic Login",
        icon = Icons.Default.SignLanguage
    )

    object ProdeRanking: ClientScreen(
        route = "client/proderank",
        title = "Prode Ranking",
        icon = Icons.Default.SignLanguage
    )

    object UserPredictionSummary: ClientScreen(
        route = "client/user_prediction_summary",
        title = "User prediction summary",
        icon = Icons.Default.SignLanguage
    )
}
