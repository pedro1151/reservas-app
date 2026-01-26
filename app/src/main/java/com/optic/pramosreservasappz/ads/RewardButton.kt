package com.optic.pramosreservasappz.ads

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun RewardButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Ver anuncio y ganar recompensa")
    }
}