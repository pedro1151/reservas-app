package com.optic.pramosreservasappz.presentation.screens.sales.Components


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleItemCreateWithoutSaleId
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.sales.Components.SAccent
import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray100
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import com.optic.pramosreservasappz.presentation.sales.Components.SRed
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterSaleSheet(
    viewModel: SalesViewModel,
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var productName by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }

    val productsState by viewModel.productsState.collectAsState()
    val products = (productsState as? Resource.Success)?.data ?: emptyList()

    val selectedProducts = remember {
        mutableStateListOf<Pair<ProductResponse, Int>>()
    }

    fun addProduct(product: ProductResponse) {
        val index = selectedProducts.indexOfFirst { it.first.id == product.id }
        if (index >= 0) {
            val current = selectedProducts[index]
            selectedProducts[index] = current.copy(second = current.second + 1)
        } else {
            selectedProducts.add(product to 1)
        }
    }

    fun removeProduct(product: ProductResponse) {
        val index = selectedProducts.indexOfFirst { it.first.id == product.id }
        if (index >= 0) {
            val current = selectedProducts[index]
            if (current.second <= 1) {
                selectedProducts.removeAt(index)
            } else {
                selectedProducts[index] = current.copy(second = current.second - 1)
            }
        }
    }

    val total = selectedProducts.sumOf { it.first.price * it.second }

    val canConfirm =
        priceText.isNotEmpty() || selectedProducts.isNotEmpty()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {

        Column(
            Modifier
                .fillMaxWidth()
        ) {

            // 🟢 HEADER VERDE (DIVISIÓN VISUAL)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SAccent)
                    .padding(20.dp)
            ) {
                Text(
                    "Nueva venta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(12.dp))

                // INPUTS
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("¿Qué vendiste?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(0.6f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(0.7f),
                        cursorColor = Color.White
                    )
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("¿Cuánto cobraste?") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(0.6f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(0.7f),
                        cursorColor = Color.White
                    )
                )
            }

            // ⚪ CONTENIDO BLANCO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SGray100)
                    .padding(16.dp)
            ) {

                Text(
                    "Productos recientes",
                    color = SGray600,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(12.dp))

                when (productsState) {

                    is Resource.Loading -> {
                        CircularProgressIndicator(color = SAccent)
                    }

                    is Resource.Failure -> {
                        Text("Error cargando productos", color = SRed)
                    }

                    is Resource.Success -> {

                        products.forEach { product ->

                            val inCart =
                                selectedProducts.find { it.first.id == product.id }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { addProduct(product) },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column {
                                        Text(
                                            product.name,
                                            fontWeight = FontWeight.SemiBold,
                                            color = SBlack
                                        )
                                        Text(
                                            "Bs ${product.price}",
                                            color = SGray600,
                                            fontSize = 13.sp
                                        )
                                    }

                                    if (inCart != null) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(SBlack, CircleShape)
                                                    .clickable { removeProduct(product) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("-", color = Color.White)
                                            }

                                            Spacer(Modifier.width(8.dp))

                                            Text(
                                                "${inCart.second}",
                                                fontWeight = FontWeight.Bold,
                                                color = SBlack
                                            )

                                            Spacer(Modifier.width(8.dp))

                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(SBlack, CircleShape)
                                                    .clickable { addProduct(product) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+", color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }

                Spacer(Modifier.height(16.dp))

                if (selectedProducts.isNotEmpty()) {
                    Text(
                        "Total: Bs $total",
                        fontWeight = FontWeight.Bold,
                        color = SBlack,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 🟢 BOTÓN PRINCIPAL
                Button(
                    onClick = {

                        val now = java.time.LocalDateTime.now()
                        val generatedName =
                            "Venta ${now.dayOfMonth}/${now.monthValue}/${now.year} ${now.hour}:${now.minute}"

                        if (selectedProducts.isEmpty()) {

                            val price = priceText.toDoubleOrNull() ?: 0.0

                            val saleRequest = SaleCreateRequest(
                                ownerId = 1,
                                createdByUserId = 1,
                                amount = price,
                                description = productName.ifEmpty { generatedName }
                            )

                            viewModel.createSale(saleRequest)

                            if (productName.isNotEmpty()) {
                                viewModel.createProduct(
                                    ProductCreateRequest(
                                        userId = 1,
                                        name = productName,
                                        price = price
                                    )
                                )
                            }

                        } else {

                            val items = selectedProducts.map {
                                SaleItemCreateWithoutSaleId(
                                    productId = it.first.id,
                                    quantity = it.second,
                                    price = it.first.price
                                )
                            }

                            val totalAmount = selectedProducts.sumOf {
                                it.first.price * it.second
                            }

                            val saleRequest = CreateSaleWithItemsRequest(
                                sale = SaleCreateRequest(
                                    ownerId = 1,
                                    createdByUserId = 1,
                                    amount = totalAmount,
                                    description = generatedName
                                ),
                                items = items
                            )

                            viewModel.createSaleWithItems(saleRequest)
                        }

                        onDismiss()
                    },
                    enabled = canConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SAccent,
                        disabledContainerColor = SGray400
                    )
                ) {
                    Text("Registrar venta", color = Color.White)
                }
            }
        }
    }
}