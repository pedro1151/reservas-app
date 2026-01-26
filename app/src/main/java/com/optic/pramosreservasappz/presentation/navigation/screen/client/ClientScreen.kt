package com.optic.pramosreservasappz.presentation.navigation.screen.client

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.Airplay
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SignLanguage
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.ui.graphics.vector.ImageVector



sealed class ClientScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {


    object Clientes: ClientScreen(
        route = "client/clientes",
        title = "Clientes",
        icon = Icons.Default.SentimentSatisfied
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


    object Matches: ClientScreen(
        route = "client/calendario",
        title = "Calendario",
        icon = Icons.Default.DateRange
    )

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

