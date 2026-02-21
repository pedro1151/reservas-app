package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

data class ClientCreateState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val city: String = "",
    val country: String
)
