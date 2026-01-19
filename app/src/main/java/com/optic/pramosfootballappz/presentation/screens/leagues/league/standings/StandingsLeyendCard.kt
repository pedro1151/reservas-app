package com.optic.pramosfootballappz.presentation.screens.leagues.league.standings


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StandingsLegendCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
           // .background(MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
              //  .background(MaterialTheme.colorScheme.background),
        ) {

            val ModernYellow = Color(0xFFFFC107) // Amber 500
            val ModernRed = Color(0xFFE53935)    // Red 600
            val ModernGreen = Color(0xFF43A047)  // Green 600
            val ModernBlue = Color(0xFF1E88E5)   // Blue 600
            Spacer(modifier = Modifier.height(8.dp))

            LegendRow(color = ModernBlue, label = "Champions League")
            LegendRow(color = ModernGreen, label = "Europa League")
            LegendRow(color = ModernYellow, label = "Conference League / Playoffs")
            LegendRow(color = ModernRed, label = "Descenso")
        }
    }
}

@Composable
fun LegendRow(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}