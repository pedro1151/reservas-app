package com.optic.pramosreservasappz.presentation.screens.sales.temporalcomponents


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.presentation.sales.Components.CartItem
import com.optic.pramosreservasappz.presentation.sales.Components.ProductItem
import com.optic.pramosreservasappz.presentation.sales.Components.SAccent
import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray100
import com.optic.pramosreservasappz.presentation.sales.Components.SGray200
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import com.optic.pramosreservasappz.presentation.sales.Components.SaleItem
import com.optic.pramosreservasappz.presentation.sales.Components.SaleStatus
import com.optic.pramosreservasappz.presentation.sales.Components.fakeClients
import com.optic.pramosreservasappz.presentation.sales.Components.fakeProducts
import com.optic.pramosreservasappz.presentation.sales.Components.fakeSales
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarVentaFicticio(
    products:  List<ProductItem> = fakeProducts,
    clients:   List<String>      = fakeClients,
    onDismiss: () -> Unit,
    onConfirm: (SaleItem) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var clientQuery      by remember { mutableStateOf("") }
    var selectedClient   by remember { mutableStateOf("") }
    var showClientDrop   by remember { mutableStateOf(false) }
    var productQuery     by remember { mutableStateOf("") }
    val cart             = remember { mutableStateListOf<CartItem>() }

    val filteredClients  = remember(clientQuery) {
        if (clientQuery.length < 1) emptyList()
        else clients.filter { it.contains(clientQuery, ignoreCase = true) }
    }
    val filteredProducts = remember(productQuery, products) {
        if (productQuery.isEmpty()) products
        else products.filter { it.name.contains(productQuery, ignoreCase = true) }
    }
    val cartTotal = cart.sumOf { it.product.price * it.quantity }

    fun addToCart(product: ProductItem) {
        val idx = cart.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) cart[idx] = cart[idx].copy(quantity = cart[idx].quantity + 1)
        else cart.add(CartItem(product))
    }
    fun removeFromCart(product: ProductItem) {
        val idx = cart.indexOfFirst { it.product.id == product.id }
        if (idx < 0) return
        if (cart[idx].quantity <= 1) cart.removeAt(idx)
        else cart[idx] = cart[idx].copy(quantity = cart[idx].quantity - 1)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState,
        containerColor   = Color.White,
        tonalElevation   = 0.dp,
        dragHandle = {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp), contentAlignment = Alignment.Center) {
                Box(Modifier.width(36.dp).height(4.dp).clip(CircleShape).background(SGray200))
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nueva venta", fontSize = 20.sp, fontWeight = FontWeight.W800, color = SBlack, letterSpacing = (-0.5).sp)
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(SGray100)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Close, null, tint = SGray600, modifier = Modifier.size(16.dp)) }
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
            ) {
                SectionLabel("Cliente")
                Spacer(Modifier.height(8.dp))

                Column {
                    OutlinedTextField(
                        value = if (selectedClient.isNotEmpty()) selectedClient else clientQuery,
                        onValueChange = {
                            if (selectedClient.isNotEmpty()) selectedClient = ""
                            clientQuery    = it
                            showClientDrop = it.isNotEmpty()
                        },
                        modifier    = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar cliente…", color = SGray400, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Outlined.Person, null, tint = SGray400, modifier = Modifier.size(18.dp)) },
                        trailingIcon = if (selectedClient.isNotEmpty()) {
                            { Icon(Icons.Default.CheckCircle, null, tint = SAccent, modifier = Modifier.size(18.dp)) }
                        } else null,
                        singleLine = true,
                        shape      = RoundedCornerShape(12.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = SBlack,
                            unfocusedBorderColor    = SGray200,
                            focusedContainerColor   = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = SBlack)
                    )

                    AnimatedVisibility(
                        visible = showClientDrop && filteredClients.isNotEmpty(),
                        enter   = fadeIn(tween(150)) + expandVertically(tween(180)),
                        exit    = fadeOut(tween(120)) + shrinkVertically(tween(140))
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 8.dp
                        ) {
                            Column {
                                filteredClients.take(5).forEach { name ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                selectedClient = name
                                                clientQuery = ""
                                                showClientDrop = false
                                            }
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(SGray100),
                                            Alignment.Center
                                        ) {
                                            Text(
                                                name.take(1).uppercase(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.W700,
                                                color = SGray600
                                            )
                                        }
                                        Text(name, fontSize = 14.sp, color = SBlack)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                SectionLabel("Agregar productos / servicios")
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(
                        SGray100
                    ).padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.Search, null, tint = SGray400, modifier = Modifier.size(16.dp))
                    BasicTextField(
                        value = productQuery, onValueChange = { productQuery = it },
                        modifier = Modifier.weight(1f), singleLine = true,
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = SBlack),
                        decorationBox = { inner ->
                            if (productQuery.isEmpty()) Text("Ítem o código…", color = SGray400, fontSize = 14.sp)
                            inner()
                        }
                    )
                    if (productQuery.isNotEmpty()) {
                        Icon(Icons.Default.Close, null, tint = SGray400, modifier = Modifier.size(14.dp)
                            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { productQuery = "" })
                    }
                }

                Spacer(Modifier.height(8.dp))

                filteredProducts.forEach { product ->
                    val inCart = cart.find { it.product.id == product.id }
                    ProductCartRow(product = product, inCart = inCart, onAdd = { addToCart(product) }, onRemove = { removeFromCart(product) })
                    HorizontalDivider(color = SGray100)
                }

                AnimatedVisibility(
                    visible = cart.isNotEmpty(),
                    enter   = fadeIn(tween(200)) + expandVertically(tween(280)),
                    exit    = fadeOut(tween(180)) + shrinkVertically(tween(220))
                ) {
                    Column {
                        Spacer(Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            SectionLabel("Resumen")
                            Text("${cart.sumOf { it.quantity }} ítem${if (cart.sumOf { it.quantity } != 1) "s" else ""}", fontSize = 12.sp, color = SGray400)
                        }
                        Spacer(Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(
                            SGray100
                        ).padding(16.dp)) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                cart.forEach { item ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text("${item.quantity}× ${item.product.name}", fontSize = 13.sp, color = SBlack, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text("Bs ${"%.2f".format(item.product.price * item.quantity)}", fontSize = 13.sp, fontWeight = FontWeight.W600, color = SBlack)
                                    }
                                }
                                HorizontalDivider(color = SGray200)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Total", fontSize = 15.sp, fontWeight = FontWeight.W700, color = SBlack)
                                    AnimatedContent(targetState = cartTotal, transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(120)) }, label = "total_anim") { total ->
                                        Text("Bs ${"%.2f".format(total)}", fontSize = 15.sp, fontWeight = FontWeight.W800, color = SAccent)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(120.dp))
            }

            Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 16.dp)) {
                val canConfirm = cart.isNotEmpty()
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (canConfirm) SBlack else SGray200)
                        .clickable(enabled = canConfirm, interactionSource = remember { MutableInteractionSource() }, indication = null) {
                            val newSale = SaleItem(
                                id         = (fakeSales.size + 1),
                                clientName = selectedClient.ifEmpty { "Cliente sin nombre" },
                                items      = cart.map { it.product.name },
                                total      = cartTotal,
                                time       = LocalTime.now(),
                                date       = LocalDate.now(),
                                status     = SaleStatus.COMPLETED
                            )
                            onConfirm(newSale); onDismiss()
                        }.padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.CheckCircle, null, tint = if (canConfirm) Color.White else SGray400, modifier = Modifier.size(18.dp))
                        Text(
                            if (canConfirm) "Confirmar venta · Bs ${"%.2f".format(cartTotal)}" else "Añade productos para continuar",
                            fontSize = 15.sp, fontWeight = FontWeight.W700, color = if (canConfirm) Color.White else SGray400
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCartRow(product: ProductItem, inCart: CartItem?, onAdd: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(product.color))
        Column(Modifier.weight(1f)) {
            Text(product.name, fontSize = 13.sp, fontWeight = FontWeight.W500, color = SBlack, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Bs ${"%.2f".format(product.price)}", fontSize = 12.sp, color = SGray400)
        }
        AnimatedContent(targetState = inCart, transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(140)) }, label = "cart_ctrl") { item ->
            if (item == null) {
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(SBlack).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onAdd), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(28.dp).clip(CircleShape).border(1.dp, SGray200, CircleShape).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onRemove), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Remove, null, tint = SBlack, modifier = Modifier.size(12.dp))
                    }
                    Text(item.quantity.toString(), fontSize = 14.sp, fontWeight = FontWeight.W700, color = SBlack)
                    Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(SBlack).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onAdd), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, fontSize = 11.sp, fontWeight = FontWeight.W700, color = SGray400, letterSpacing = 0.8.sp)
}