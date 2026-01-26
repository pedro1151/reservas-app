
package com.optic.pramosreservasappz.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogCapturePicture(
    state: MutableState<Boolean>,
    takePhoto: () -> Unit,
    pickImage: () -> Unit,
) {
    if (state.value) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp), // un poco más alto por botones
            onDismissRequest = { state.value = false },
            confirmButton = {},
            dismissButton = {},
            title = {
                Text(
                    text = "Selecciona una opción",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier.width(130.dp),
                        onClick = {
                            state.value = false
                            pickImage()
                        }
                    ) {
                        Text(text = "Galería")
                    }
                    Button(
                        modifier = Modifier.width(130.dp),
                        onClick = {
                            state.value = false
                            takePhoto()
                        }
                    ) {
                        Text(text = "Cámara")
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            iconContentColor = MaterialTheme.colorScheme.primary
        )
    }
}