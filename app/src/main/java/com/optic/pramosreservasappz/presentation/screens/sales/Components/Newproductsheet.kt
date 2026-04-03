package com.optic.pramosreservasappz.presentation.screens.sales.Components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
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
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.presentation.sales.Components.ProductItem
import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray100
import com.optic.pramosreservasappz.presentation.sales.Components.SGray200
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import com.optic.pramosreservasappz.presentation.sales.Components.SRed
import com.optic.pramosreservasappz.presentation.sales.Components.fakeProducts

private val productColorOptions = listOf(
    Color(0xFF5C6BC0), Color(0xFF26A69A), Color(0xFF7E57C2),
    Color(0xFFEF5350), Color(0xFFFF7043), Color(0xFFEC407A),
    Color(0xFF42A5F5), Color(0xFF66BB6A), Color(0xFFFFCA28),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductSheet(
    onDismiss: () -> Unit,
    onSave:    (ProductItem) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name          by remember { mutableStateOf("") }
    var priceText     by remember { mutableStateOf("") }
    var stockText     by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(productColorOptions[0]) }
    var nameError     by remember { mutableStateOf(false) }
    var priceError    by remember { mutableStateOf(false) }

    val price = priceText.toDoubleOrNull() ?: 0.0
    val stock = stockText.toIntOrNull() ?: 0

    fun validate(): Boolean {
        nameError  = name.isBlank()
        priceError = price <= 0.0
        return !nameError && !priceError
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
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nuevo producto", fontSize = 20.sp, fontWeight = FontWeight.W800, color = SBlack, letterSpacing = (-0.5).sp)
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(SGray100)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Close, null, tint = SGray600, modifier = Modifier.size(16.dp)) }
            }
            Text("Completa los datos del servicio o producto", fontSize = 13.sp, color = SGray400, modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 20.dp))

            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AnimatedVisibility(visible = name.isNotEmpty(), enter = fadeIn(tween(200)) + expandVertically(tween(250)), exit = fadeOut(tween(150)) + shrinkVertically(tween(200))) {
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(selectedColor.copy(alpha = 0.08f)).padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(selectedColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                                Text(name.take(2).uppercase(), fontSize = 13.sp, fontWeight = FontWeight.W800, color = selectedColor)
                            }
                            Column {
                                Text(name.ifEmpty { "Nombre del producto" }, fontSize = 15.sp, fontWeight = FontWeight.W600, color = SBlack)
                                if (price > 0) Text("Bs ${"%.2f".format(price)} · $stock en stock", fontSize = 12.sp, color = SGray400)
                            }
                        }
                    }
                }

                ProductTextField(value = name, onValueChange = { name = it; nameError = false }, label = "Nombre", placeholder = "Ej: Masaje relajante", icon = Icons.Outlined.Inventory2, isError = nameError, errorMsg = "El nombre es obligatorio")
                ProductTextField(value = priceText, onValueChange = { priceText = it.filter { c -> c.isDigit() || c == '.' }; priceError = false }, label = "Precio (Bs)", placeholder = "0.00", icon = Icons.Outlined.AttachMoney, keyboardType = KeyboardType.Decimal, isError = priceError, errorMsg = "Ingresa un precio válido")
                ProductTextField(value = stockText, onValueChange = { stockText = it.filter { c -> c.isDigit() } }, label = "Stock disponible", placeholder = "0", icon = Icons.Outlined.Layers, keyboardType = KeyboardType.Number)

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("COLOR DE ETIQUETA", fontSize = 11.sp, fontWeight = FontWeight.W700, color = SGray400, letterSpacing = 0.8.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        productColorOptions.forEach { color ->
                            val isSelected = color == selectedColor
                            Box(
                                modifier = Modifier.size(if (isSelected) 34.dp else 30.dp).clip(CircleShape).background(color)
                                    .then(if (isSelected) Modifier.border(2.dp, SBlack, CircleShape) else Modifier)
                                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { selectedColor = color },
                                contentAlignment = Alignment.Center
                            ) { if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
                        }
                    }
                }
                Spacer(Modifier.height(100.dp))
            }

            Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 16.dp)) {
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(
                        SBlack
                    )
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                            if (validate()) {
                                onSave(ProductItem(id = fakeProducts.size + 1, name = name.trim(), price = price, stock = stock, color = selectedColor))
                                onDismiss()
                            }
                        }.padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Text("Guardar producto", fontSize = 15.sp, fontWeight = FontWeight.W700, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductTextField(
    value: String, onValueChange: (String) -> Unit,
    label: String, placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false, errorMsg: String = ""
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.W700, color = if (isError) SRed else SGray400, letterSpacing = 0.8.sp)
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = SGray400, fontSize = 14.sp) },
            leadingIcon = { Icon(icon, null, tint = if (isError) SRed else SGray400, modifier = Modifier.size(18.dp)) },
            singleLine = true, isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SBlack, unfocusedBorderColor = if (isError) SRed else SGray200,
                errorBorderColor = SRed, focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White, errorContainerColor = SRed.copy(alpha = 0.04f)
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = SBlack)
        )
        AnimatedVisibility(visible = isError && errorMsg.isNotEmpty()) {
            Text(errorMsg, fontSize = 11.sp, color = SRed)
        }
    }
}