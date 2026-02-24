package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.state

data class ClientCreateState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val enterprise: String = "",
    val country: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = ""
)