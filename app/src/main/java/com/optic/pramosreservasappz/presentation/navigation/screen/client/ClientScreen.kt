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

    object Sales: ClientScreen(
        route = "client/sales",
        title = "Inicio",
        icon = Icons.Default.Home
    )

    object CompleteSaleStepOne: ClientScreen(
        route = "client/complete_sale_one",
        title = "Ventas Paso 1",
        icon = Icons.Default.CheckCircle
    )

    object CompleteSaleStepTwo: ClientScreen(
        route = "client/complete_sale_two",
        title = "Ventas Paso 2",
        icon = Icons.Default.CheckCircle
    )

    object CompleteSaleStepTree: ClientScreen(
        route = "client/complete_sale_tree",
        title = "Ventas Paso 3",
        icon = Icons.Default.CheckCircle
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

    object CreateReservationStepFour: ClientScreen(
        route = "client/create_reservation_step_four",
        title = "Create Reservation step four",
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
        icon = Icons.Default.GifBox
    )

    object RapidSale: ClientScreen(
        route = "client/rapid_sale",
        title = "Venta Rapida",
        icon = Icons.Default.GifBox
    )

    object RapidSaleResumen: ClientScreen(
        route = "client/rapid_sale_resumen",
        title = "Venta Rapida Resumen",
        icon = Icons.Default.GifBox
    )

    object SaleDetail: ClientScreen(
        route = "client/detail_sale/{saleId}",
        title = "Detalle Venta",
        icon = Icons.Default.GifBox
    )
    {
        fun createRoute(saleId: Int): String = "client/detail_sale/$saleId"
    }
    // Pantalla de detalle del cliente
    object ClientDetail : ClientScreen(
        route = "client/detail/{clientId}",
        title = "Detalle Cliente",
        icon = Icons.Default.Person
    ) {
        fun createRoute(clientId: Int): String = "client/detail/$clientId"
    }



    object Productos: ClientScreen(
        route = "client/productos",
        title = "Productos",
        icon = Icons.Default.GifBox
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


    object ABMProduct : ClientScreen(
        route = "products/abm_product?productId={productId}&editable={editable}",
        title = "Crear/Editar SProducto",
        icon = Icons.Default.SpaceDashboard
    ) {
        fun createRoute(productId: Int? = null, editable: Boolean = false): String {
            return "products/abm_product?productId=${productId ?: ""}&editable=$editable"
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



    // Pantalla de detalle del servicio
    object ServiceDetail : ClientScreen(
        route = "client/service_detail/{serviceId}",
        title = "Detalle Servicio",
        icon = Icons.Default.SpaceDashboard
    ) {
        fun createRoute(serviceId: Int): String = "client/service_detail/$serviceId"
    }


    // Pantalla de detalle de producto
    object ProductDetail : ClientScreen(
        route = "products/product_detail/{productId}",
        title = "Detalle Producto",
        icon = Icons.Default.SpaceDashboard
    ) {
        fun createRoute(productId: Int): String = "products/product_detail/$productId"
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

    object SaleStats: ClientScreen(
        route = "client/fire_stats",
        title = "Resumen",
        icon = Icons.Default.BarChart
    )

    object SelecClient: ClientScreen(
        route = "client/select_client",
        title = "Seleccionar Cliente",
        icon = Icons.Default.BarChart
    )

    object BusinessMembers: ClientScreen(
        route = "client/business_members",
        title = "Colaboradores",
        icon = Icons.Default.BarChart
    )

    object ABMBusinessMember: ClientScreen(
        route = "client/create_business_member",
        title = "Crear colaborador",
        icon = Icons.Default.BarChart
    )

    object UpdateBusinessMember: ClientScreen(
            route = "client/update_business_member/{userId}",
            title = "Actualizar colaborador",
            icon = Icons.Default.SpaceDashboard
        ) {
            fun createRoute(userId: Int): String = "client/update_business_member/$userId"
        }

    object MyBusiness: ClientScreen(
        route = "client/mybusiness",
        title = "Mi Negocio",
        icon = Icons.Default.BarChart
    )

    object ReciboDetail: ClientScreen(
        route = "client/recibo_sale/{saleId}",
        title = "Recibo de venta",
        icon = Icons.Default.SpaceDashboard
    ) {
        fun createRoute(saleId: Int): String = "client/recibo_sale/$saleId"
    }






    object Historial: ClientScreen(
        route = "client/historial",
        title = "Tus Ventas",
        icon = Icons.Default.CheckCircle
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


    object Planes: ClientScreen(
        route = "client/planes",
        title = "Planes",
        icon = Icons.Default.Menu
    )

    object PlanMode: ClientScreen(
        route = "client/planes_mode",
        title = "Planes",
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


}
