package com.optic.pramosreservasappz.presentation.screens.recibos.selector

// 📄 Archivo nuevo:
// presentation/screens/recibos/components/ReceiptSelectorMenu.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.pramosreservasappz.domain.model.business.recibos.ReceiptType
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground

@Composable
fun ReceiptSelectorMenu(
    currentType: ReceiptType,
    onTypeSelected: (ReceiptType) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = "Tipo recibo",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(
                GradientBackground
            )
        ) {

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.STANDARD)
                            "✓ Estándar"
                        else
                            "Estándar"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.STANDARD)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.DARK)
                            "✓ Dark"
                        else
                            "Dark"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.DARK)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.COSMETIC)
                            "✓ Pastel"
                        else
                            "Pastel"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.COSMETIC)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.DRUG)
                            "✓ Drugstore"
                        else
                            "Drugstore"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.DRUG)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.PANADERIA)
                            "✓ Beige"
                        else
                            "Beige"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.PANADERIA)
                }
            )


            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.GAMMING)
                            "✓ Gamming"
                        else
                            "Gamming"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.GAMMING)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.METAL)
                            "✓ Metal"
                        else
                            "Metal"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.METAL)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.JUGUETERIA)
                            "✓ Juguetes"
                        else
                            "Juguetes"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.JUGUETERIA)
                }
            )


            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.ECO)
                            "✓ ECO"
                        else
                            "ECO"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.ECO)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.FUTURE)
                            "✓ Future"
                        else
                            "Future"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.FUTURE)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.RESTO)
                            "✓ Resto"
                        else
                            "Resto"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.RESTO)
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (currentType == ReceiptType.RESTO1)
                            "✓ Resto 1"
                        else
                            "Resto 1"
                    )
                },
                onClick = {
                    expanded = false
                    onTypeSelected(ReceiptType.RESTO1)
                }
            )
        }
    }
}