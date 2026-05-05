package com.optic.pramosreservasappz.presentation.screens.productos.abmproducto


data class ProductCreateState(
    val name: String = "",
    val price: String = "",
    val stock: String = "",
    val isActive: Boolean = true,
    val description: String="",
    val type: String = "product"
)