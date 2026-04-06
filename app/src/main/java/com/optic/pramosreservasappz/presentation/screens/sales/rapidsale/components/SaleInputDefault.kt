package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*

@Composable
fun SaleInputDefault(
    value: String,
    onValueChange: (value: String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardType = KeyboardType.Text

) {
    // INPUTS
    OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = {
                        Text(
                            text = label
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardOptions),
                    placeholder = {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(

                        // ✍️ texto
                        focusedTextColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedTextColor =  MaterialTheme.colorScheme.surface,

                        // 🏷 label
                        focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedLabelColor =  MaterialTheme.colorScheme.surface,

                        // 🔲 bordes
                        focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedBorderColor =  MaterialTheme.colorScheme.primaryContainer,

                        // ✨ cursor
                        cursorColor = MaterialTheme.colorScheme.primaryContainer,

                        // 🎨 fondo
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

}
