package com.optic.pramosreservasappz.presentation.screens.services.abmservicio

data class ServiceCreateState(
    val name: String = "",
    val durationMinutes: String = "",
    val description: String = "",
    val price: String = "",
    val isActive: Boolean = true,

    val bufferTime: String = "",
    val category: String = "",
    val color: String = "",
    val hidden: Boolean = false
)
