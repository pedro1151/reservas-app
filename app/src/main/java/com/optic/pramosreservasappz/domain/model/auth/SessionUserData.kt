package com.optic.pramosreservasappz.domain.model.auth


data class SessionUserData(
    val email: String = "",
    val userId: Int? = null,
    val businessId: Int? = null,
    val planCode: String = "basic",
    val businessName: String = "Mi Negocio",
    val isOwner: Boolean = false,
    val isCollaborator: Boolean = false,
    val isAdmin: Boolean = false
)