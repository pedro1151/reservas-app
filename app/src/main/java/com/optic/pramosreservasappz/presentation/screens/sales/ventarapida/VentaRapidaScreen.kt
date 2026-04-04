package com.optic.pramosreservasappz.presentation.screens.sales.ventarapida


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ShoppingCart
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
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import com.optic.pramosreservasappz.presentation.sales.Components.SRed
import com.optic.pramosreservasappz.presentation.screens.sales.ventarapida.components.SmallCircleButton
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.sales.Components.HeaderRapidSale
import com.optic.pramosreservasappz.presentation.screens.sales.ventarapida.components.SaleInputDefault
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaRapidaScreen(
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
                    .background(MaterialTheme.colorScheme.background)
                    .padding(5.dp)
            ) {
                // header titulo + boton cerrar
                HeaderRapidSale(
                    onDismiss = onDismiss
                )
                Spacer(Modifier.height(5.dp))

                // subtitle
                /*
                Text(
                    "Ingresa nuevo producto",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface
                )

                 */

                // INPUTS
                SaleInputDefault(
                    value = productName,
                    onValueChange = { productName = it },
                    label = "¿Qué vendiste?",
                    placeholder = "Ejemplo: Gaseosa, Galletitas, Servicio técnico etc",
                    keyboardOptions = KeyboardType.Text
                )


                Spacer(Modifier.height(8.dp))

                SaleInputDefault(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = "¿Cuánto cobraste?",
                    keyboardOptions = KeyboardType.Number,
                    placeholder ="Ejemplo: 5560"
                )
            }

            // ⚪ CONTENIDO BLANCO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Text(
                    "Escoge entre tus productos mas vendidos",
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                when (productsState) {

                    is Resource.Loading -> {
                        CircularProgressIndicator(
                            color = SAccent,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    is Resource.Failure -> {
                        Text(
                            "Error cargando productos",
                            color = SRed,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    is Resource.Success -> {

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f) // 🔥 CLAVE
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(bottom = 12.dp)
                        ) {

                            items(products) { product ->

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
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, BorderGray),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 0.dp // 🔥 casi flat (más elegante)
                                    )
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        // 🔹 IZQUIERDA → nombre + precio
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            // 🔹 ICONO (extremo izquierdo)
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (inCart != null) Color(0xFF22C55E) else Color(0xFFE5E7EB),
                                                modifier = Modifier.size(22.dp)
                                            )

                                            Spacer(Modifier.width(12.dp))

                                            // 🔹 TEXTO (nombre + precio)
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    product.name,
                                                    fontWeight = FontWeight.Medium,
                                                    color = SBlack
                                                )

                                                Text(
                                                    "$ ${product.price}",
                                                    color = SGray600,
                                                    fontSize = 13.sp
                                                )
                                            }

                                            // 🔹 CONTROLES (derecha)
                                            if (inCart != null) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {

                                                    SmallCircleButton("-") {
                                                        removeProduct(product)
                                                    }

                                                    Spacer(Modifier.width(8.dp))

                                                    Text(
                                                        "${inCart.second}",
                                                        fontWeight = FontWeight.Bold,
                                                        color = SBlack
                                                    )

                                                    Spacer(Modifier.width(8.dp))

                                                    SmallCircleButton("+") {
                                                        addProduct(product)
                                                    }
                                                }
                                            }
                                        }

                                        // 🔹 DERECHA → botones (+ / -)
                                        if (inCart != null) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                                SmallCircleButton("-") {
                                                    removeProduct(product)
                                                }

                                                Spacer(Modifier.width(8.dp))

                                                Text(
                                                    "${inCart.second}",
                                                    fontWeight = FontWeight.Bold,
                                                    color = SBlack
                                                )

                                                Spacer(Modifier.width(8.dp))

                                                SmallCircleButton("+") {
                                                    addProduct(product)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }

                // 🔥 FOOTER FIJO
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {

                    if (selectedProducts.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "Total",
                                tint = Color(0xFF7C3AED), // violeta branding
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "$",
                                color = Color(0xFF7C3AED),
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = "$total",
                                fontWeight = FontWeight.Bold,
                                color = SBlack,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(Modifier.height(12.dp))
                    }

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
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContainerColor = SGray400
                        )
                    ) {
                        Text("Registrar venta", color = Color.White)
                    }
                }
            }
        }
    }
}