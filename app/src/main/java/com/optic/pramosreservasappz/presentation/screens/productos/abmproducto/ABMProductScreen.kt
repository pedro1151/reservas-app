package com.optic.pramosreservasappz.presentation.screens.productos.abmproducto


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductUpdateRequest
import com.optic.pramosreservasappz.presentation.authstate.AuthStateVM
import com.optic.pramosreservasappz.presentation.screens.productos.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABMProductScreen(
    navController: NavHostController,
    productId: Int?,
    editable: Boolean,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()

    val businessId = sessionData.businessId
    val viewModel: ProductViewModel = hiltViewModel()

    val productState = viewModel.oneProductState.value

    LaunchedEffect(productId) {
        if (editable && productId != null) {
            viewModel.getProductById(productId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, null, tint = Color.Black)
                    }
                },
                title = {
                    Text(
                        text = if (editable) "Editar producto" else "Agregar producto",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.MoreVert, null, tint = Color.Black)
                    }

                    Button(
                        onClick = {

                            val state = viewModel.formState.value

                            if (editable && productId != null) {

                                viewModel.updateProduct(
                                    productId,
                                    ProductUpdateRequest(
                                        name = state.name,
                                        price = state.price.toDoubleOrNull(),
                                        stock = state.stock.toIntOrNull(),
                                        isActive = state.isActive,
                                        type = state.type,
                                        description = state.description
                                    )
                                )

                            } else {

                                viewModel.createProduct(
                                    ProductCreateRequest(
                                        name = state.name,
                                        description = state.description,
                                        type = state.type,
                                        price = state.price.toDoubleOrNull() ?: 0.0,
                                        stock = state.stock.toIntOrNull(),
                                        businessId = businessId
                                    )
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "Guardar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        ABMProductContent(
            paddingValues = paddingValues,
            navController = navController,
            productId = productId,
            editable = editable,
            viewModel = viewModel
        )
    }
}